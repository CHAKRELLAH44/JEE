package com.mcommerce.microservice_produits.controller;

import com.mcommerce.microservice_produits.configurations.GlobalConfig;
import com.mcommerce.microservice_produits.dao.ProductDao;
import com.mcommerce.microservice_produits.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;

@RestController
public class ProductController implements HealthIndicator {

    @Autowired
    private GlobalConfig globalConfig;

    //private final List<String> produits = new ArrayList<>(List.of("Café", "Thé", "Chocolat", "Cappuccino"));
    @Autowired
    private ProductDao productDao;

    // Endpoint : liste limitée de produits
    @GetMapping("/Produits")
    public List<Product> listeDesProduits() {
        int limite = globalConfig.getLimitDeProduits();
        List<Product> produits = productDao.findAll();
        return produits.subList(0, Math.min(limite, produits.size()));
    }

    @Override
    public Health health() {
        long count = productDao.count();
        if (count == 0) {
            return Health.down().withDetail("Erreur", "Aucun produit en base").build();
        }
        return Health.up().withDetail("Produits_en_base", count).build();
    }
}
