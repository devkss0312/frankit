package com.frankit.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@Table(name = "products", uniqueConstraints = {@UniqueConstraint(columnNames = "name")}) // name이 중복되지 않도록 설정
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double shippingFee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date(); // ✅ 등록일 추가, null 불가능하게 설정

    // ✅ 상품 등록한 사용자 정보 추가 (ManyToOne 관계)
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // null 불가능하게 설정
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}