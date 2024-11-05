package com.dw.razorpay;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private double rating;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "text")
    @Convert(converter = MediaListConverter.class)
    private List<Media> media;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class Location {
    private String city;
    private String address;
}