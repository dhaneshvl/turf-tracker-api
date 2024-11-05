package com.dw.razorpay;

import lombok.Data;

@Data
public class PaymentDetails {
    private String payment_id;
    private String order_id;
    private String status;
    private String message;
}
