package com.frankit.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "index"; // index.html 반환
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 반환
    }

    @GetMapping("/products")
    public String productListPage() {
        return "product_list"; // product_list.html 반환
    }

    @GetMapping("/products/add")
    public String productAddPage() {
        return "product_add"; // product_detail.html 반환
    }



    @GetMapping("/products/edit")
    public String editProduct(@RequestParam("id") Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "product_edit"; // ✅ product_edit.html 반환
    }
}
