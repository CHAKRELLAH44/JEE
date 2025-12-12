package com.example.rest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.rest.model.Product;
import com.example.rest.service.IProductService;

@RestController
public class ProductController {

    @Autowired
    private IProductService service;

    @GetMapping("/products")
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/products/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/products")
    public ResponseEntity<Object> create(@Validated @RequestBody Product product) {
        service.create(product);
        return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Product product) {
        Product existing = service.getById(id);
        if (existing == null)
            return ResponseEntity.notFound().build();

        service.update(id, product);
        return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Product existing = service.getById(id);
        if (existing == null)
            return ResponseEntity.notFound().build();

        service.delete(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}
