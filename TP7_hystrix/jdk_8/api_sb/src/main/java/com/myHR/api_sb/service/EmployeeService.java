package com.myHR.api_sb.service;


import com.myHR.api_sb.model.Employee;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmployeeService {

    private static final Map<Long, Employee> employeeMap = new HashMap<>();

    static {
        employeeMap.put(1L, new Employee(1L, "Oussama", "Chakrellah", "oussama@mail.com", "1234"));
        employeeMap.put(2L, new Employee(2L, "Sara", "Benali", "sara@mail.com", "abcd"));
        employeeMap.put(3L, new Employee(3L, "Youssef", "Mansouri", "youssef@mail.com", "xyz"));
    }

    public Iterable<Employee> getEmployees() {
        return employeeMap.values();
    }

    public Optional<Employee> getEmployee(Long id) {
        return Optional.ofNullable(employeeMap.get(id));
    }

    public Employee saveEmployee(Employee employee) {
        employeeMap.put(employee.getId(), employee);
        return employee;
    }

    public void deleteEmployee(Long id) {
        employeeMap.remove(id);
    }
}

