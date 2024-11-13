package com.taxidata.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taxidata.api.model.Location;
import com.taxidata.api.model.TaxiRide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxiRideDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Location start;
    private Location end;

    @Builder.Default
    private List<Location> importantPlaces = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private Double price;
    private Double distanceKm;

    private long durationInMinutes;
    private boolean inProgress;

    public static TaxiRideDTO fromEntity(TaxiRide entity) {
        if (entity == null) {
            return null;
        }

        return TaxiRideDTO.builder()
                .id(entity.getId())
                .start(entity.getStart())
                .end(entity.getEnd())
                .importantPlaces(new ArrayList<>(entity.getImportantPlaces()))
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .price(entity.getPrice())
                .distanceKm(entity.getDistanceKm())
                .durationInMinutes(entity.getDurationInMinutes())
                .inProgress(entity.isInProgress())
                .build();
    }

    public TaxiRide toEntity() {
        TaxiRide entity = new TaxiRide();
        entity.setId(this.id);
        entity.setStart(this.start);
        entity.setEnd(this.end);
        entity.setImportantPlaces(new ArrayList<>(this.importantPlaces));
        entity.setStartDate(this.startDate);
        entity.setEndDate(this.endDate);
        entity.setPrice(this.price);
        entity.setDistanceKm(this.distanceKm);
        return entity;
    }

    public void addImportantPlace(Location location) {
        if (this.importantPlaces == null) {
            this.importantPlaces = new ArrayList<>();
        }
        this.importantPlaces.add(location);
    }

    public void removeImportantPlace(Location location) {
        if (this.importantPlaces != null) {
            this.importantPlaces.remove(location);
        }
    }
}