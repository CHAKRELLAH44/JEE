package com.example.rest.service;

import java.util.List;
import com.example.rest.model.Product;

public interface IProductService {
    Product getById(Long id);
    List<Product> getAll();
    void create(Product product);
    void update(Long id, Product product);
    void delete(Long id);
}
