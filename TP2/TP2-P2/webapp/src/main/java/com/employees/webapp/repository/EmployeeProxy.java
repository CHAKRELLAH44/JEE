package com.employees.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.employees.webapp.model.Employee;

import java.util.List;

@Component
public class EmployeeProxy {

    @Autowired
    private CustomProperties props;

    // 🔹 Récupérer tous les employés
    public Iterable<Employee> getEmployees() {
        String baseApiUrl = props.getApiUrl();
        String getEmployeesUrl = baseApiUrl + "/employees";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Employee>> response = restTemplate.exchange(
                getEmployeesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Employee>>() {}
        );
        return response.getBody();
    }

    // 🔹 Récupérer un employé par ID
    public Employee getEmployee(Long id) {
        String getUrl = props.getApiUrl() + "/employees/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Employee> response = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                null,
                Employee.class
        );
        return response.getBody();
    }

    // 🔹 Créer ou mettre à jour un employé
    public Employee saveEmployee(Employee employee) {
        String postUrl = props.getApiUrl() + "/employees";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Employee> request = new HttpEntity<>(employee);
        ResponseEntity<Employee> response = restTemplate.exchange(
                postUrl,
                HttpMethod.POST,
                request,
                Employee.class
        );
        return response.getBody();
    }

    // 🔹 Supprimer un employé
    public void deleteEmployee(Long id) {
        String deleteUrl = props.getApiUrl() + "/employees/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class);
    }
}

//Utilise RestTemplate pour faire un appel HTTP vers http://localhost:9000/employees