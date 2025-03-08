package com.frankit.task.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users") // 'users' 테이블과 매핑
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 (Auto Increment)
    private Long id;

    @Column(nullable = false, unique = true) // 이메일은 반드시 있어야 하며, 중복 불가
    private String email;

    @Column(nullable = false) // 비밀번호는 반드시 입력되어야 함
    private String password;

    // ✅ 사용자가 등록한 상품 리스트 (OneToMany 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>(); // ⚡ List 초기화 (NullPointerException 방지)

}
