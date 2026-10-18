-- Repeatable, additive fictional baseline data for Software Personnel Management System.
-- This script does not delete or overwrite existing employees or operational records.
USE SPMS_DB;

INSERT IGNORE INTO departments(name,description) VALUES
 ('Engineering','Software engineering and delivery'),
 ('Quality Assurance','Product quality and validation'),
 ('Product Design','Product and user experience design'),
 ('People Operations','Employee support and HR'),
 ('Finance','Financial operations and payroll');

INSERT INTO employees(employee_id,name,email,phone,address,department_id,designation,joining_date,salary)
SELECT seed.employee_id,seed.name,seed.email,seed.phone,seed.address,seed.department_id,seed.designation,seed.joining_date,seed.salary
FROM (
 SELECT 'SP001' employee_id,'Aarav Mehta' name,'aarav.mehta@example.com' email,'+91 98765 12001' phone,'Bengaluru' address,(SELECT department_id FROM departments WHERE name='Engineering' LIMIT 1) department_id,'Software Engineer' designation,DATE '2022-04-11' joining_date,95000.00 salary UNION ALL
 SELECT 'SP002','Diya Nair','diya.nair@example.com','+91 98765 12002','Pune',(SELECT department_id FROM departments WHERE name='Quality Assurance' LIMIT 1),'QA Engineer',DATE '2023-01-16',72000.00 UNION ALL
 SELECT 'SP003','Kabir Shah','kabir.shah@example.com','+91 98765 12003','Mumbai',(SELECT department_id FROM departments WHERE name='Product Design' LIMIT 1),'Product Designer',DATE '2022-08-08',81000.00 UNION ALL
 SELECT 'SP004','Ananya Rao','ananya.rao@example.com','+91 98765 12004','Hyderabad',(SELECT department_id FROM departments WHERE name='Engineering' LIMIT 1),'Backend Engineer',DATE '2021-11-22',102000.00 UNION ALL
 SELECT 'SP005','Ishita Das','ishita.das@example.com','+91 98765 12005','Chennai',(SELECT department_id FROM departments WHERE name='People Operations' LIMIT 1),'HR Executive',DATE '2024-02-05',68000.00
) seed
WHERE NOT EXISTS(SELECT 1 FROM employees e WHERE e.employee_id=seed.employee_id OR e.email=seed.email);

-- Passwords are salted PBKDF2-HMAC-SHA256 hashes; plaintext is never stored.
INSERT IGNORE INTO users(username,password_hash,role,employee_id,must_change_password)
VALUES('admin','pbkdf2$a5At1d47w9+pIPj3AmTRGg==$oMF/qHKPWWK6Er09R/sqnAXUjk87rjMQU6CzppisoEY=','ADMIN',NULL,TRUE);
INSERT IGNORE INTO users(username,password_hash,role,employee_id,must_change_password)
VALUES('hr','pbkdf2$XK+l2pxBZkcHhN2yvXtvVQ==$BVwB1NYTiqK8byasxAy+bx3/MTGNAdnD3BTyto/u034=','HR_MANAGER',NULL,TRUE);
INSERT IGNORE INTO users(username,password_hash,role,employee_id,must_change_password)
SELECT 'employee','pbkdf2$XS6r5panUdu+th+NkBP7XA==$JkQuHVHZP3WGqZYS9RZX6dwjHSaKhQr91RLXkCn8d1Y=', 'EMPLOYEE',
       COALESCE((SELECT employee_id FROM employees WHERE employee_id='SP001' LIMIT 1),(SELECT employee_id FROM employees WHERE email='aarav.mehta@example.com' LIMIT 1)),TRUE
WHERE NOT EXISTS(SELECT 1 FROM users WHERE username='employee');

INSERT INTO attendance(employee_id,work_date,status,check_in)
SELECT e.employee_id,CURRENT_DATE,
       CASE WHEN e.email='ishita.das@example.com' THEN 'LEAVE' ELSE 'PRESENT' END,
       CASE WHEN e.email='ishita.das@example.com' THEN NULL ELSE '09:00:00' END
FROM employees e
WHERE e.email IN ('aarav.mehta@example.com','diya.nair@example.com','kabir.shah@example.com','ananya.rao@example.com','ishita.das@example.com')
  AND NOT EXISTS(SELECT 1 FROM attendance a WHERE a.employee_id=e.employee_id AND a.work_date=CURRENT_DATE);

INSERT INTO leave_requests(employee_id,start_date,end_date,leave_type,reason,status)
SELECT e.employee_id,DATE_ADD(CURRENT_DATE,INTERVAL 14 DAY),DATE_ADD(CURRENT_DATE,INTERVAL 15 DAY),'Annual','Family commitment','PENDING'
FROM employees e WHERE e.email='diya.nair@example.com'
  AND NOT EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id AND l.leave_type='Annual' AND l.start_date=DATE_ADD(CURRENT_DATE,INTERVAL 14 DAY));

INSERT INTO payroll(employee_id,pay_month,basic_salary,allowances,deductions,status)
SELECT e.employee_id,DATE_SUB(CURRENT_DATE,INTERVAL DAYOFMONTH(CURRENT_DATE)-1 DAY),e.salary,
       ROUND(e.salary*0.06,2),ROUND(e.salary*0.025,2),'PROCESSED'
FROM employees e
WHERE e.email IN ('aarav.mehta@example.com','diya.nair@example.com','kabir.shah@example.com')
  AND NOT EXISTS(SELECT 1 FROM payroll p WHERE p.employee_id=e.employee_id AND p.pay_month=DATE_SUB(CURRENT_DATE,INTERVAL DAYOFMONTH(CURRENT_DATE)-1 DAY));

INSERT INTO performance(employee_id,evaluation_period,rating,remarks,evaluator_id,evaluated_at)
SELECT e.employee_id,'2026 H1',seed.rating,seed.remarks,(SELECT user_id FROM users WHERE username='hr' LIMIT 1),seed.evaluated_at
FROM (
 SELECT 'aarav.mehta@example.com' email,4.5 rating,'Consistently delivers reliable, maintainable work and supports team reviews.' remarks,DATE_SUB(CURRENT_DATE,INTERVAL 14 DAY) evaluated_at UNION ALL
 SELECT 'diya.nair@example.com',4.2,'Strong attention to detail and clear communication of quality risks.',DATE_SUB(CURRENT_DATE,INTERVAL 12 DAY) UNION ALL
 SELECT 'kabir.shah@example.com',4.4,'Produces thoughtful design work grounded in user needs.',DATE_SUB(CURRENT_DATE,INTERVAL 10 DAY)
) seed JOIN employees e ON e.email=seed.email
WHERE NOT EXISTS(SELECT 1 FROM performance p WHERE p.employee_id=e.employee_id AND p.evaluation_period='2026 H1');
