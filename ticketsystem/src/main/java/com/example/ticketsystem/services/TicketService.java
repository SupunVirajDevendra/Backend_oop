package com.example.ticketsystem.services;

import com.example.ticketsystem.models.SimulationDetails;
import com.example.ticketsystem.models.SimulationParameters;
import com.example.ticketsystem.models.Ticket;
import com.example.ticketsystem.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketService {
    private static final int FIXED_SIMULATION_ID = 1;
    @Autowired
    private TicketRepository ticketRepository;

    private final ConcurrentLinkedQueue<Ticket> ticketQueue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private ExecutorService vendorExecutor;
    private ExecutorService customerExecutor;
    private long startTime;
    private SimulationDetails simulationDetails;
    private SimulationParameters parameters;
    private final Lock queueLock = new ReentrantLock();
    private List<Thread> activeThreads = new ArrayList<>();

    public synchronized String startSimulation(SimulationParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Invalid parameters: parameters must not be null");
        }
        if (parameters.getTotalTickets() <= 0 || parameters.getNumVendors() <= 0) {
            throw new IllegalArgumentException("Invalid parameters: totalTickets, numVendors must be a positive values");
        }

        this.parameters = parameters;
        ticketQueue.clear();
        isRunning.set(true);
        startTime = System.currentTimeMillis();
        simulationDetails = new SimulationDetails();
        simulationDetails.setVendorTicketsAdded(new HashMap<>());
        simulationDetails.setCustomerTicketsRetrieved(new HashMap<>());

        vendorExecutor = Executors.newFixedThreadPool(parameters.getNumVendors());
        customerExecutor = Executors.newFixedThreadPool(parameters.getNumCustomers());

        activeThreads.clear(); // Clear the list of active threads

        // Start vendor threads
        for (int i = 0; i < parameters.getNumVendors(); i++) {
            long vendorId = i;
            VendorTask vendorTask = new VendorTask(vendorId);
            Thread vendorThread = new Thread(vendorTask);
            vendorExecutor.submit(vendorThread);
            activeThreads.add(vendorThread);
        }

        // Start customer threads
        for (int i = 0; i < parameters.getNumCustomers(); i++) {
            long customerId = i;
            CustomerTask customerTask = new CustomerTask(customerId);
            Thread customerThread = new Thread(customerTask);
            customerExecutor.submit(customerThread);
            activeThreads.add(customerThread);
        }

        return String.valueOf(FIXED_SIMULATION_ID);
    }

    public class VendorTask implements Runnable {
        private final long vendorId;

        public VendorTask(long vendorId) {
            this.vendorId = vendorId;
        }

        @Override
        public void run() {
            long maxTicketsPerVendor = parameters.getTotalTickets() / parameters.getNumVendors();
            while (isRunning.get() && simulationDetails.getVendorTicketsAdded().getOrDefault(vendorId, 0) < maxTicketsPerVendor) {
                try {
                    addTicket(vendorId);
                    for (int i = 0; i < parameters.getTicketReleaseRate()/100; i++) {
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Re-interrupt the thread
                    break;
                }
            }
        }
    }

    public class CustomerTask implements Runnable {
        private final long customerId;

        public CustomerTask(long customerId) {
            this.customerId = customerId;
        }

        @Override
        public void run() {
            while (isRunning.get()) {
                try {
                    retrieveTicket(customerId);
                    for (int i = 0; i < parameters.getCustomerRetrievalRate()/100; i++) {
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Transactional
    public void addTicket(long vendorId) {
        if (parameters == null || ticketQueue.size() >= parameters.getMaxTicketCapacity()) {
            return;
        }
        Ticket ticket = new Ticket();
        ticket = ticketRepository.save(ticket);
        queueLock.lock();
        try {
            ticketQueue.offer(ticket);
            simulationDetails.addVendorTicket(vendorId);
        } finally {
            queueLock.unlock();
        }
    }

    @Transactional
    public void retrieveTicket(long customerId) {
        queueLock.lock();
        try {
            if (!ticketQueue.isEmpty()) {
                Ticket ticket = ticketQueue.poll();
                if (ticket != null) {
                    simulationDetails.addCustomerTicket(customerId);
                    simulationDetails.setTicketsRemaining(ticketQueue.size()); // Update count of remaining tickets
                    ticketRepository.delete(ticket);
                }
            }
        } finally {
            queueLock.unlock();
        }
    }

    public SimulationDetails stopSimulation() {
        isRunning.set(false);

        // Interrupt all active threads
        for (Thread thread : activeThreads) {
            thread.interrupt();
        }

        shutdownExecutor(vendorExecutor);
        shutdownExecutor(customerExecutor);
        simulationDetails.setExecutionTime(System.currentTimeMillis() - startTime);
        return simulationDetails;
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public SimulationDetails getSimulationDetails() {
        SimulationDetails detailsCopy = new SimulationDetails();
        detailsCopy.setExecutionTime(simulationDetails.getExecutionTime());

        // Unmodifiable copies of the maps
        detailsCopy.setVendorTicketsAdded(new HashMap<>(simulationDetails.getVendorTicketsAdded()));
        detailsCopy.setCustomerTicketsRetrieved(new HashMap<>(simulationDetails.getCustomerTicketsRetrieved()));
        detailsCopy.setTicketsRemaining(ticketQueue.size());

        return detailsCopy;
    }
}
