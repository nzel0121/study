package com.brownfield.pss.fares.component;

import com.brownfield.pss.fares.entity.Fare;
import com.brownfield.pss.fares.repository.FaresRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FaresComponent {

    private static final Logger logger = LoggerFactory.getLogger(FaresComponent.class);

    private final FaresRepository faresRepository;

    public FaresComponent(FaresRepository faresRepository) {
        this.faresRepository = faresRepository;
    }

    public Fare getFare(String flightNumber, String flightDate) {
        logger.info("Looking for fares flightNumber "+ flightNumber + " flightDate "+ flightDate);
        return faresRepository.getFareByFlightNumberAndFlightDate(flightNumber, flightDate);
    }
}
