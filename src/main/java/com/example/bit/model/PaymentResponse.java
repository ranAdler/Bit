package com.example.bit.model;

public class PaymentResponse {

    private String transactionId;

    private Double amount;

    private String senderPhone;

    private String receiverPhone;

    private String status;

    private String timestamp;

    public PaymentResponse() {}

    public PaymentResponse(String transactionId, Double amount, String senderPhone,
                          String receiverPhone, String status, String timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.senderPhone = senderPhone;
        this.receiverPhone = receiverPhone;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String transactionId;
        private Double amount;
        private String senderPhone;
        private String receiverPhone;
        private String status;
        private String timestamp;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder senderPhone(String senderPhone) {
            this.senderPhone = senderPhone;
            return this;
        }

        public Builder receiverPhone(String receiverPhone) {
            this.receiverPhone = receiverPhone;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(transactionId, amount, senderPhone, receiverPhone, status, timestamp);
        }
    }
}