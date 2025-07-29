# Library Management Console App

A console-based Java application for managing a library system, containerized using Docker, MySQL, and Adminer.

## Features

- Console-based interactive app
- MySQL-backed storage
- Adminer for web-based DB browsing
- Configurable via `.env` file
- Containerized with Docker Compose

---

## Prerequisites

- Docker
- Docker Compose

---

## How to Build and Run
Start all services:

```bash
docker-compose up --build -d
```

This will start:
- `db`: MySQL server (with optional init scripts)
- `library-app`: The Java console app (interactive)
- `adminer`: Web interface for DB management

---

## How to Use the App (Terminal Access)

Since the app expects input from the terminal, attach to the container like this:

```bash
docker attach library-app
```

You can now use the Java app interactively.

> To safely detach from the container without stopping it, press:
> `Ctrl + P`, then `Ctrl + Q`

---

## Access Adminer

To explore or debug the database using a browser:

- Open: [http://localhost:8080](http://localhost:8080)
- Use the credentials in the .env file.

---

## Stop and Clean Up

Stop and remove all containers and volumes:

```bash
docker-compose down
```
---

## Database Initialization

SQL scripts placed inside `db-init/` (e.g., `init.sql`) will be automatically executed when the DB container is created for the first time.
This will crate the database schema and add a root admin user having the name "root admin".

---

## Notes

- The Java app uses `stdin`, so it requires an attached terminal.
- Adminer is provided for convenient database access.
