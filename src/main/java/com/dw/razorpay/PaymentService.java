package com.dw.razorpay;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final RazorPayClientConfig razorPayClientConfig;
    private final TransactionRepository transactionRepository;

    @Autowired
    public PaymentService(RazorPayClientConfig razorPayClientConfig, TransactionRepository transactionRepository) throws RazorpayException {
        this.razorPayClientConfig = razorPayClientConfig;
        this.razorpayClient = new RazorpayClient(razorPayClientConfig.getKey(), razorPayClientConfig.getSecret());
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionDetails initiateTransaction(SlotBookingRequest slotBookingRequest) {
        try {
            LocalDate selectedSlotDate = LocalDate.parse(slotBookingRequest.getSlotDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currency", razorPayClientConfig.getCurrency());
            jsonObject.put("amount", (slotBookingRequest.getTotalAmount() * 100)); // Convert to the smallest currency unit

            Order order = razorpayClient.orders.create(jsonObject);

            String internalOrderId = UUID.randomUUID().toString();

            Transaction transaction = new Transaction();
            transaction.setInternalOrderId(internalOrderId);
            transaction.setRazorpayOrderId(order.get("id"));
            transaction.setAmount(slotBookingRequest.getTotalAmount());
            transaction.setCurrency(razorPayClientConfig.getCurrency());
            transaction.setSlotDate(selectedSlotDate);
            transaction.setStatus("INITIATED");
            transaction.setTurfId(slotBookingRequest.getTurfId());
            transaction.setSlotIds(slotBookingRequest.getSelectedSlots());
            transaction.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            return prepareTransactionDetails(order, internalOrderId);
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
        return null;
    }

    @Transactional
    public String completePayment(PaymentDetails paymentDetails) {
        Transaction transaction = transactionRepository.findByRazorpayOrderId(paymentDetails.getOrder_id())
                .orElseThrow(() -> new EntityNotFoundException("Order ID: " + paymentDetails.getOrder_id() + " not found in the system."));

        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setRazorpayPaymentId(paymentDetails.getPayment_id());

        String paymentStatus = (paymentDetails.getStatus() != null) ? paymentDetails.getStatus().toLowerCase() : "unknown";

        transaction.setStatus(switch (paymentStatus) {
            case "success" -> "COMPLETED";
            case "failed" -> {
                transaction.setPaymentRemarks(paymentDetails.getMessage() != null ? paymentDetails.getMessage() : "Failure reason not provided.");
                yield "FAILED";
            }
            default -> {
                transaction.setPaymentRemarks("Unrecognized or missing payment status.");
                yield "UNKNOWN_STATUS";
            }
        });

        transactionRepository.save(transaction);

        return "Payment processed for Payment ID: " + paymentDetails.getPayment_id() + ", Order ID: " + paymentDetails.getOrder_id() +
                " with status: " + transaction.getStatus();
    }

    @Transactional
    public String processDeclinedPayment(DeclinedPaymentDetails declinedPaymentDetails) {
        Optional<Transaction> optionalTransaction = transactionRepository.findByRazorpayOrderId(declinedPaymentDetails.getOrderId());

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setStatus("DECLINED");
            transaction.setPaymentRemarks(declinedPaymentDetails.getMessage());
            transaction.setUpdatedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            return "Declined payment recorded for Order ID: " + declinedPaymentDetails.getOrderId();
        } else {
            return "Order ID: " + declinedPaymentDetails.getOrderId() + " not found in the system.";
        }
    }

    public TransactionDetails prepareTransactionDetails(Order order, String internalOrderId) {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setOrder_id(order.get("id"));
        transactionDetails.setInternalOrderId(internalOrderId);
        transactionDetails.setAmount(order.get("amount"));
        transactionDetails.setCurrency(razorPayClientConfig.getCurrency());
        transactionDetails.setKey(razorPayClientConfig.getKey());
        return transactionDetails;
    }
}
