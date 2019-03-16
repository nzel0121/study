package com.brownfield.pss.search;

import com.brownfield.pss.search.entity.Fares;
import com.brownfield.pss.search.entity.Flight;
import com.brownfield.pss.search.entity.Inventory;
import com.brownfield.pss.search.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    FlightRepository flightRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Flight> flights = Arrays.asList(
                new Flight("BF100", "SEA", "SFO", "22-JAN-18", new Fares("100", "USD"), new Inventory(100)),
                new Flight("BF101", "NYC", "SFO", "22-JAN-18", new Fares("101", "USD"), new Inventory(100)),
                new Flight("BF105", "NYC", "SFO", "22-JAN-18", new Fares("105", "USD"), new Inventory(100)),
                new Flight("BF106", "NYC", "SFO", "22-JAN-18", new Fares("106", "USD"), new Inventory(100)),
                new Flight("BF102", "CHI", "SFO", "22-JAN-18", new Fares("102", "USD"), new Inventory(100)),
                new Flight("BF103", "HOU", "SFO", "22-JAN-18", new Fares("103", "USD"), new Inventory(100)),
                new Flight("BF104", "LAX", "SFO", "22-JAN-18", new Fares("104", "USD"), new Inventory(100))
        );
        flightRepository.saveAll(flights);
    }
}
