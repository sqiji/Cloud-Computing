# Events App - Multi-Cloud Java Spring Boot Application
A **full-stack milestone project** developed to simulate an event management system designed for high availability. This project demonstrates the ability to architect, secure, and host a Java application, with specific configurations validated for **AWS**, **Microsoft Azure**, **Google Cloud (GCP)**, and **Heroku**.

---

## Table of Contents
- [Features](#-features)
- [System Design](#-system-design)
- [Technical Stack](#-technical-stack)
- [Cloud Infrastructure](#-cloud-infrastructure)
- [DevOps & Monitoring](#-devops--monitoring)

---

## Features
- **User Authentication:** Secure registration and login using BCrypt encryption.
- **Event Management:** Full CRUD (Create, Read, Update, Delete) for event scheduling.
- **Search Functionality:** Dynamic search to find events by title or description.
- **Responsive Interface:** Mobile-friendly design powered by Thymeleaf and Bootstrap.

---

## System Design

### Sitemap & Navigation:
This diagram shows the user flow and how different pages (Login, Home, Events, Search) are connected.

[Sitemap](/documents/Sitemap.jpg)

### Application Architecture:
This flowchart illustrates the request flow from the user through the cloud environment to the persistence layer.

[Flowchart](/documets/Flowchart.jpg). 

## User Interface Diagrams:
Wireframes were designed in Figma to plan and validate the user experience before implementation.

[Wireframe](/documents/Wireframe.jpg)

### UML Class Diagram
Represents the backend structure, showing the relationships between the Controller, Service, and Repository layers.
<br>
[UML diagram](/documents/UML.jpg).


### Database Schema (ER Diagram)
The relational structure for our User and Event data.
<br>

[ER diagram](/documents/UML.jpg).

---

## Technical Stack
- **Backend:** Java 17, Spring Boot 3.3.0
- **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap
- **Database:** MySQL (Local & Cloud-hosted)
- **Containerization:** Docker

---

## Cloud Infrastructure
The application is architected for cross-platform compatibility. While not currently live to manage subscription costs, the codebase is fully deployment-ready.

The repository includes a _deployment-configs/_ directory containing the specific environment variables, property files, and build configurations validated for:

- **Heroku (Prime Configuration):** Fully configuration with _system.properties_ and JawsDB integration for Heroku Pipelines.
- **AWS (Validated):** Compatible with Elastic Beanstalk (Corretto 17) and RDS MySQL.
- **Microsoft Azure (Validated):** Configurated for Azure App Service with Azure Database for MySQL.
- **Google Cloud (Validated):** Ready for App Emgine (Java Flexible) and Cloud SQL.


**Deployment and Monitoring:**
The project implements professional-grade monitoring and deployment workflows:
- **CI/CD:** Configuration ready for Heroku Piplines and GitHub Actions.
- **Logging:** Centralized logs using **SLF4J** and **Loggly**.
- **Health Check:** Pre-configured for **Uptime Robot** monitoring upon deployment.

---

## DevOps & Monitoring
We implemented a "Fail Fast, Recover Quickly" philosophy:

- **CI/CD:** Automated deployment via GitHub Actions and Heroku Pipelines.
- **Logging:** Centralized logs using **SLF4J** and **Loggly**.
- **Health Checks:** 24/7 uptime monitoring via **Uptime Robot**.

---
**Author: Soran Qiji\
Course: Cloud Computing\
Grand Canyon University**
