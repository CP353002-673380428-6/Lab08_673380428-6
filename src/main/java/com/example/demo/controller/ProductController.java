package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // หน้าแรก เด้งไปหน้ารายการสินค้าทันที
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    // GET: แสดงรายการสินค้าทั้งหมด (รองรับทั้ง /products และ /products/)
    @GetMapping({"/products", "/products/"})
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    // GET: หน้าฟอร์มเพิ่มสินค้า
    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/add";
    }

    // POST: บันทึกข้อมูลสินค้าใหม่
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "บันทึกข้อมูลสินค้าเรียบร้อยแล้ว!");
        return "redirect:/products";
    }

    // GET: หน้าฟอร์มแก้ไขสินค้า
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/edit";
    }

    // POST: อัปเดตข้อมูลสินค้า
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("message", "อัปเดตข้อมูลสินค้าสำเร็จ!");
        return "redirect:/products";
    }

    // GET: หน้าฟอร์มยืนยันการลบ
    @GetMapping("/products/delete/{id}")
    public String showDeleteConfirm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/delete";
    }

    // POST: สั่งลบสินค้าออกจากฐานข้อมูล
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "ลบสินค้าเรียบร้อยแล้ว!");
        return "redirect:/products";
    }
}