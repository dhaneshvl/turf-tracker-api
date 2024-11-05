package com.dw.razorpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<TransactionDetails> initiatePayment(@RequestBody SlotBookingRequest slotBookingRequest) {
        return ResponseEntity.ok(paymentService.initiateTransaction(slotBookingRequest));
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completePayment(@RequestBody PaymentDetails paymentDetails) {
        return ResponseEntity.ok(paymentService.completePayment(paymentDetails));
    }

    @PostMapping("/declined")
    public ResponseEntity<String> handleDeclinedPayment(@RequestBody DeclinedPaymentDetails declinedPaymentDetails) {
        String result = paymentService.processDeclinedPayment(declinedPaymentDetails);
        return ResponseEntity.ok(result);
    }
}
