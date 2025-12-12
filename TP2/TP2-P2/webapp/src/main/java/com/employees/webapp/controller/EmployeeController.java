package com.employees.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.employees.webapp.model.Employee;
import com.employees.webapp.service.EmployeeService;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    // ✅ Lister tous les employés
    @GetMapping("/")
    public String home(Model model) {
        Iterable<Employee> listEmployee = service.getEmployees();
        model.addAttribute("employees", listEmployee);
        return "home";
    }

    // ✅ Formulaire d'ajout
    @GetMapping("/addEmployee")
    public String addEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "addEmployee";
    }

    // ✅ Sauvegarder un employé
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee employee) {
        service.saveEmployee(employee);
        return "redirect:/";
    }

    // ✅ Supprimer un employé
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") final Long id) {
        service.deleteEmployee(id);
        return "redirect:/";
    }

    // ✅ Modifier un employé
    @GetMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable("id") final Long id, Model model) {
        Employee e = service.getEmployee(id);
        model.addAttribute("employee", e);
        return "updateEmployee";
    }
}

//Affiche la page d’accueil / avec la liste des employés envoyée au template HTML.