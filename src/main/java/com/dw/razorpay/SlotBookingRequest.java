package com.dw.razorpay;

import lombok.Data;

@Data
public class SlotBookingRequest {
    private Long turfId;
    private Double totalAmount;
    private String selectedSlots;
    private String slotDate;
}