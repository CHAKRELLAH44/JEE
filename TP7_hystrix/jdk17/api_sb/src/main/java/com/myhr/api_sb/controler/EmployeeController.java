package com.myhr.api_sb.controler;



import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.myhr.api_sb.service.EmployeeService;
import com.myhr.api_sb.model.Employee;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // --- HYSTRIX TEST METHOD ---
    @GetMapping("/myMessage")
    @HystrixCommand(fallbackMethod = "myHistrixbuildFallbackMessage",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
            },
            threadPoolKey = "messageThreadPool")
    public String getMessage() throws InterruptedException {
        System.out.println("Message from EmployeeController.getMessage(): Begin To sleep for 3 scondes");
        // We simulate a delay of 3 seconds, but the timeout is set to 1 second (1000ms)
        Thread.sleep(3000);
        return "Message from EmployeeController.getMessage(): End from sleep for 3 scondes";
    }

    // Fallback method called when the timeout is reached
    private String myHistrixbuildFallbackMessage() {
        return "Message from myHistrixbuildFallbackMessage() Hystrix Fallback message (after timeout: 1 second)";
    }
    // ---------------------------

    // Standard CRUD methods (from TP context)
    @GetMapping("/employees")
    public Iterable<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployee(@PathVariable("id") final Long id) {
        java.util.Optional<Employee> employee = employeeService.getEmployee(id);
        return employee.orElse(null);
    }

    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }
}