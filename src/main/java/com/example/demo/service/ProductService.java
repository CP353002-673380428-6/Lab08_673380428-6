package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.DiscountContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountContext discountContext;

    // DIP: รับ Abstraction / Spring-managed Bean ผ่าน Constructor Injection
    public ProductService(ProductRepository productRepository, DiscountContext discountContext) {
        this.productRepository = productRepository;
        this.discountContext = discountContext;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            discountContext.setStrategy(discountContext.getStrategyByType(product.getDiscountType()));
            double price = (product.getPrice() != null) ? product.getPrice() : 0.0;
            double discounted = discountContext.calculate(price);
            product.setDiscountedPrice(discounted);
        }
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id: " + id));
    }

    public void saveProduct(Product product) {
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }

        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            List<Review> validReviews = product.getReviews().stream()
                    .filter(r -> r.getReviewer() != null && !r.getReviewer().trim().isEmpty())
                    .toList();

            for (Review review : validReviews) {
                review.setProduct(product);
                if (review.getReviewDate() == null) {
                    review.setReviewDate(LocalDate.now());
                }
            }
            product.setReviews(validReviews);
        }

        productRepository.save(product);
    }

    public void updateProduct(Long id, Product updatedData) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedData.getName());
        existingProduct.setCategory(updatedData.getCategory());
        existingProduct.setBrand(updatedData.getBrand());
        existingProduct.setStock(updatedData.getStock());
        existingProduct.setPrice(updatedData.getPrice());
        existingProduct.setDiscountType(updatedData.getDiscountType());

        if (updatedData.getDetail() != null) {
            if (existingProduct.getDetail() == null) {
                existingProduct.setDetail(updatedData.getDetail());
            } else {
                existingProduct.getDetail().setDescription(updatedData.getDetail().getDescription());
                existingProduct.getDetail().setWarranty(updatedData.getDetail().getWarranty());
                existingProduct.getDetail().setWeight(updatedData.getDetail().getWeight());
                existingProduct.getDetail().setDimensions(updatedData.getDetail().getDimensions());
                existingProduct.getDetail().setManufacturedCountry(updatedData.getDetail().getManufacturedCountry());
            }
        }

        productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}