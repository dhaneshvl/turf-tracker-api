package com.dw.razorpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails {
    private String order_id;
    private String internalOrderId;
    private int amount;
    private String currency;
    private String key;
}
