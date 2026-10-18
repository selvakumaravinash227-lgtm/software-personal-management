CREATE DATABASE IF NOT EXISTS SPMS_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE SPMS_DB;

CREATE TABLE IF NOT EXISTS departments (
 department_id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(80) NOT NULL UNIQUE,
 description VARCHAR(255) NOT NULL DEFAULT '',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS employees (
 employee_id VARCHAR(16) PRIMARY KEY,
 name VARCHAR(120) NOT NULL,
 email VARCHAR(160) NOT NULL UNIQUE,
 phone VARCHAR(24) NOT NULL,
 gender VARCHAR(24) NOT NULL DEFAULT 'Prefer not to say',
 date_of_birth DATE NULL,
 address VARCHAR(255) NOT NULL DEFAULT '',
 department_id INT NULL,
 designation VARCHAR(100) NOT NULL,
 joining_date DATE NOT NULL,
 salary DECIMAL(12,2) NOT NULL CHECK (salary >= 0),
 employment_status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
 CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS users (
 user_id INT AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(60) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 role ENUM('ADMIN','HR_MANAGER','EMPLOYEE') NOT NULL,
 employee_id VARCHAR(16) NULL UNIQUE,
 active BOOLEAN NOT NULL DEFAULT TRUE,
 must_change_password BOOLEAN NOT NULL DEFAULT FALSE,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS attendance (
 attendance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 employee_id VARCHAR(16) NOT NULL,
 work_date DATE NOT NULL,
 status ENUM('PRESENT','ABSENT','LEAVE') NOT NULL,
 check_in TIME NULL,
 check_out TIME NULL,
 notes VARCHAR(255) NOT NULL DEFAULT '',
 UNIQUE KEY uq_attendance_employee_date(employee_id,work_date),
 CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS leave_requests (
 leave_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 employee_id VARCHAR(16) NOT NULL,
 start_date DATE NOT NULL,
 end_date DATE NOT NULL,
 leave_type VARCHAR(40) NOT NULL,
 reason VARCHAR(500) NOT NULL,
 status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
 requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 reviewed_by INT NULL,
 reviewed_at TIMESTAMP NULL,
 decision_note VARCHAR(255) NOT NULL DEFAULT '',
 CHECK (end_date >= start_date),
 CONSTRAINT fk_leave_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
 CONSTRAINT fk_leave_reviewer FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS payroll (
 payroll_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 employee_id VARCHAR(16) NOT NULL,
 pay_month DATE NOT NULL,
 basic_salary DECIMAL(12,2) NOT NULL CHECK (basic_salary >= 0),
 allowances DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (allowances >= 0),
 deductions DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (deductions >= 0),
 net_salary DECIMAL(12,2) GENERATED ALWAYS AS (basic_salary + allowances - deductions) STORED,
 status ENUM('DRAFT','PROCESSED','PAID') NOT NULL DEFAULT 'DRAFT',
 pay_date DATE NULL,
 UNIQUE KEY uq_payroll_employee_month(employee_id,pay_month),
 CHECK (deductions <= basic_salary + allowances),
 CONSTRAINT fk_payroll_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS payroll_payments (
 payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 payroll_id BIGINT NOT NULL,
 initiated_by INT NOT NULL,
 confirmed_by INT NOT NULL,
 initiated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 confirmed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 amount DECIMAL(12,2) NOT NULL CHECK (amount >= 0),
 payment_reference VARCHAR(100) NOT NULL DEFAULT '',
 status ENUM('RECORDED','FAILED') NOT NULL DEFAULT 'RECORDED',
 failure_details VARCHAR(500) NOT NULL DEFAULT '',
 KEY ix_payment_payroll_status(payroll_id,status),
 KEY ix_payment_confirmed_at(confirmed_at),
 CONSTRAINT fk_payment_payroll FOREIGN KEY (payroll_id) REFERENCES payroll(payroll_id) ON DELETE RESTRICT,
 CONSTRAINT fk_payment_initiator FOREIGN KEY (initiated_by) REFERENCES users(user_id) ON DELETE RESTRICT,
 CONSTRAINT fk_payment_confirmer FOREIGN KEY (confirmed_by) REFERENCES users(user_id) ON DELETE RESTRICT
);
CREATE TABLE IF NOT EXISTS performance (
 performance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 employee_id VARCHAR(16) NOT NULL,
 evaluation_period VARCHAR(30) NOT NULL,
 rating DECIMAL(3,1) NOT NULL CHECK (rating BETWEEN 1 AND 5),
 remarks VARCHAR(1000) NOT NULL,
 technical_skills_score DECIMAL(3,1) NULL,
 communication_score DECIMAL(3,1) NULL,
 teamwork_score DECIMAL(3,1) NULL,
 productivity_score DECIMAL(3,1) NULL,
 quality_score DECIMAL(3,1) NULL,
 improvement_suggestions VARCHAR(1000) NOT NULL DEFAULT '',
 future_goals VARCHAR(1000) NOT NULL DEFAULT '',
 evaluator_id INT NULL,
 evaluated_at DATE NOT NULL,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 CONSTRAINT fk_performance_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
 CONSTRAINT fk_performance_evaluator FOREIGN KEY (evaluator_id) REFERENCES users(user_id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS reports (
 report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 report_type ENUM('EMPLOYEE','ATTENDANCE','LEAVE','PAYROLL','PERFORMANCE') NOT NULL,
 requested_by INT NOT NULL,
 generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 filter_summary VARCHAR(500) NOT NULL DEFAULT '',
 CONSTRAINT fk_report_user FOREIGN KEY (requested_by) REFERENCES users(user_id) ON DELETE RESTRICT
);
