package com.dw.razorpay;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private RazorpayClient razorpayClient;

    private RazorPayClientConfig razorPayClientConfig;

    @Autowired
    public PaymentService(RazorPayClientConfig razorpayClientConfig) throws RazorpayException {
        this.razorPayClientConfig = razorpayClientConfig;
        this.razorpayClient = new RazorpayClient(razorpayClientConfig.getKey(), razorpayClientConfig.getSecret());
    }

    public TransactionDetails initiateTransaction(Double amount) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currency", razorPayClientConfig.getCurrency());
            jsonObject.put("amount", (amount * 100));
            Order order = razorpayClient.orders.create(jsonObject);
           return prepareTransactionDetails(order);
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
        return null;
    }

    public TransactionDetails prepareTransactionDetails(Order order) {
        String order_id = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");
        return new TransactionDetails(order_id, currency, amount,razorPayClientConfig.getKey());
    }
}
