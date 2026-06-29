pipeline {
    agent any

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['dev', 'test', 'staging', 'prod'],
            description: 'Select environment to run tests against'
        )
        choice(
            name: 'TEST_SUITE',
            choices: ['api', 'ui', 'all'],
            description: 'Select which test suite to run'
        )
        string(
            name: 'BROWSER',
            defaultValue: 'CHROME',
            description: 'Browser for UI tests (CHROME, FIREFOX, EDGE)'
        )
        booleanParam(
            name: 'GENERATE_REPORT',
            defaultValue: true,
            description: 'Generate HTML test report'
        )
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }

    environment {
        // Maven settings
        MAVEN_HOME = '/usr/share/maven'
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=512m'

        // Environment-specific variables
        API_BASE_URL = getApiBaseUrl("${params.ENVIRONMENT}")
        APP_URL = getAppUrl("${params.ENVIRONMENT}")
        LOG_LEVEL = getLogLevel("${params.ENVIRONMENT}")
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "🔄 Checking out source code..."
                    checkout scm
                    echo "✅ Source code checked out successfully"
                }
            }
        }

        stage('Validate Environment') {
            steps {
                script {
                    echo "🔍 Validating environment configuration..."
                    echo "   Environment: ${params.ENVIRONMENT}"
                    echo "   Test Suite: ${params.TEST_SUITE}"
                    echo "   Browser: ${params.BROWSER}"
                    echo "   API Base URL: ${API_BASE_URL}"
                    echo "   App URL: ${APP_URL}"
                    echo "   Log Level: ${LOG_LEVEL}"

                    // Validate environment choice
                    if (!['dev', 'test', 'staging', 'prod'].contains(params.ENVIRONMENT)) {
                        error "❌ Invalid environment: ${params.ENVIRONMENT}"
                    }
                    echo "✅ Environment validation passed"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "🔨 Building project..."
                    withEnv([
                        "ENVIRONMENT=${params.ENVIRONMENT}",
                        "API_BASE_URL=${API_BASE_URL}",
                        "APP_URL=${APP_URL}"
                    ]) {
                        sh '''
                            mvn clean compile \
                                -DskipTests \
                                -Dapp.environment=${ENVIRONMENT} \
                                -Dapi.base.url=${API_BASE_URL} \
                                -Dapp.url=${APP_URL}
                        '''
                    }
                    echo "✅ Build completed successfully"
                }
            }
        }

        stage('Run API Tests') {
            when {
                expression { params.TEST_SUITE == 'api' || params.TEST_SUITE == 'all' }
            }
            steps {
                script {
                    echo "🧪 Running API Tests for ${params.ENVIRONMENT}..."
                    withEnv([
                        "ENVIRONMENT=${params.ENVIRONMENT}",
                        "API_BASE_URL=${API_BASE_URL}",
                        "LOG_LEVEL=${LOG_LEVEL}"
                    ]) {
                        sh '''
                            mvn clean test \
                                -Dapp.environment=${ENVIRONMENT} \
                                -Dapi.base.url=${API_BASE_URL} \
                                -Dlog.level=${LOG_LEVEL} \
                                -Dtest=BitApplicationTests \
                                -DsuiteXmlFile=src/test/resources/testng.xml
                        '''
                    }
                    echo "✅ API Tests completed"
                }
            }
        }

        stage('Run UI Tests') {
            when {
                expression { params.TEST_SUITE == 'ui' || params.TEST_SUITE == 'all' }
            }
            steps {
                script {
                    echo "🖥️  Running UI Tests for ${params.ENVIRONMENT}..."
                    withEnv([
                        "ENVIRONMENT=${params.ENVIRONMENT}",
                        "APP_URL=${APP_URL}",
                        "APP_BROWSER=${params.BROWSER}",
                        "LOG_LEVEL=${LOG_LEVEL}"
                    ]) {
                        sh '''
                            mvn clean test \
                                -Dapp.environment=${ENVIRONMENT} \
                                -Dapp.url=${APP_URL} \
                                -Dapp.browser=${APP_BROWSER} \
                                -Dlog.level=${LOG_LEVEL} \
                                -Dtest=ApprovalMoneyUITest \
                                -DsuiteXmlFile=src/test/resources/testng.xml
                        '''
                    }
                    echo "✅ UI Tests completed"
                }
            }
        }

        stage('Generate Reports') {
            when {
                expression { params.GENERATE_REPORT == true }
            }
            steps {
                script {
                    echo "📊 Generating test reports..."
                    sh '''
                        # Archive test results
                        mkdir -p reports
                        cp -r target/surefire-reports/* reports/ || true
                        cp -r logs/* reports/ || true
                    '''

                    // Publish test results
                    junit 'target/surefire-reports/*.xml'

                    echo "✅ Test reports generated"
                }
            }
        }
    }

    post {
        always {
            script {
                echo "🧹 Cleaning up..."

                // Archive logs
                archiveArtifacts artifacts: 'logs/**/*.log',
                                 allowEmptyArchive: true

                // Archive reports
                archiveArtifacts artifacts: 'target/surefire-reports/**',
                                 allowEmptyArchive: true

                // Publish HTML reports if they exist
                publishHTML([
                    reportDir: 'reports',
                    reportFiles: 'index.html',
                    reportName: "Test Report - ${params.ENVIRONMENT}",
                    allowMissing: true,
                    keepAll: true
                ])
            }
        }

        success {
            script {
                echo "✅ Pipeline executed successfully for ${params.ENVIRONMENT}"
                // Send success notification
                sendNotification("SUCCESS", "Tests passed for ${params.ENVIRONMENT}")
            }
        }

        failure {
            script {
                echo "❌ Pipeline failed for ${params.ENVIRONMENT}"
                // Send failure notification
                sendNotification("FAILURE", "Tests failed for ${params.ENVIRONMENT}")
            }
        }

        unstable {
            script {
                echo "⚠️  Pipeline unstable for ${params.ENVIRONMENT}"
                // Send unstable notification
                sendNotification("UNSTABLE", "Tests unstable for ${params.ENVIRONMENT}")
            }
        }

        cleanup {
            cleanWs()
        }
    }
}

// ==================== Helper Functions ====================

/**
 * Get API Base URL based on environment
 */
String getApiBaseUrl(String environment) {
    Map urls = [
        'dev': 'http://localhost:8080',
        'test': 'https://test-api.example.com',
        'staging': 'https://staging-api.example.com',
        'prod': 'https://api.example.com'
    ]
    return urls[environment] ?: 'http://localhost:8080'
}

/**
 * Get Application URL based on environment
 */
String getAppUrl(String environment) {
    Map urls = [
        'dev': 'http://localhost:8080/approval',
        'test': 'https://test.example.com/approval',
        'staging': 'https://staging.example.com/approval',
        'prod': 'https://www.example.com/approval'
    ]
    return urls[environment] ?: 'http://localhost:8080/approval'
}

/**
 * Get Log Level based on environment
 */
String getLogLevel(String environment) {
    Map levels = [
        'dev': 'DEBUG',
        'test': 'INFO',
        'staging': 'INFO',
        'prod': 'WARN'
    ]
    return levels[environment] ?: 'INFO'
}

/**
 * Send notification (email, Slack, etc.)
 */
void sendNotification(String status, String message) {
    echo "📢 Sending ${status} notification: ${message}"

    // Uncomment for actual notifications:
    // emailext(
    //     subject: "[${status}] Bit Payment API Tests - ${params.ENVIRONMENT}",
    //     body: message,
    //     to: 'team@example.com'
    // )

    // For Slack:
    // slackSend(
    //     color: status == 'SUCCESS' ? 'good' : 'danger',
    //     message: message
    // )
}