package com.frankit.task.dto;

import com.frankit.task.entity.OptionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OptionDTO {
    private Long id;
    private String name;
    private OptionType type;
    private List<String> values; // 선택 타입일 경우만 사용
    private int extraPrice;
    private Long productId; // 옵션이 연결된 상품 ID
}
