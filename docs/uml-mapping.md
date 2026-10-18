# OOAD diagram-to-code mapping

The original project presentation was not included with the inspected files. This mapping follows the SPMS requirements and should be reconciled with the approved diagrams when the presentation is available.

| Diagram | SPMS mapping |
|---|---|
| Use Case | Admin manages accounts/departments and all modules; HR Manager manages workforce records; Employee accesses own activity and submits leave. |
| Class | Abstract `User` with role subclasses; `Department`, `EmployeeRecord`, attendance, leave, payroll, performance; `ModuleController`, `SpmsService`, `ModuleDAO`, `DatabaseConnection`. |
| Activity | Authenticate, maintain employee records, mark attendance, apply/review leave, process payroll, evaluate performance, generate a report. |
| Sequence | React view → HTTP API → Spring REST controller → module controller/service authorization and validation → DAO → JDBC → MySQL. |
| Collaboration | Employee applies for leave; authenticated session supplies employee scope; service validates dates; DAO persists; HR/Admin records approval or rejection; employee views the resulting status. |
| Deployment | React/Vite browser client connects to Java 17 Spring Boot REST service; service uses JDBC to connect to MySQL `SPMS_DB`. |
| State Chart | Leave: Pending → Approved or Rejected. Payroll: Draft → Processed → Paid. |
| Component | React/Vite frontend, Spring API, controller/service, DAO/JDBC, MySQL schema. The preserved Swing implementation remains a separate legacy frontend. |

Encapsulation appears in domain fields, abstraction/inheritance in the user hierarchy, polymorphism in role access, associations in model relationships and foreign keys, and cohesion/coupling in the frontend/API/service/DAO boundaries.
