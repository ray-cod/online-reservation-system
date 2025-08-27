# ReserVate — Online Reservation System

A clean, professional single-page reservation system built with **Java**, **Spring Boot**, **Thymeleaf**, **Spring Security**, and **H2**.
This project is designed as a portfolio-ready sample showing a full-stack Java web app with authentication, a simple booking flow, and REST endpoints for front-end interaction.

---

## 🚀 Highlights

* Full Spring Boot app with layered architecture: **entities → repositories → services → controllers**.
* Authentication with **Spring Security** and password hashing (BCrypt).
* Single-page user experience (search / book / view / cancel) built in Thymeleaf + JavaScript.
* H2 in-memory DB for quick dev/testing, with optional persistent file mode.
* Clean, modern UI (responsive) with separate CSS for easy theming.
* Seeded test data on startup (ApplicationRunner) for quick demo.

---

## 🧰 Tech stack

* Java 17, Spring Boot 3.x
* Spring Data JPA, Hibernate
* Spring Security (form-based login)
* Thymeleaf for server-side views
* H2 database (runtime)
* Lombok for boilerplate reduction
* Build: Maven

---

## ✅ Features

* Sign up / login (secure, passwords are BCrypt hashed).
* Search trains by class/date (single-page UX).
* Book a seat (creates a `Reservation`, decrements seat count).
* Cancel by PNR or via “My reservations”.
* Admin/developer friendly: H2 console and seed/test data.
* REST endpoints for integration / AJAX requests.

---

## Quick start (run locally)

1. Clone the project:

```bash
git clone https://github.com/ray-cod/online-reservation-system.git
cd online-reservation-system
```

2. Build & run:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

or

```bash
mvn clean spring-boot:run
```

3. Open in browser:

* App front-end: `http://localhost:8080/` (home page / single-page reservation)
* H2 console (dev): `http://localhost:8080/h2-console`

  * JDBC URL: `jdbc:h2:mem:reservation_db`
  * Username: `sa` (empty password)

---

## Seeded demo data

If you included the ApplicationRunner test data loader, on startup the app seeds:

* **10 trains**
* **2 users**

  * `alice` / `password1`
  * `bob` / `password2`
* **3 reservations** for each user (6 total)

> Use these credentials to log in and demo “My reservations” flows.

---

## Configuration notes (application.properties)

Important properties used in development:

```properties
# H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Datasource (in-memory)
spring.datasource.url=jdbc:h2:mem:reservation_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

To persist DB to disk between runs:

```properties
spring.datasource.url=jdbc:h2:file:./data/reservate
```

To enable seeding only in dev:

* Annotate the loader with `@Profile("dev")` and start with `--spring.profiles.active=dev`.

---

## API (important endpoints)

> Note: endpoints that modify or return user-specific data require authentication (session cookie).

* `GET  /` — Home / single-page UI (Thymeleaf)

### Public / unauthenticated

* `GET  /signup` — Sign up page
* `POST /signup` — Register new users (form)
* `GET  /login` — Login page

### Train APIs

* `GET  /api/trains` — List all trains
* `GET  /api/trains/class/{classType}?date=YYYY-MM-DD` — Trains by class (and optional date)

### Reservations

* `POST /api/reservations/book` — Book a reservation

  * Typical fields: `trainId`, `passengerName`, `classType`, `journeyDate`, `fromStation`, `toStation`.
* `GET  /api/reservations/user/me` — Get reservations for the logged-in user
* `PUT /api/reservations/cancel/{pnr}` — Cancel by PNR

---

## DTOs & Binding

* Used small request DTOs for form submissions (example `ReservationRequestDTO` with fields like `trainId`, `passengerName`, `classType`).
* Keep entity models separate from DTOs to avoid accidental persistence of UI-only fields (e.g., `confirmPassword`).

---

## Tests

* Unit tests can be added for services (mock repositories) and controllers (MockMvc).
* Integration tests: use `@SpringBootTest` with an in-memory H2 DB.

---

## Screenshots & demo

Include screenshots in `docs/` or the repo root to showcase:

* Home / search panel
* Booking modal
* “My reservations” list
* H2 console (optional)

---

## Contributing / Usage notes

This is a portfolio project — feel free to fork and experiment.
If you re-use code, please keep attribution or use the MIT license.

---

## License

**MIT** — simple and permissive.

---

## Contact

If you’d like to see a walkthrough or want improvements:

* Author: *Raimi Dikamona Lassissi*
* Email: `rdikamona9@gmail.com`
* Repo: `https://github.com/ray-cod/online-reservation-system.git`

