# Gravalign Online

Gravalign Online is an in-progress Java and Spring Boot portfolio project built around training puzzles for the classic Four In A Row game.

## Current Status

This project currently has a local Spring Boot application with a simple static frontend and REST API endpoints for requesting and attempting puzzles.

## Tech Stack

- Java 25
- Spring Boot 4
- Maven
- Spring Web / REST API
- HTML
- CSS
- JavaScript

## Current Features

- Static home page
- Static puzzle page
- Board rendering in the browser
- Column-click puzzle attempts
- REST endpoint for getting the next puzzle
- REST endpoint for submitting a puzzle attempt
- Simplified Java board logic

## Running Locally

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open the app in your browser:

http://localhost:8080