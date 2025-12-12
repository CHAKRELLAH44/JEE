package com.example.rest.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.rest.model.Product;

@Service
public class ProductServiceImpl implements IProductService {

    private static List<Product> productRepo = new ArrayList<>();

    static {
        productRepo.add(new Product(1L, "PC PORTABLE HP"));
        productRepo.add(new Product(2L, "TV LG 32p"));
        productRepo.add(new Product(3L, "Camera Sony"));
    }

    @Override
    public Product getById(Long id) {
        return productRepo.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> getAll() {
        return productRepo;
    }

    @Override
    public void create(Product product) {
        productRepo.add(product);
    }

    @Override
    public void update(Long id, Product product) {
        Product existing = getById(id);
        if (existing != null) {
            productRepo.remove(existing);
            product.setId(id);
            productRepo.add(product);
        }
    }

    @Override
    public void delete(Long id) {
        Product existing = getById(id);
        if (existing != null)
            productRepo.remove(existing);
    }
}
