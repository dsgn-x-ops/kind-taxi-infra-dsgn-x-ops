package com.taxidata.processor.repository;

import com.taxidata.processor.model.TaxiRide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiRideRepository extends JpaRepository<TaxiRide, Long> {
}