package com.example.demo.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DiscountContext {

    // Spring จะรวบรวมทุก Bean ที่ implement DiscountStrategy มาใส่ใน Map ให้โดยอัตโนมัติ (DIP)
    private final Map<String, DiscountStrategy> strategies;
    private DiscountStrategy currentStrategy;

    public DiscountContext(Map<String, DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.currentStrategy = strategy;
    }

    public double calculate(double originalPrice) {
        if (currentStrategy == null) {
            return originalPrice;
        }
        return currentStrategy.applyDiscount(originalPrice);
    }

    // OCP: หากมี Strategy ใหม่เพิ่มเข้ามา จะค้นเจอจาก Map ทันทีโดยไม่ต้องแก้โค้ดเมธอดนี้
    public DiscountStrategy getStrategyByType(String type) {
        if (type == null) {
            return strategies.getOrDefault("NONE", new NoDiscountStrategy());
        }
        return strategies.getOrDefault(type.toUpperCase(), 
               strategies.getOrDefault("NONE", new NoDiscountStrategy()));
    }
}