package com.dw.razorpay;

import lombok.Data;

@Data
public class DeclinedPaymentDetails {
    private String orderId;
    private String message;
}
