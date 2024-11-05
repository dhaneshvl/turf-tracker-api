package com.dw.razorpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turf-slots")
@CrossOrigin(origins = "*")
public class TurfSlotController {

    @Autowired
    private TurfSlotService turfSlotService;

    @GetMapping("/{turfId}")
    public List<TurfSlot> getTurfSlots(@PathVariable Long turfId, @RequestParam String slotDate) {
        return turfSlotService.getTurfSlotsByTurfId(turfId, slotDate);
    }
}
