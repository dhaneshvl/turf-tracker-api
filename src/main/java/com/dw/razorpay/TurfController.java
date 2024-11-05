package com.dw.razorpay;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/turfs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TurfController {

    private final TurfService turfService;

    @GetMapping
    public List<Turf> getTurfs() {
        return turfService.getAllTurfs();
    }

    @GetMapping("/{id}")
    public Optional<Turf> getTurfById(@PathVariable Long id) {
        return turfService.getTurfById(id);
    }
}
