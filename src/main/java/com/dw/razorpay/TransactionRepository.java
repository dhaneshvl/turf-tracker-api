package com.dw.razorpay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, BigDecimal> {
    Optional<Transaction> findByRazorpayOrderId(String razorpayOrderId);
    @Query("SELECT t FROM Transaction t WHERE t.turfId = :turfId AND t.slotDate = :slotDate")
    List<Transaction> findByTurfIdAndSlotDate(
            @Param("turfId") Long turfId,
            @Param("slotDate") LocalDate slotDate
    );

}
