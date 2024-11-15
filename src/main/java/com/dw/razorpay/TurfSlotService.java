package com.dw.razorpay;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TurfSlotService {
    private final TurfSlotRepository turfSlotRepository;
    private final TransactionRepository transactionRepository;
    public TurfSlotService(TurfSlotRepository turfSlotRepository, TransactionRepository transactionRepository) {
        this.turfSlotRepository = turfSlotRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<TurfSlot> getTurfSlotsByTurfId(Long turfId, String slotDate) {
        LocalDate selectedSlotDate = LocalDate.parse(slotDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Set<Long> sealedSlots = transactionRepository.findByTurfIdAndSlotDate(turfId, selectedSlotDate).stream()
                .flatMap(transaction -> Arrays.stream(transaction.getSlotIds().split(","))
                        .map(String::trim)
                        .map(Long::valueOf))
                .collect(Collectors.toSet());

        return turfSlotRepository.findByTurfId(turfId).stream()
                .peek(turfSlot -> {
                    if (sealedSlots.contains(turfSlot.getId())) {
                        turfSlot.setAvailability(false);
                    }
                })
                .sorted(Comparator.comparingInt(TurfSlot::getSortOrder))
                .collect(Collectors.toList());
    }


}
