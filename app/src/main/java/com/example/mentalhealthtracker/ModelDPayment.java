package com.example.mentalhealthtracker;

public class ModelDPayment {
    String PaymentDate, PaymentAmount, PaidBy;

    public ModelDPayment() {
    }

    public ModelDPayment(String paymentDate, String paymentAmount, String paidBy) {
        PaymentDate = paymentDate;
        PaymentAmount = paymentAmount;
        PaidBy = paidBy;
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

    public String getPaidBy() {
        return PaidBy;
    }

    public void setPaidBy(String paidBy) {
        PaidBy = paidBy;
    }
}
