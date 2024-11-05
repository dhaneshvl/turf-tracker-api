package com.dw.razorpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;

@Converter(autoApply = true)
public class MediaListConverter implements AttributeConverter<List<Media>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Media> media) {
        try {
            return objectMapper.writeValueAsString(media);
        } catch (IOException e) {
            throw new RuntimeException("Error converting media list to JSON", e);
        }
    }

    @Override
    public List<Media> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to media list", e);
        }
    }
}