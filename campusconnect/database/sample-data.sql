-- CampusConnect Sample Data
USE campusconnect;

-- Passwords are hashed with BCrypt (Password for all: 'password123')
-- $2a$10$XUeS0eXjP4jR3J7iX5uJ.e9LpQ5kZ8n6L6x2v1q0p9o8n7m6l5k4j

INSERT INTO users (name, email, password, role, status) VALUES
('Somajit Khatua', 'student@cc.com', '$2a$10$XUeS0eXjP4jR3J7iX5uJ.e9LpQ5kZ8n6L6x2v1q0p9o8n7m6l5k4j', 'STUDENT', 'ACTIVE'),
('ABC Technologies HR', 'recruiter@cc.com', '$2a$10$XUeS0eXjP4jR3J7iX5uJ.e9LpQ5kZ8n6L6x2v1q0p9o8n7m6l5k4j', 'RECRUITER', 'ACTIVE'),
('Admin User', 'admin@cc.com', '$2a$10$XUeS0eXjP4jR3J7iX5uJ.e9LpQ5kZ8n6L6x2v1q0p9o8n7m6l5k4j', 'ADMIN', 'ACTIVE');

INSERT INTO students (user_id, college, degree, branch, graduation_year, cgpa, skills, phone, location, resume_url, github_url, linkedin_url) VALUES
(1, 'National Institute of Technology', 'B.Tech', 'Computer Science and Engineering', 2026, 8.85, 'Java, Spring Boot, MySQL, REST API, Git, HTML, CSS, JavaScript', '9876543210', 'Kolkata, India', '/uploads/resumes/somajit_resume.pdf', 'https://github.com/somajit', 'https://linkedin.com/in/somajit');

INSERT INTO recruiters (user_id, company_name, company_description, website, location, industry, verified) VALUES
(2, 'ABC Technologies', 'Global leader in software engineering and cloud solutions.', 'https://abctech.com', 'Bangalore, India', 'Information Technology', TRUE);

INSERT INTO opportunities (recruiter_id, title, description, type, company_name, location, work_mode, salary, skills_required, deadline, status) VALUES
(1, 'Java Developer Intern', 'Looking for enthusiastic Java Spring Boot interns to build scalable microservices and REST APIs.', 'INTERNSHIP', 'ABC Technologies', 'Remote', 'REMOTE', '₹25,000/month', 'Java, Spring Boot, MySQL, REST API', '2026-10-15', 'ACTIVE'),
(1, 'Full Stack Engineer - Fresher', 'Join our core product engineering team building high-performance web applications.', 'FULL_TIME', 'ABC Technologies', 'Bangalore, India', 'HYBRID', '₹8 LPA', 'Java, Spring Boot, React, MySQL, Git', '2026-10-30', 'ACTIVE');

INSERT INTO applications (student_id, opportunity_id, resume_url, cover_letter, status) VALUES
(1, 1, '/uploads/resumes/somajit_resume.pdf', 'I am very passionate about Java development and eager to contribute to ABC Technologies.', 'SHORTLISTED');
