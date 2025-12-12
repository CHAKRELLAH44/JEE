package com.employees.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.webapp.model.Employee;
import com.employees.webapp.repository.EmployeeProxy;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeProxy proxy;

    // 🔹 Récupère tous les employés
    public Iterable<Employee> getEmployees() {
        return proxy.getEmployees();
    }

    // 🔹 Récupère un employé par son ID
    public Employee getEmployee(Long id) {
        return proxy.getEmployee(id);
    }

    // 🔹 Crée ou met à jour un employé
    public Employee saveEmployee(Employee employee) {
        return proxy.saveEmployee(employee);
    }

    // 🔹 Supprime un employé par ID
    public void deleteEmployee(Long id) {
        proxy.deleteEmployee(id);
    }
}

//Gère la logique métier côté client.
//
//Sert d’intermédiaire entre le contrôleur et la couche “proxy”