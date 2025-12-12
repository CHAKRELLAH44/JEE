package com.myHR.api_sb.controler;



import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import com.myHR.api_sb.model.Employee;
import com.myHR.api_sb.service.EmployeeService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@EnableCircuitBreaker
@Configuration
@EnableHystrixDashboard

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // --- Exemple Hystrix Timeout ---
    @GetMapping("/myMessage")
    @HystrixCommand(
            fallbackMethod = "myHystrixFallbackMessage",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
            },
            threadPoolKey = "messageThreadPool"
    )
    public String getMessage() throws InterruptedException {
        System.out.println("Message from EmployeeController.getMessage(): Begin to sleep for 3 seconds...");
        Thread.sleep(3000); // Simule un long traitement
        return "Message from EmployeeController.getMessage(): End from sleep after 3 seconds!";
    }

    private String myHystrixFallbackMessage() {
        return "Message from myHystrixFallbackMessage(): Hystrix fallback executed after timeout (1s).";
    }

    // --- Endpoints CRUD ---
    @GetMapping("/employees")
    public Iterable<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployee(@PathVariable("id") Long id) {
        Optional<Employee> employee = employeeService.getEmployee(id);
        return employee.orElse(null);
    }

    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @PutMapping("/employee/{id}")
    public Employee updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        Optional<Employee> e = employeeService.getEmployee(id);
        if (e.isPresent()) {
            Employee current = e.get();
            if (employee.getFirstName() != null) current.setFirstName(employee.getFirstName());
            if (employee.getLastName() != null) current.setLastName(employee.getLastName());
            if (employee.getMail() != null) current.setMail(employee.getMail());
            if (employee.getPassword() != null) current.setPassword(employee.getPassword());
            return employeeService.saveEmployee(current);
        }
        return null;
    }

    @DeleteMapping("/employee/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
    }
}
