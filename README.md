# Backend for Real-Time Event Ticketing System

## Introduction

This is the backend component of the Real-Time Event Ticketing System, responsible for handling ticket releases from vendors, processing customer purchases, and managing the shared ticket pool. It uses a multi-threaded approach with thread-safe locking, as well as OOP programming concepts for modular design. This system maintains real time ticket management and distribution from multiple vendors to many consumers.

## Technologies Used

*   **Language:** Java 21
*   **Framework:** Spring Boot 3.2.0
*   **Database:** H2
*    **Build Tool:** Maven
*   **Threading:** Utilizes thread safe mechanisms using java `Synchronized`  keyword to implement appropriate thread management and protection of resources against race conditions and deadlocks.
