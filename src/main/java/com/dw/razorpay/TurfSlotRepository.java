package com.dw.razorpay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurfSlotRepository extends JpaRepository<TurfSlot, Long> {
    List<TurfSlot> findByTurfId(Long turfId);
}
