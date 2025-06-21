package com.taxidata.processor.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double latitude;
    private Double longitude;
    private String place;
}