package com.example.ticketsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_data")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Ticket() {
        // Default constructor needed by JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                // ... other attributes if any ...
                '}';
    }
}