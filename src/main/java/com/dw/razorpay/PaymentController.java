package com.dw.razorpay;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/initiate/{amount}")
    public TransactionDetails initiateTransaction(@PathVariable Double amount) {
        return paymentService.initiateTransaction(amount);
    }

    @PostMapping("/complete")
    public String  completePayment(@RequestBody PaymentDetails paymentDetails) {
        String paymentId = paymentDetails.getPayment_id();
        String orderId = paymentDetails.getOrder_id();
        return "Payment processed successfully for Payment ID: " + paymentId + ", Order ID: " + orderId;
    }
}
