package com.example.ticketsystem.models;

public class SimulationParameters {
    private int totalTickets;
    private int ticketReleaseRate; // in milliseconds
    private int customerRetrievalRate; // in milliseconds
    private int maxTicketCapacity;
    private int numVendors;
    private int numCustomers;

    public SimulationParameters() {
    }

    public SimulationParameters(int totalTickets, int ticketReleaseRate, int customerRetrievalRate,
                                int maxTicketCapacity, int numVendors, int numCustomers) {
        if (totalTickets <= 0 || ticketReleaseRate <= 0 || customerRetrievalRate <= 0 ||
                maxTicketCapacity <= 0 || numVendors <= 0 || numCustomers <= 0) {
            throw new IllegalArgumentException("All parameters must be greater than 0");
        }

        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.numVendors = numVendors;
        this.numCustomers = numCustomers;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        if (totalTickets <= 0) {
            throw new IllegalArgumentException("Total tickets must be greater than 0");
        }
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        if (ticketReleaseRate <= 0) {
            throw new IllegalArgumentException("Ticket release rate must be greater than 0");
        }
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        if (customerRetrievalRate <= 0) {
            throw new IllegalArgumentException("Customer retrieval rate must be greater than 0");
        }
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        if (maxTicketCapacity <= 0) {
            throw new IllegalArgumentException("Max ticket capacity must be greater than 0");
        }
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getNumVendors() {
        return numVendors;
    }

    public void setNumVendors(int numVendors) {
        if (numVendors <= 0) {
            throw new IllegalArgumentException("Number of vendors must be greater than 0");
        }
        this.numVendors = numVendors;
    }

    public int getNumCustomers() {
        return numCustomers;
    }

    public void setNumCustomers(int numCustomers) {
        if (numCustomers <= 0) {
            throw new IllegalArgumentException("Number of customers must be greater than 0");
        }
        this.numCustomers = numCustomers;
    }
}
