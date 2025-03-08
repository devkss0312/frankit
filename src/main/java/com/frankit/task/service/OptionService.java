package com.frankit.task.service;

import com.frankit.task.dto.OptionDTO;
import com.frankit.task.entity.Option;
import com.frankit.task.entity.Product;
import com.frankit.task.repository.OptionRepository;
import com.frankit.task.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    /**
     * ✅ 옵션 추가 (최대 3개 제한)
     */
    public OptionDTO addOption(OptionDTO optionDTO) {
        validateOptionData(optionDTO);

        Product product = productRepository.findById(optionDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        List<Option> existingOptions = optionRepository.findByProductId(product.getId());
        if (existingOptions.size() >= 3) {
            throw new IllegalArgumentException("옵션은 최대 3개까지 추가할 수 있습니다.");
        }

        Option option = new Option();
        option.setName(optionDTO.getName().trim());
        option.setType(optionDTO.getType());
        option.setValues(optionDTO.getValues());
        option.setExtraPrice(optionDTO.getExtraPrice());
        option.setProduct(product);

        Option savedOption = optionRepository.save(option);
        optionDTO.setId(savedOption.getId());
        return optionDTO;
    }

    /**
     * ✅ 특정 상품의 옵션 조회
     */
    public List<OptionDTO> getOptionsByProduct(Long productId) {
        return optionRepository.findByProductId(productId).stream().map(option -> {
            OptionDTO dto = new OptionDTO();
            dto.setId(option.getId());
            dto.setName(option.getName());
            dto.setType(option.getType());
            dto.setValues(option.getValues());
            dto.setExtraPrice(option.getExtraPrice());
            dto.setProductId(option.getProduct().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * ✅ 옵션 수정
     */
    public OptionDTO updateOption(Long optionId, OptionDTO optionDTO) {
        validateOptionData(optionDTO);

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        option.setName(optionDTO.getName().trim());
        option.setType(optionDTO.getType());
        option.setValues(optionDTO.getValues());
        option.setExtraPrice(optionDTO.getExtraPrice());

        Option updatedOption = optionRepository.save(option);
        optionDTO.setId(updatedOption.getId());
        return optionDTO;
    }

    /**
     * ✅ 옵션 삭제
     */
    public void deleteOption(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new IllegalArgumentException("삭제할 옵션을 찾을 수 없습니다.");
        }
        optionRepository.deleteById(optionId);
    }

    /**
     * ✅ 입력값 검증 (빈 문자열 및 음수 가격 방지)
     */
    private void validateOptionData(OptionDTO optionDTO) {
        if (optionDTO.getName() == null || optionDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("옵션명은 필수 입력값입니다.");
        }
        if (optionDTO.getExtraPrice() < 0) {
            throw new IllegalArgumentException("추가 가격은 0 이상이어야 합니다.");
        }
    }
}
