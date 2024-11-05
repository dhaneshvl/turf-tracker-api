package com.dw.razorpay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurfService {

    private final TurfRepository turfRepository;
    public List<Turf> getAllTurfs() {
        return turfRepository.findAll();
    }
    public Optional<Turf> getTurfById(Long id) {
        return turfRepository.findById(id);
    }
}
