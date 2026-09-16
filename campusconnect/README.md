# CampusConnect – Student Internship, Placement & Opportunity Management System

CampusConnect is a comprehensive web application built for college placement cells, students, and recruiters to discover, post, track, and manage campus opportunities (internships, full-time jobs, hackathons, workshops, and certifications).

---

## 🚀 Tech Stack

- **Frontend:** HTML5, CSS3, JavaScript (ES6+), Bootstrap 5, FontAwesome
- **Backend:** Java 21, Spring Boot, Spring MVC, Spring Security + JWT
- **Database:** MySQL / PostgreSQL
- **Build Tool:** Maven
- **AI Feature:** AI Resume Matcher (Skill Extraction & Comparison)

---

## 📁 Project Structure

```text
campusconnect/
├── backend/                  # Spring Boot backend application
├── frontend/                 # HTML/CSS/JS frontend pages
├── database/                 # SQL schema and sample data scripts
└── docs/                     # Documentation & API specs
```

---

## ⚙️ Setup & Installation

### 1. Database Setup
Create a MySQL database named `campusconnect` and execute `database/schema.sql` and `database/sample-data.sql`.

### 2. Backend Setup
Navigate to `backend/`, configure your MySQL credentials in `src/main/resources/application.properties`, and run:
```bash
mvn spring-boot:run
```

### 3. Frontend Setup
Open `frontend/index.html` in your browser or serve it using a local static server (e.g., Live Server).

---

## 👥 Demo Credentials

- **Student:** `student@cc.com` / `password123`
- **Recruiter:** `recruiter@cc.com` / `password123`
- **Admin:** `admin@cc.com` / `password123`
