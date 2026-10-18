-- Additive SPMS repair migration. Safe to run again; existing records are preserved.
USE SPMS_DB;

-- Require existing employee accounts to replace any shared/demo password once.
SET @spms_user_force_added = (
  SELECT COUNT(*) = 0 FROM information_schema.columns
  WHERE table_schema = DATABASE() AND table_name = 'users' AND column_name = 'must_change_password'
);
SET @spms_sql = IF(@spms_user_force_added,
  'ALTER TABLE users ADD COLUMN must_change_password BOOLEAN NOT NULL DEFAULT FALSE', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF(@spms_user_force_added,
  'UPDATE users SET must_change_password = TRUE WHERE role = ''EMPLOYEE''', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;

-- Demo administrator and HR accounts use published bootstrap credentials.
-- Require a password change before those accounts can access the application.
UPDATE users SET must_change_password = TRUE WHERE username IN ('admin', 'hr');

-- Preserve the legacy overall rating/remarks while adding structured score fields.
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='technical_skills_score'), 'ALTER TABLE performance ADD COLUMN technical_skills_score DECIMAL(3,1) NULL', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='communication_score'), 'ALTER TABLE performance ADD COLUMN communication_score DECIMAL(3,1) NULL', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='teamwork_score'), 'ALTER TABLE performance ADD COLUMN teamwork_score DECIMAL(3,1) NULL', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='productivity_score'), 'ALTER TABLE performance ADD COLUMN productivity_score DECIMAL(3,1) NULL', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='quality_score'), 'ALTER TABLE performance ADD COLUMN quality_score DECIMAL(3,1) NULL', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='improvement_suggestions'), 'ALTER TABLE performance ADD COLUMN improvement_suggestions VARCHAR(1000) NOT NULL DEFAULT ''''', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='future_goals'), 'ALTER TABLE performance ADD COLUMN future_goals VARCHAR(1000) NOT NULL DEFAULT ''''', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;
SET @spms_sql = IF((SELECT COUNT(*) = 0 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='performance' AND column_name='updated_at'), 'ALTER TABLE performance ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP', 'SELECT 1');
PREPARE spms_stmt FROM @spms_sql; EXECUTE spms_stmt; DEALLOCATE PREPARE spms_stmt;

-- Internal audit of HR-confirmed payments; this table does not initiate bank transfers.
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
