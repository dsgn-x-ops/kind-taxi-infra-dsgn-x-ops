package com.taxidata.processor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taxi_rides")
public class TaxiRide implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Location start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "end_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "end_longitude")),
            @AttributeOverride(name = "place", column = @Column(name = "end_place"))
    })
    private Location end;

    @ElementCollection
    @CollectionTable(
            name = "important_places",
            joinColumns = @JoinColumn(name = "ride_id")
    )
    @Builder.Default
    private List<Location> importantPlaces = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Double price;

    @Column(name = "distance_km")
    private Double distanceKm;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Version
    @Column(name = "version")
    private Long version;

    @PrePersist
    @PreUpdate
    protected void validateAndInitialize() {
        // Inicialização
        if (this.importantPlaces == null) {
            this.importantPlaces = new ArrayList<>();
        }

        // Validações
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("End date cannot be before start date");
        }
        if (price != null && price < 0) {
            throw new IllegalStateException("Price cannot be negative");
        }
        if (distanceKm != null && distanceKm < 0) {
            throw new IllegalStateException("Distance cannot be negative");
        }
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

    public long getDurationInMinutes() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return java.time.Duration.between(startDate, endDate).toMinutes();
    }

    public boolean isInProgress() {
        return startDate != null && endDate == null;
    }
}