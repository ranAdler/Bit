package com.example.bit.model;

public class PaymentRequest {

    private Double amount;

    private String senderPhone;

    private String receiverPhone;

    private String senderName;

    private String receiverName;

    public PaymentRequest() {}

    public PaymentRequest(Double amount, String senderPhone, String receiverPhone,
                         String senderName, String receiverName) {
        this.amount = amount;
        this.senderPhone = senderPhone;
        this.receiverPhone = receiverPhone;
        this.senderName = senderName;
        this.receiverName = receiverName;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Double amount;
        private String senderPhone;
        private String receiverPhone;
        private String senderName;
        private String receiverName;

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

        public Builder senderName(String senderName) {
            this.senderName = senderName;
            return this;
        }

        public Builder receiverName(String receiverName) {
            this.receiverName = receiverName;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(amount, senderPhone, receiverPhone, senderName, receiverName);
        }
    }
}