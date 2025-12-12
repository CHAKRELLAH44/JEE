package com.myHR.api_sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.myHR.api_sb.model.Employee;
import com.myHR.api_sb.service.EmployeeService;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 🔹 Lister tous les employés
    @GetMapping
    public Iterable<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    // 🔹 Récupérer un employé par son ID
    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployee(id);
    }

    // 🔹 Créer un nouvel employé
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    // 🔹 Mettre à jour un employé existant
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return employeeService.saveEmployee(employee);
    }

    // 🔹 Supprimer un employé
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
