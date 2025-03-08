package com.frankit.task.controller;

import com.frankit.task.dto.ProductDTO;
import com.frankit.task.entity.Product;
import com.frankit.task.entity.User;
import com.frankit.task.repository.UserRepository;
import com.frankit.task.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    /**
     * ✅ 상품 등록 (User 정보 Controller에서 처리)
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(productService.createProduct(productDTO, user));
    }

    /**
     * ✅ 특정 상품 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    /**
     * ✅ 사용자의 상품 목록 조회 (페이징 적용)
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getUserProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(productService.getUserProducts(user.getId(), page, size));
    }

    /**
     * ✅ 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));

        productService.updateProduct(id, productDTO, user.getId());
        return ResponseEntity.ok("상품이 수정되었습니다.");
    }

    /**
     * ✅ 상품 삭제
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}

