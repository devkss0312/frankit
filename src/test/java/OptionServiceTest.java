package com.frankit.task.service;

import com.frankit.task.dto.OptionDTO;
import com.frankit.task.entity.Option;
import com.frankit.task.entity.OptionType;
import com.frankit.task.entity.Product;
import com.frankit.task.repository.OptionRepository;
import com.frankit.task.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptionServiceTest {

    @InjectMocks
    private OptionService optionService; // ✅ 실제 서비스 객체

    @Mock
    private OptionRepository optionRepository; // ✅ 가짜 레포지토리

    @Mock
    private ProductRepository productRepository; // ✅ 가짜 레포지토리

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // ✅ Mockito 초기화

        // ✅ 테스트용 상품 생성 (실제 DB 저장 X)
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("테스트 상품");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    }

    @Test
    @DisplayName("옵션을 추가하면 정상적으로 저장되고 반환되어야 한다.")
    void addOption() {
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setName("색상");
        optionDTO.setType(OptionType.SELECT);
        optionDTO.setValues(Arrays.asList("빨강", "파랑", "초록"));
        optionDTO.setExtraPrice(2000);
        optionDTO.setProductId(1L);

        Option option = new Option();
        option.setId(1L);
        option.setName(optionDTO.getName());
        option.setType(optionDTO.getType());
        option.setValues(optionDTO.getValues());
        option.setExtraPrice(optionDTO.getExtraPrice());
        option.setProduct(testProduct);

        when(optionRepository.findByProductId(1L)).thenReturn(Arrays.asList());
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        OptionDTO savedOption = optionService.addOption(optionDTO);

        assertNotNull(savedOption);
        assertEquals("색상", savedOption.getName());
        verify(optionRepository, times(1)).save(any(Option.class)); // ✅ 저장 확인
    }

    @Test
    @DisplayName("옵션 목록을 조회할 수 있어야 한다.")
    void getAllOptionsForProduct() {
        Option option1 = new Option();
        option1.setId(1L);
        option1.setName("색상");
        option1.setType(OptionType.SELECT);
        option1.setValues(Arrays.asList("빨강", "파랑", "초록"));
        option1.setExtraPrice(2000);
        option1.setProduct(testProduct);

        Option option2 = new Option();
        option2.setId(2L);
        option2.setName("사이즈");
        option2.setType(OptionType.SELECT);
        option2.setValues(Arrays.asList("S", "M", "L"));
        option2.setExtraPrice(1000);
        option2.setProduct(testProduct);

        when(optionRepository.findByProductId(1L)).thenReturn(Arrays.asList(option1, option2));

        List<OptionDTO> options = optionService.getOptionsByProduct(1L);

        assertEquals(2, options.size());
        assertEquals("색상", options.get(0).getName());
        assertEquals("사이즈", options.get(1).getName());
    }

    @Test
    @DisplayName("옵션을 수정할 수 있다.")
    void updateOption() {
        Option existingOption = new Option();
        existingOption.setId(1L);
        existingOption.setName("크기");
        existingOption.setType(OptionType.SELECT);
        existingOption.setValues(Arrays.asList("소", "중", "대"));
        existingOption.setExtraPrice(1500);
        existingOption.setProduct(testProduct);

        OptionDTO updatedOptionDTO = new OptionDTO();
        updatedOptionDTO.setName("수정된 크기");
        updatedOptionDTO.setType(OptionType.INPUT);
        updatedOptionDTO.setExtraPrice(500);
        updatedOptionDTO.setProductId(1L);

        when(optionRepository.findById(1L)).thenReturn(Optional.of(existingOption));
        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OptionDTO updatedOption = optionService.updateOption(1L, updatedOptionDTO);

        assertEquals("수정된 크기", updatedOption.getName());
        assertEquals(OptionType.INPUT, updatedOption.getType());
        verify(optionRepository, times(1)).save(any(Option.class)); // ✅ 저장 호출 확인
    }

    @Test
    @DisplayName("옵션을 삭제할 수 있다.")
    void deleteOption() {
        when(optionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(optionRepository).deleteById(1L);

        optionService.deleteOption(1L);

        verify(optionRepository, times(1)).deleteById(1L); // ✅ 삭제 호출 확인
    }

    @Test
    @DisplayName("옵션이 3개 이상 추가되면 예외가 발생해야 한다.")
    void addMoreThanThreeOptionsShouldFail() {
        Option option1 = new Option();
        Option option2 = new Option();
        Option option3 = new Option();

        when(optionRepository.findByProductId(1L)).thenReturn(Arrays.asList(option1, option2, option3));

        OptionDTO extraOption = new OptionDTO();
        extraOption.setName("초과 옵션");
        extraOption.setType(OptionType.SELECT);
        extraOption.setValues(Arrays.asList("X", "Y", "Z"));
        extraOption.setExtraPrice(500);
        extraOption.setProductId(1L);

        assertThrows(IllegalArgumentException.class, () -> optionService.addOption(extraOption));
    }
}
