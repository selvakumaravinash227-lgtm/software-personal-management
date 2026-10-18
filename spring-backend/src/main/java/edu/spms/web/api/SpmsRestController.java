package edu.spms.web.api;

import edu.spms.web.security.SpmsPrincipal;
import edu.spms.web.security.TokenStore;
import edu.spms.web.service.SpmsApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api")
public class SpmsRestController {
    private final SpmsApplicationService service; private final TokenStore tokens;
    public SpmsRestController(SpmsApplicationService service,TokenStore tokens){this.service=service;this.tokens=tokens;}
    @GetMapping("/dashboard") public Map<String,Object> dashboard(Authentication a){return service.dashboard(actor(a));}
    @GetMapping("/{view}") public List<Map<String,Object>> list(@PathVariable String view,@RequestParam(defaultValue="")String search,Authentication a){return service.list(view,search,actor(a));}
    @PostMapping("/employees") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> addEmployee(@RequestBody Map<String,Object>b,Authentication a){String id=service.addEmployee(b,actor(a));return Map.of("message","Employee created.","employeeId",id);}
    @PutMapping("/employees/{id}") public Map<String,String> updateEmployee(@PathVariable String id,@RequestBody Map<String,Object>b,Authentication a){service.updateEmployee(id,b,actor(a));return ok("Employee updated.");}
    @DeleteMapping("/employees/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteEmployee(@PathVariable String id,Authentication a){service.deleteEmployee(id,actor(a));}
    @PostMapping("/attendance") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> attendance(@RequestBody Map<String,Object>b,Authentication a){service.markAttendance(b,actor(a));return ok("Attendance saved.");}
    @PostMapping("/leaves") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> applyLeave(@RequestBody Map<String,Object>b,Authentication a){service.applyLeave(b,actor(a));return ok("Leave application submitted.");}
    @PostMapping("/leaves/{id}/decision") public Map<String,String> decideLeave(@PathVariable long id,@RequestBody Map<String,Object>b,Authentication a){service.decideLeave(id,b,actor(a));return ok("Leave request updated.");}
    @PostMapping("/payroll") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> payroll(@RequestBody Map<String,Object>b,Authentication a){service.createPayroll(b,actor(a));return ok("Payroll record created.");}
    @PostMapping("/payroll/{id}/process") public Map<String,String> processPayroll(@PathVariable long id,Authentication a){service.processPayroll(id,actor(a));return ok("Payroll processed and ready for payment recording.");}
    @PostMapping("/payroll/{id}/paid") public Map<String,String> markPaid(@PathVariable long id,@RequestBody Map<String,Object>b,Authentication a){service.markPaid(id,b,actor(a));return ok("Salary payment recorded in the SPMS audit trail. No bank transfer was initiated by SPMS.");}
    @PostMapping("/performance") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> performance(@RequestBody Map<String,Object>b,Authentication a){service.addPerformance(b,actor(a));return ok("Evaluation saved.");}
    @PutMapping("/performance/{id}") public Map<String,String> updatePerformance(@PathVariable long id,@RequestBody Map<String,Object>b,Authentication a){service.updatePerformance(id,b,actor(a));return ok("Evaluation updated.");}
    @PostMapping("/departments") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> department(@RequestBody Map<String,Object>b,Authentication a){service.addDepartment(b,actor(a));return ok("Department created.");}
    @PutMapping("/departments/{id}") public Map<String,String> updateDepartment(@PathVariable int id,@RequestBody Map<String,Object>b,Authentication a){service.updateDepartment(id,b,actor(a));return ok("Department updated.");}
    @DeleteMapping("/departments/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteDepartment(@PathVariable int id,Authentication a){service.deleteDepartment(id,actor(a));}
    @PostMapping("/users") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> user(@RequestBody Map<String,Object>b,Authentication a){service.addUser(b,actor(a));return ok("User account created.");}
    @PostMapping("/users/provision-employees") public Map<String,Object> provisionEmployees(Authentication a){var accounts=service.provisionEmployeeAccounts(actor(a));return Map.of("accounts",accounts,"count",accounts.size());}
    @PostMapping("/users/reset-employee-passwords") public Map<String,Object> resetEmployeePasswords(Authentication a){var credentials=service.resetEmployeePasswords(actor(a));credentials.forEach(c->tokens.revokeUser(Integer.parseInt(c.remove("userId"))));return Map.of("accounts",credentials,"count",credentials.size());}
    @PostMapping("/users/{id}/reset-password") public Map<String,String> resetPassword(@PathVariable int id,Authentication a){Map<String,String> result=service.resetUserPassword(id,actor(a));tokens.revokeUser(id);return result;}
    @PutMapping("/users/{id}/role") public Map<String,String> role(@PathVariable int id,@RequestBody Map<String,Object>b,Authentication a){service.setRole(id,b,actor(a));tokens.revokeUser(id);return ok("Role updated.");}
    @PutMapping("/users/{id}/active") public Map<String,String> active(@PathVariable int id,@RequestBody Map<String,Object>b,Authentication a){service.setActive(id,b,actor(a));if(!Boolean.parseBoolean(String.valueOf(b.get("active"))))tokens.revokeUser(id);return ok("Account status updated.");}
    @PostMapping("/reports/{type}") public Map<String,Object> report(@PathVariable String type,@RequestBody(required=false)Map<String,Object>b,Authentication a){return service.report(type,b,actor(a));}
    private static SpmsPrincipal actor(Authentication a){return (SpmsPrincipal)a.getPrincipal();}
    private static Map<String,String> ok(String message){return Map.of("message",message);}
}
