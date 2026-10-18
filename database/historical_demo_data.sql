-- Additive, repeatable fictional history for Software Personnel Management System.
-- Run after schema.sql and sample_data.sql. This script never drops or deletes rows.
USE SPMS_DB;

-- Preserve the existing department records and add the six demonstration groups.
INSERT IGNORE INTO departments(name, description) VALUES
 ('Software Development','Application engineering and delivery'),
 ('Quality Assurance','Product quality and test engineering'),
 ('Human Resources','People operations and employee support'),
 ('Finance','Financial operations and payroll'),
 ('Information Technology','Infrastructure, systems and support'),
 ('Project Management','Planning, analysis and delivery leadership');

-- Insert only enough from this fictional pool to bring the employee count to 30.
-- Existing employee IDs and email addresses are never overwritten.
SET @spms_employee_need := GREATEST(0, 30 - (SELECT COUNT(*) FROM employees));
INSERT INTO employees(employee_id,name,email,phone,gender,date_of_birth,address,department_id,designation,joining_date,salary,employment_status)
SELECT pool.employee_id,pool.name,pool.email,pool.phone,pool.gender,pool.dob,pool.address,
       (SELECT department_id FROM departments WHERE name=pool.department LIMIT 1),
       pool.designation,pool.joined,pool.salary,'ACTIVE'
FROM (
 SELECT candidates.*,ROW_NUMBER() OVER(ORDER BY candidates.employee_id) AS seed_order
 FROM (
 SELECT 'EMP1001' employee_id,'Arjun Kumar' name,'arjun.kumar@spms.example' email,'+91 90000 10001' phone,'Male' gender,'1995-02-14' dob,'Bengaluru, Karnataka' address,'Software Development' department,'Software Developer' designation,'2022-04-11' joined,78000.00 salary UNION ALL
 SELECT 'EMP1002','Priya Sharma','priya.sharma@spms.example','+91 90000 10002','Female','1996-06-21','Pune, Maharashtra','Quality Assurance','QA Engineer','2023-01-16',68000.00 UNION ALL
 SELECT 'EMP1003','Rahul Verma','rahul.verma@spms.example','+91 90000 10003','Male','1993-10-09','Hyderabad, Telangana','Human Resources','HR Executive','2021-09-06',62000.00 UNION ALL
 SELECT 'EMP1004','Meera Iyer','meera.iyer@spms.example','+91 90000 10004','Female','1992-12-02','Chennai, Tamil Nadu','Finance','Financial Analyst','2020-07-20',82000.00 UNION ALL
 SELECT 'EMP1005','Vikram Singh','vikram.singh@spms.example','+91 90000 10005','Male','1990-04-18','Jaipur, Rajasthan','Information Technology','System Administrator','2019-03-11',91000.00 UNION ALL
 SELECT 'EMP1006','Ananya Rao','ananya.rao@spms.example','+91 90000 10006','Female','1994-08-11','Mysuru, Karnataka','Project Management','Project Coordinator','2022-11-07',72000.00 UNION ALL
 SELECT 'EMP1007','Rohan Patel','rohan.patel@spms.example','+91 90000 10007','Male','1998-01-27','Ahmedabad, Gujarat','Software Development','Junior Software Developer','2024-06-03',52000.00 UNION ALL
 SELECT 'EMP1008','Kavya Nair','kavya.nair@spms.example','+91 90000 10008','Female','1997-03-08','Kochi, Kerala','Quality Assurance','Test Automation Engineer','2023-08-14',74000.00 UNION ALL
 SELECT 'EMP1009','Aditya Joshi','aditya.joshi@spms.example','+91 90000 10009','Male','1989-11-15','Nagpur, Maharashtra','Human Resources','HR Manager','2018-05-21',108000.00 UNION ALL
 SELECT 'EMP1010','Nisha Kapoor','nisha.kapoor@spms.example','+91 90000 10010','Female','1995-05-30','Delhi, Delhi','Finance','Accountant','2021-02-01',66000.00 UNION ALL
 SELECT 'EMP1011','Siddharth Das','siddharth.das@spms.example','+91 90000 10011','Male','1991-07-19','Kolkata, West Bengal','Information Technology','Database Administrator','2020-10-12',96000.00 UNION ALL
 SELECT 'EMP1012','Ishita Bose','ishita.bose@spms.example','+91 90000 10012','Female','1996-09-04','Bhubaneswar, Odisha','Project Management','Business Analyst','2022-01-17',79000.00 UNION ALL
 SELECT 'EMP1013','Karan Malhotra','karan.malhotra@spms.example','+91 90000 10013','Male','1992-02-23','Chandigarh, Chandigarh','Software Development','Senior Software Developer','2019-08-05',118000.00 UNION ALL
 SELECT 'EMP1014','Sneha Kulkarni','sneha.kulkarni@spms.example','+91 90000 10014','Female','1994-11-12','Nashik, Maharashtra','Quality Assurance','QA Lead','2020-04-13',99000.00 UNION ALL
 SELECT 'EMP1015','Manish Reddy','manish.reddy@spms.example','+91 90000 10015','Male','1997-12-01','Vijayawada, Andhra Pradesh','Human Resources','HR Executive','2023-04-10',59000.00 UNION ALL
 SELECT 'EMP1016','Pooja Menon','pooja.menon@spms.example','+91 90000 10016','Female','1993-06-06','Thiruvananthapuram, Kerala','Finance','Financial Analyst','2020-12-07',85000.00 UNION ALL
 SELECT 'EMP1017','Dev Gupta','dev.gupta@spms.example','+91 90000 10017','Male','1990-09-25','Noida, Uttar Pradesh','Information Technology','IT Support Engineer','2018-09-24',73000.00 UNION ALL
 SELECT 'EMP1018','Ayesha Khan','ayesha.khan@spms.example','+91 90000 10018','Female','1995-01-16','Lucknow, Uttar Pradesh','Project Management','Project Manager','2021-06-14',122000.00 UNION ALL
 SELECT 'EMP1019','Nikhil Bhat','nikhil.bhat@spms.example','+91 90000 10019','Male','1998-04-07','Mangaluru, Karnataka','Software Development','Software Developer','2024-01-08',61000.00 UNION ALL
 SELECT 'EMP1020','Ritika Sen','ritika.sen@spms.example','+91 90000 10020','Female','1996-07-29','Guwahati, Assam','Quality Assurance','Test Automation Engineer','2022-09-19',76000.00 UNION ALL
 SELECT 'EMP1021','Sanjay Pillai','sanjay.pillai@spms.example','+91 90000 10021','Male','1988-12-11','Coimbatore, Tamil Nadu','Human Resources','HR Manager','2017-03-06',112000.00 UNION ALL
 SELECT 'EMP1022','Tara Chawla','tara.chawla@spms.example','+91 90000 10022','Female','1994-03-19','Amritsar, Punjab','Finance','Accountant','2020-02-10',70000.00 UNION ALL
 SELECT 'EMP1023','Farhan Ali','farhan.ali@spms.example','+91 90000 10023','Male','1991-08-03','Indore, Madhya Pradesh','Information Technology','System Administrator','2019-11-18',94000.00 UNION ALL
 SELECT 'EMP1024','Neha Sethi','neha.sethi@spms.example','+91 90000 10024','Female','1993-10-26','Gurugram, Haryana','Project Management','Business Analyst','2019-05-27',88000.00 UNION ALL
 SELECT 'EMP1025','Arvind Rao','arvind.rao@spms.example','+91 90000 10025','Male','1987-05-14','Visakhapatnam, Andhra Pradesh','Software Development','Technical Lead','2016-07-04',148000.00 UNION ALL
 SELECT 'EMP1026','Leena Thomas','leena.thomas@spms.example','+91 90000 10026','Female','1997-02-09','Kollam, Kerala','Quality Assurance','QA Engineer','2023-02-20',65000.00 UNION ALL
 SELECT 'EMP1027','Yash Thakur','yash.thakur@spms.example','+91 90000 10027','Male','1999-06-17','Bhopal, Madhya Pradesh','Human Resources','HR Executive','2024-03-18',54000.00 UNION ALL
 SELECT 'EMP1028','Simran Kaur','simran.kaur@spms.example','+91 90000 10028','Female','1992-04-01','Ludhiana, Punjab','Finance','Financial Analyst','2021-09-13',84000.00 UNION ALL
 SELECT 'EMP1029','Joseph Mathew','joseph.mathew@spms.example','+91 90000 10029','Male','1990-01-31','Thrissur, Kerala','Information Technology','Database Administrator','2018-01-15',101000.00 UNION ALL
 SELECT 'EMP1030','Maya Banerjee','maya.banerjee@spms.example','+91 90000 10030','Female','1995-12-20','Siliguri, West Bengal','Project Management','Project Coordinator','2022-05-09',75000.00
 ) candidates
 WHERE NOT EXISTS (SELECT 1 FROM employees e WHERE e.employee_id=candidates.employee_id OR e.email=candidates.email)
) pool
WHERE @spms_employee_need > 0 AND pool.seed_order<=@spms_employee_need;

-- Historical leave requests, with review metadata matching the current schema.
INSERT INTO leave_requests(employee_id,start_date,end_date,leave_type,reason,status,requested_at,reviewed_by,reviewed_at,decision_note)
SELECT e.employee_id,seed.start_date,seed.end_date,seed.leave_type,seed.reason,seed.status,
       TIMESTAMP(DATE_SUB(seed.start_date,INTERVAL 10 DAY),'10:00:00'),
       CASE WHEN seed.status='PENDING' THEN NULL ELSE (SELECT user_id FROM users WHERE username='hr' LIMIT 1) END,
       CASE WHEN seed.status='PENDING' THEN NULL ELSE TIMESTAMP(DATE_SUB(seed.start_date,INTERVAL 7 DAY),'15:00:00') END,
       CASE seed.status WHEN 'APPROVED' THEN 'Approved for the requested period.' WHEN 'REJECTED' THEN 'Please coordinate coverage and submit revised dates.' ELSE '' END
FROM (
 SELECT 'EMP1001' employee_id,DATE_SUB(CURRENT_DATE,INTERVAL 72 DAY) start_date,DATE_SUB(CURRENT_DATE,INTERVAL 71 DAY) end_date,'Casual' leave_type,'Personal commitment' reason,'APPROVED' status UNION ALL
 SELECT 'EMP1004',DATE_SUB(CURRENT_DATE,INTERVAL 58 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 56 DAY),'Sick','Recovery and follow-up appointment','APPROVED' UNION ALL
 SELECT 'EMP1007',DATE_SUB(CURRENT_DATE,INTERVAL 43 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 42 DAY),'Annual','Family travel','REJECTED' UNION ALL
 SELECT 'EMP1011',DATE_SUB(CURRENT_DATE,INTERVAL 36 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 35 DAY),'Casual','Personal appointment','APPROVED' UNION ALL
 SELECT 'EMP1015',DATE_SUB(CURRENT_DATE,INTERVAL 24 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 23 DAY),'Sick','Seasonal illness','APPROVED' UNION ALL
 SELECT 'EMP1018',DATE_SUB(CURRENT_DATE,INTERVAL 17 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 16 DAY),'Annual','Family visit','REJECTED' UNION ALL
 SELECT 'EMP1022',DATE_SUB(CURRENT_DATE,INTERVAL 9 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 8 DAY),'Emergency','Urgent family matter','APPROVED' UNION ALL
 SELECT 'EMP1024',DATE_ADD(CURRENT_DATE,INTERVAL 3 DAY),DATE_ADD(CURRENT_DATE,INTERVAL 4 DAY),'Casual','Personal commitment','PENDING' UNION ALL
 SELECT 'EMP1026',DATE_ADD(CURRENT_DATE,INTERVAL 3 DAY),DATE_ADD(CURRENT_DATE,INTERVAL 4 DAY),'Casual','Personal commitment','PENDING' UNION ALL
 SELECT 'EMP1030',DATE_ADD(CURRENT_DATE,INTERVAL 12 DAY),DATE_ADD(CURRENT_DATE,INTERVAL 14 DAY),'Annual','Planned family trip','PENDING' UNION ALL
 SELECT 'SP002',DATE_SUB(CURRENT_DATE,INTERVAL 30 DAY),DATE_SUB(CURRENT_DATE,INTERVAL 29 DAY),'Annual','Family commitment','APPROVED'
) seed JOIN employees e ON e.employee_id=seed.employee_id
WHERE seed.start_date>=e.joining_date
  AND (seed.status='PENDING' OR EXISTS(SELECT 1 FROM users WHERE username='hr'))
  AND NOT EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id AND l.start_date=seed.start_date AND l.end_date=seed.end_date AND l.leave_type=seed.leave_type);

-- The baseline attendance sample marks Ishita Das as on leave today; keep that
-- attendance state aligned with an approved leave request in this schema.
INSERT INTO leave_requests(employee_id,start_date,end_date,leave_type,reason,status,requested_at,reviewed_by,reviewed_at,decision_note)
SELECT e.employee_id,CURRENT_DATE,CURRENT_DATE,'Sick','Recovery day', 'APPROVED',
       TIMESTAMP(DATE_SUB(CURRENT_DATE,INTERVAL 2 DAY),'10:00:00'),
       (SELECT user_id FROM users WHERE username='hr' LIMIT 1),
       TIMESTAMP(DATE_SUB(CURRENT_DATE,INTERVAL 1 DAY),'15:00:00'),
       'Approved for recovery.'
FROM employees e
WHERE e.email='ishita.das@example.com' AND e.joining_date<=CURRENT_DATE
  AND EXISTS(SELECT 1 FROM users WHERE username='hr')
  AND NOT EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id AND l.start_date=CURRENT_DATE AND l.end_date=CURRENT_DATE AND l.leave_type='Sick');

-- Reconcile pre-existing LEAVE attendance rows from the original database too.
INSERT INTO leave_requests(employee_id,start_date,end_date,leave_type,reason,status,requested_at,reviewed_by,reviewed_at,decision_note)
SELECT a.employee_id,a.work_date,a.work_date,'Casual','Approved leave aligned with attendance history','APPROVED',
       TIMESTAMP(GREATEST(DATE_SUB(a.work_date,INTERVAL 2 DAY),e.joining_date),'10:00:00'),
       (SELECT user_id FROM users WHERE username='hr' LIMIT 1),
       TIMESTAMP(GREATEST(DATE_SUB(a.work_date,INTERVAL 1 DAY),e.joining_date),'15:00:00'),
       'Approved to match the stored attendance status.'
FROM attendance a JOIN employees e ON e.employee_id=a.employee_id
WHERE a.status='LEAVE' AND a.work_date>=e.joining_date
  AND EXISTS(SELECT 1 FROM users WHERE username='hr')
  AND NOT EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=a.employee_id AND l.status='APPROVED' AND a.work_date BETWEEN l.start_date AND l.end_date);

-- Ensure every employee has at least one historical request without replacing any existing leave history.
INSERT INTO leave_requests(employee_id,start_date,end_date,leave_type,reason,status,requested_at,reviewed_by,reviewed_at,decision_note)
SELECT e.employee_id,
       DATE_SUB(CURRENT_DATE,INTERVAL (28+MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),25)) DAY),
       DATE_SUB(CURRENT_DATE,INTERVAL (27+MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),25)) DAY),
       CASE MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),4) WHEN 0 THEN 'Sick' WHEN 1 THEN 'Casual' WHEN 2 THEN 'Annual' ELSE 'Emergency' END,
       CASE MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),3) WHEN 0 THEN 'Planned recovery and personal time' WHEN 1 THEN 'Personal family commitment' ELSE 'Short personal appointment' END,
       CASE MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),3) WHEN 0 THEN 'REJECTED' ELSE 'APPROVED' END,
       TIMESTAMP(DATE_SUB(CURRENT_DATE,INTERVAL (38+MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),25)) DAY),'10:00:00'),
       (SELECT user_id FROM users WHERE username='hr' LIMIT 1),
       TIMESTAMP(DATE_SUB(CURRENT_DATE,INTERVAL (31+MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),25)) DAY),'15:00:00'),
       CASE MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),3) WHEN 0 THEN 'Please coordinate coverage and submit revised dates.' ELSE 'Approved for the requested period.' END
FROM employees e
WHERE EXISTS(SELECT 1 FROM users WHERE username='hr')
  AND NOT EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id)
  AND DATE_SUB(CURRENT_DATE,INTERVAL (28+MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),25)) DAY)>=e.joining_date;

-- Three completed monthly payroll periods; generated net_salary remains database-calculated.
INSERT INTO payroll(employee_id,pay_month,basic_salary,allowances,deductions,status,pay_date)
WITH RECURSIVE periods(n,pay_month) AS (
 SELECT 1, DATE_SUB(DATE_SUB(CURRENT_DATE,INTERVAL DAYOFMONTH(CURRENT_DATE)-1 DAY),INTERVAL 1 MONTH)
 UNION ALL SELECT n+1,DATE_SUB(pay_month,INTERVAL 1 MONTH) FROM periods WHERE n<3
)
SELECT e.employee_id,p.pay_month,e.salary,
       ROUND(e.salary*CASE MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),4) WHEN 0 THEN 0.06 WHEN 1 THEN 0.08 WHEN 2 THEN 0.10 ELSE 0.07 END,2),
       ROUND(e.salary*CASE MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),5) WHEN 0 THEN 0.025 WHEN 1 THEN 0.03 WHEN 2 THEN 0.035 WHEN 3 THEN 0.02 ELSE 0.04 END,2),
       'PROCESSED',LAST_DAY(p.pay_month)
FROM employees e CROSS JOIN periods p
WHERE e.joining_date<=LAST_DAY(p.pay_month)
  AND NOT EXISTS(SELECT 1 FROM payroll existing WHERE existing.employee_id=e.employee_id AND existing.pay_month=p.pay_month);

-- Historical weekday attendance for the previous 90 days, starting no earlier than hire.
-- Existing attendance, including the original five demo rows, is preserved.
INSERT INTO attendance(employee_id,work_date,status,check_in,check_out,notes)
WITH RECURSIVE calendar(work_date) AS (
 SELECT DATE_SUB(CURRENT_DATE,INTERVAL 90 DAY)
 UNION ALL SELECT DATE_ADD(work_date,INTERVAL 1 DAY) FROM calendar WHERE work_date<DATE_SUB(CURRENT_DATE,INTERVAL 1 DAY)
)
SELECT e.employee_id,c.work_date,
       CASE WHEN EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id AND l.status='APPROVED' AND c.work_date BETWEEN l.start_date AND l.end_date)
            THEN 'LEAVE' WHEN MOD(DAYOFYEAR(c.work_date)+CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),23)=0 THEN 'ABSENT' ELSE 'PRESENT' END,
       CASE WHEN EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id AND l.status='APPROVED' AND c.work_date BETWEEN l.start_date AND l.end_date) THEN NULL
            WHEN MOD(DAYOFYEAR(c.work_date)+CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),23)=0 THEN NULL ELSE MAKETIME(8+MOD(CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),2),45+MOD(DAYOFYEAR(c.work_date),20),0) END,
       CASE WHEN EXISTS(SELECT 1 FROM leave_requests l WHERE l.employee_id=e.employee_id AND l.status='APPROVED' AND c.work_date BETWEEN l.start_date AND l.end_date) THEN NULL
            WHEN MOD(DAYOFYEAR(c.work_date)+CAST(SUBSTRING(e.employee_id,4) AS UNSIGNED),23)=0 THEN NULL ELSE '17:30:00' END,
       'Fictional demonstration record'
FROM employees e CROSS JOIN calendar c
WHERE WEEKDAY(c.work_date)<5 AND c.work_date>=e.joining_date
  AND NOT EXISTS(SELECT 1 FROM attendance a WHERE a.employee_id=e.employee_id AND a.work_date=c.work_date);

-- Two varied evaluations per employee. Strengths and development areas are kept
-- in remarks because the approved schema has a single remarks field.
INSERT INTO performance(employee_id,evaluation_period,rating,remarks,evaluator_id,evaluated_at)
SELECT e.employee_id,s.period,s.rating,s.remarks,(SELECT user_id FROM users WHERE username='hr' LIMIT 1),s.evaluated_at
FROM employees e
JOIN (
 SELECT '2026 Q1' period,CAST(3.2 AS DECIMAL(3,1)) rating,'Strengths: dependable delivery and collaboration. Development: broaden technical documentation.' remarks,DATE '2026-03-31' evaluated_at UNION ALL
 SELECT '2026 Q2',CAST(4.1 AS DECIMAL(3,1)),'Strengths: clear ownership and consistent quality. Development: increase cross-team knowledge sharing.',DATE '2026-06-30' UNION ALL
 SELECT '2026 Q3',CAST(3.7 AS DECIMAL(3,1)),'Strengths: responsive execution and peer support. Development: improve estimation accuracy.',DATE '2026-09-30'
) s ON s.evaluated_at>=e.joining_date
WHERE EXISTS(SELECT 1 FROM users WHERE username='hr')
  AND NOT EXISTS(SELECT 1 FROM performance p WHERE p.employee_id=e.employee_id AND p.evaluation_period=s.period);

-- Historical report audit entries (the report endpoints calculate rows live).
INSERT INTO reports(report_type,requested_by,filter_summary)
SELECT seed.report_type,u.user_id,seed.filter_summary
FROM (SELECT 'EMPLOYEE' report_type,'Historical employee directory' filter_summary UNION ALL
      SELECT 'ATTENDANCE','Previous 90 days' UNION ALL
      SELECT 'LEAVE','Historical leave decisions' UNION ALL
      SELECT 'PAYROLL','Previous three completed monthly periods' UNION ALL
      SELECT 'PERFORMANCE','2026 quarterly evaluations') seed
JOIN users u ON u.username='admin'
WHERE NOT EXISTS(SELECT 1 FROM reports r WHERE r.report_type=seed.report_type AND r.filter_summary=seed.filter_summary);
