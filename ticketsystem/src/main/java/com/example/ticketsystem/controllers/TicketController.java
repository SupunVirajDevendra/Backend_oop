package com.example.ticketsystem.controllers;

import com.example.ticketsystem.models.SimulationDetails;
import com.example.ticketsystem.models.SimulationParameters;
import com.example.ticketsystem.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/simulation/start")
    public ResponseEntity<?> startSimulation(@RequestBody SimulationParameters parameters) throws URISyntaxException {
        System.out.println("startSimulation: Received parameters - " + parameters);

        try {
            String simulationId = ticketService.startSimulation(parameters);
            System.out.println("startSimulation: Simulation started.");

            // Construct the URI for the newly created simulation resource
            URI location = new URI("/api/simulation/details");

            // Return the 201 Created status with the Location header and the ID in the body
            return ResponseEntity.created(location).body(simulationId);
        } catch (IllegalArgumentException e) {
            System.err.println("startSimulation: Error - " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/simulation/stop")
    public ResponseEntity<SimulationDetails> stopSimulation() {
        SimulationDetails details = ticketService.stopSimulation();
        System.out.println("stopSimulation: Details - " + details);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/simulation/details")
    public ResponseEntity<SimulationDetails> getSimulationDetails() {
        SimulationDetails details = ticketService.getSimulationDetails();
        System.out.println("getSimulationDetails: Details - " + details);
        return ResponseEntity.ok(details);
    }
}
