package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String brand;
    private Integer stock;
    private Double price;
    private String discountType = "NONE";

    // ── 1:1 กับ ProductDetail (Owner side) ──
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "detail_id", referencedColumnName = "id")
    private ProductDetail detail = new ProductDetail();

    // ── 1:N กับ Review ──
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // เก็บราคาสุทธิที่คำนวณแล้วสำหรับแสดงผลใน View (ไม่บันทึกลงฐานข้อมูล)
    @Transient
    private Double discountedPrice;

    public Product() {
        // เพิ่ม Review เปล่าไว้ 1 ตัวเพื่อรองรับการ Binding ฟอร์มใน add.html
        Review initialReview = new Review();
        initialReview.setProduct(this);
        this.reviews.add(initialReview);
    }

    // Helper method จัดการความสัมพันธ์ Review
    public void addReview(Review review) {
        reviews.add(review);
        review.setProduct(this);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public ProductDetail getDetail() { return detail; }
    public void setDetail(ProductDetail detail) { 
        this.detail = detail; 
        if (detail != null) {
            detail.setProduct(this);
        }
    }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public Double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(Double discountedPrice) { this.discountedPrice = discountedPrice; }
}