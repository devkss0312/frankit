package com.frankit.task.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 옵션 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType type; // 옵션 타입 (입력/선택)

    @ElementCollection
    private List<String> values; // 선택 타입일 경우 선택 가능한 값 목록

    @Column(nullable = false)
    private int extraPrice; // 추가 금액

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 옵션이 연결된 상품
}