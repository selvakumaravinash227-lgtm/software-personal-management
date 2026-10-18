package spms.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spms.controller.ModuleController;
import spms.dao.DataAccessException;
import spms.model.User;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class SpmsApiController {
    private static final Logger LOG=Logger.getLogger(SpmsApiController.class.getName());
    private ModuleController service(String auth){return new ModuleController(AuthApiController.actor(auth));}
    private static String s(Map<String,Object> body,String key){return AuthApiController.text(body,key);}

    @GetMapping("/dashboard") public Map<String,String> dashboard(@RequestHeader("Authorization") String auth){return service(auth).dashboard();}
    @GetMapping("/{view}") public List<Map<String,Object>> list(@PathVariable String view,@RequestParam(defaultValue="") String search,@RequestHeader("Authorization") String auth){return service(auth).load(view,search);}

    @PostMapping("/employees") @ResponseStatus(HttpStatus.CREATED)
    public Map<String,String> addEmployee(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).addEmployee(s(b,"id"),s(b,"name"),s(b,"email"),s(b,"phone"),s(b,"department"),s(b,"designation"),s(b,"joiningDate"),s(b,"salary"));return ok("Employee created.");}
    @PutMapping("/employees/{id}") public Map<String,String> updateEmployee(@PathVariable String id,@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).updateEmployee(id,s(b,"name"),s(b,"email"),s(b,"phone"),s(b,"department"),s(b,"designation"),s(b,"salary"));return ok("Employee updated.");}
    @DeleteMapping("/employees/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteEmployee(@PathVariable String id,@RequestHeader("Authorization")String a){service(a).deleteEmployee(id);}

    @PostMapping("/attendance") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> attendance(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).attendance(s(b,"employeeId"),s(b,"workDate"),s(b,"status"));return ok("Attendance saved.");}
    @PostMapping("/leaves") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> leave(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).applyLeave(s(b,"employeeId"),s(b,"from"),s(b,"to"),s(b,"type"),s(b,"reason"));return ok("Leave application submitted.");}
    @PostMapping("/leaves/{id}/decision") public Map<String,String> decide(@PathVariable String id,@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).decideLeave(id,Boolean.parseBoolean(s(b,"approve")),s(b,"note"));return ok("Leave request updated.");}
    @PostMapping("/payroll") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> payroll(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).payroll(s(b,"employeeId"),s(b,"month"),s(b,"allowances"),s(b,"deductions"));return ok("Payroll record created.");}
    @PostMapping("/payroll/{id}/paid") public Map<String,String> paid(@PathVariable String id,@RequestHeader("Authorization")String a){service(a).markPaid(id);return ok("Payroll marked as paid.");}
    @PostMapping("/performance") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> evaluation(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).addPerformance(s(b,"employeeId"),s(b,"period"),s(b,"rating"),s(b,"remarks"));return ok("Evaluation saved.");}
    @PutMapping("/performance/{id}") public Map<String,String> evaluationUpdate(@PathVariable String id,@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).updatePerformance(id,s(b,"period"),s(b,"rating"),s(b,"remarks"));return ok("Evaluation updated.");}
    @PostMapping("/departments") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> department(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).department(s(b,"name"),s(b,"description"));return ok("Department created.");}
    @PutMapping("/departments/{id}") public Map<String,String> departmentUpdate(@PathVariable String id,@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).updateDepartment(id,s(b,"name"),s(b,"description"));return ok("Department updated.");}
    @DeleteMapping("/departments/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void departmentDelete(@PathVariable String id,@RequestHeader("Authorization")String a){service(a).deleteDepartment(id);}
    @PostMapping("/users") @ResponseStatus(HttpStatus.CREATED) public Map<String,String> user(@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).addUser(s(b,"username"),s(b,"password").toCharArray(),s(b,"role"),s(b,"employeeId"));return ok("User account created.");}
    @PutMapping("/users/{id}/role") public Map<String,String> role(@PathVariable String id,@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).changeRole(id,s(b,"role"));AuthApiController.revoke(Integer.parseInt(id));return ok("Role updated.");}
    @PutMapping("/users/{id}/active") public Map<String,String> active(@PathVariable String id,@RequestBody Map<String,Object>b,@RequestHeader("Authorization")String a){service(a).toggleUser(id,Boolean.parseBoolean(s(b,"active")));if(!Boolean.parseBoolean(s(b,"active")))AuthApiController.revoke(Integer.parseInt(id));return ok("Account status updated.");}
    @PostMapping("/reports/{type}") public Map<String,Object> report(@PathVariable String type,@RequestBody(required=false)Map<String,Object>b,@RequestHeader("Authorization")String a){ModuleController c=service(a);String view=type.equals("leaves")?"leaves":type;if(!Set.of("employees","attendance","leaves","payroll","performance").contains(view))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Select a supported report type.");String filter=b==null?"":s(b,"filters");List<Map<String,Object>> rows=c.load(view,filter);c.recordReport(view,filter);return Map.of("type",view,"count",rows.size(),"rows",rows);}

    @ExceptionHandler(SecurityException.class) @ResponseStatus(HttpStatus.FORBIDDEN) public Map<String,String> forbidden(SecurityException e){return Map.of("message",e.getMessage());}
    @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST) public Map<String,String> invalid(IllegalArgumentException e){return Map.of("message",e.getMessage());}
    @ExceptionHandler(DataAccessException.class) @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) public Map<String,String> database(DataAccessException e){LOG.log(Level.SEVERE,"SPMS request failed",e);return Map.of("message",e.getMessage());}
    private static Map<String,String> ok(String message){return Map.of("message",message);}
}
