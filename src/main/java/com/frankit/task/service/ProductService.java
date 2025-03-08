package com.frankit.task.service;

import com.frankit.task.dto.ProductDTO;
import com.frankit.task.entity.Product;
import com.frankit.task.entity.User;
import com.frankit.task.repository.ProductRepository;
import com.frankit.task.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * ✅ 상품 등록 (User 정보는 Controller에서 처리)
     */
    @Transactional
    public Product createProduct(ProductDTO productDTO, User user) {
        validateProductData(productDTO);

        Product product = new Product();
        product.setName(productDTO.getName().trim());
        product.setDescription(productDTO.getDescription().trim());
        product.setPrice(productDTO.getPrice());
        product.setShippingFee(productDTO.getShippingFee());
        product.setUser(user);
        product.setCreatedAt(new Date());

        return productRepository.save(product);
    }

    /**
     * ✅ 특정 상품 조회
     */
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));
    }

    /**
     * ✅ 사용자의 상품 목록 조회 (페이지네이션 적용)
     */
    public Page<Product> getUserProducts(Long userId, int page, int size) {
        return productRepository.findByUserIdOrderByIdDesc(userId, PageRequest.of(page, size));
    }

    /**
     * ✅ 상품 수정
     */
    @Transactional
    public void updateProduct(Long productId, ProductDTO updatedProduct, Long userId) {
        validateProductData(updatedProduct);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        if (!product.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("상품을 수정할 권한이 없습니다.");
        }

        product.setName(updatedProduct.getName().trim());
        product.setDescription(updatedProduct.getDescription().trim());
        product.setPrice(updatedProduct.getPrice());
        product.setShippingFee(updatedProduct.getShippingFee());

        productRepository.save(product);
    }

    /**
     * ✅ 상품 삭제 (존재 여부 확인 후 삭제)
     */
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 상품을 찾을 수 없습니다. ID: " + productId));

        productRepository.delete(product);
    }

    /**
     * ✅ 입력값 검증 (빈 문자열 및 음수 가격 방지)
     */
    private void validateProductData(ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 필수 입력값입니다.");
        }
        if (productDTO.getDescription() == null || productDTO.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("상품 설명은 필수 입력값입니다.");
        }
        if (productDTO.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        if (productDTO.getShippingFee() < 0) {
            throw new IllegalArgumentException("배송비는 0 이상이어야 합니다.");
        }
    }
}
