# Events App - Multi-Cloud Java Spring Boot Application

A full-stack event management system designed for cloud-agnostic deployment and high availability. This project demonstrates the ability to architect, secure, and host a Java application across **AWS, Microsoft Azure, Google Cloud (GCP), and Heroku**.

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

### Sitemap & Navigation
This diagram shows the user flow and how different pages (Login, Home, Events, Search) are connected.

[Sitemap](/documents/Sitemap.jpg)
<br>
<br>
[Flowchart](/documets/Flowchart.jpg). 

## User Interface Diagrams:
Wireframes were designed in Figma to plan and validate the user experience before implementation.

[Wireframe](/documents/Wireframe.jpg)

### UML Class Diagram
This diagram represents the backend structure, showing the relationships between the Controller, Service, and Repository layers.
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

This project validates "write once, run anywhere" cloud deployment.

### Multi-Cloud Architecture Flowchart
This flowchart illustrates the request flow from the user to the specific cloud providers and the centralized database.


**Deployment Highlights:**
- **AWS:** Deployed via Elastic Beanstalk using Corretto 17 and RDS MySQL.
- **Microsoft Azure:** Hosted on Azure App Service with Azure Database for MySQL Flexible Server.
- **Google Cloud:** Implemented using App Engine (Java Flexible) and Cloud SQL.
- **Heroku:** Primary production host using JawsDB and Heroku Pipelines for CI/CD.

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
