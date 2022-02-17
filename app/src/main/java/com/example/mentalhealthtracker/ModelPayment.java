package com.example.mentalhealthtracker;

public class ModelPayment {
    String PaymentDate, PaymentAmount, PaidTo, PaymentStatus;

    public ModelPayment() {
    }

    public ModelPayment(String paymentDate, String paymentAmount, String paidTo, String paymentStatus) {
        PaymentDate = paymentDate;
        PaymentAmount = paymentAmount;
        PaidTo = paidTo;
        PaymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public String getPaidTo() {
        return PaidTo;
    }

    public void setPaidTo(String paidTo) {
        PaidTo = paidTo;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }
}
