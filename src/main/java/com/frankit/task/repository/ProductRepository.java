package com.frankit.task.repository;

import com.frankit.task.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByUserIdOrderByIdDesc(Long userId, Pageable pageable); // ✅ ID 역순 정렬 추가
}
