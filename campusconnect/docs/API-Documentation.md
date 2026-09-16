# CampusConnect - API Documentation

Base URL: `http://localhost:8080/api`

---

## 1. Authentication APIs

### Register
- **POST** `/auth/register`
- **Body:**
  ```json
  {
    "name": "Somajit Khatua",
    "email": "somajit@gmail.com",
    "password": "password123",
    "role": "STUDENT",
    "college": "NIT"
  }
  ```

### Login
- **POST** `/auth/login`
- **Body:**
  ```json
  {
    "email": "student@cc.com",
    "password": "password123"
  }
  ```

---

## 2. Student APIs

- **GET** `/students/profile` - Get student profile
- **PUT** `/students/profile` - Update student profile

---

## 3. Opportunity APIs

- **GET** `/opportunities` - Get all opportunities
- **GET** `/opportunities/{id}` - Get opportunity by ID
- **POST** `/opportunities` - Post new opportunity (Recruiter)
- **GET** `/opportunities/search?keyword=java` - Search opportunities
- **DELETE** `/opportunities/{id}` - Delete opportunity

---

## 4. Application APIs

- **POST** `/applications/{opportunityId}` - Apply for an opportunity
- **GET** `/applications/my` - Get student's applications
- **GET** `/applications/recruiter` - Get applications for recruiter
- **PUT** `/applications/{id}/status?status=SHORTLISTED` - Update application status

---

## 5. AI Resume Matcher API

- **GET** `/resume/match/{opportunityId}` - Compare student skills with opportunity required skills and return match score & missing skills.
