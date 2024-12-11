package com.example.ticketsystem.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class SimulationDetails {
    private long executionTime;
    private Map<Long, Integer> vendorTicketsAdded;
    private Map<Long, Integer> customerTicketsRetrieved;
    private int ticketsRemaining;

    public SimulationDetails() {
        this.vendorTicketsAdded = new HashMap<>();
        this.customerTicketsRetrieved = new HashMap<>();
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public Map<Long, Integer> getVendorTicketsAdded() {
        return vendorTicketsAdded == null ? null : Collections.unmodifiableMap(vendorTicketsAdded);
    }

    public void setVendorTicketsAdded(Map<Long, Integer> vendorTicketsAdded) {
        this.vendorTicketsAdded = vendorTicketsAdded;
    }

    public Map<Long, Integer> getCustomerTicketsRetrieved() {
        return customerTicketsRetrieved == null ? null : Collections.unmodifiableMap(customerTicketsRetrieved);
    }

    public void setCustomerTicketsRetrieved(Map<Long, Integer> customerTicketsRetrieved) {
        this.customerTicketsRetrieved = customerTicketsRetrieved;
    }

    public int getTicketsRemaining() {
        return ticketsRemaining;
    }

    public void setTicketsRemaining(int ticketsRemaining) {
        this.ticketsRemaining = ticketsRemaining;
    }

    public void addVendorTicket(Long vendorId) {
        vendorTicketsAdded.put(vendorId, vendorTicketsAdded.getOrDefault(vendorId, 0) + 1);
    }

    public void addCustomerTicket(Long customerId) {
        customerTicketsRetrieved.put(customerId, customerTicketsRetrieved.getOrDefault(customerId, 0) + 1);
    }
}
