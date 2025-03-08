import com.frankit.task.dto.ProductDTO;
import com.frankit.task.entity.Product;
import com.frankit.task.entity.User;
import com.frankit.task.repository.ProductRepository;
import com.frankit.task.repository.UserRepository;
import com.frankit.task.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ✅ 테스트용 유저 생성
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        // ✅ 테스트용 상품 DTO 생성
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("테스트 상품");
        productDTO.setDescription("테스트 상품 설명");
        productDTO.setPrice(10000);
        productDTO.setShippingFee(3000);

        // ✅ 상품 생성 및 저장 Mocking
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName(productDTO.getName());
        testProduct.setDescription(productDTO.getDescription());
        testProduct.setPrice(productDTO.getPrice());
        testProduct.setShippingFee(productDTO.getShippingFee());
        testProduct.setUser(testUser);

        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
    }

    @Test
    @DisplayName("상품을 성공적으로 생성할 수 있다.")
    void createProductTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("새로운 상품");
        productDTO.setDescription("새로운 상품 설명");
        productDTO.setPrice(20000);
        productDTO.setShippingFee(5000);

        Product savedProduct = productService.createProduct(productDTO, testUser);

        assertNotNull(savedProduct);
        assertEquals("새로운 상품", savedProduct.getName());
        assertEquals(20000, savedProduct.getPrice());
    }

    @Test
    @DisplayName("상품을 ID로 조회할 수 있다.")
    void getProductByIdTest() {
        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("테스트 상품", foundProduct.getName());
        assertEquals(10000, foundProduct.getPrice());
    }

    @Test
    @DisplayName("존재하지 않는 상품을 조회하면 예외가 발생해야 한다.")
    void getProductById_NotFoundTest() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.getProductById(2L);
        });

        assertEquals("상품을 찾을 수 없습니다. ID: 2", exception.getMessage());
    }

    @Test
    @DisplayName("상품을 성공적으로 수정할 수 있다.")
    void updateProductTest() {
        ProductDTO updateDTO = new ProductDTO();
        updateDTO.setName("수정된 상품");
        updateDTO.setDescription("수정된 설명");
        updateDTO.setPrice(15000);
        updateDTO.setShippingFee(4000);

        productService.updateProduct(1L, updateDTO, testUser.getId());

        assertEquals("수정된 상품", testProduct.getName());
        assertEquals(15000, testProduct.getPrice());
    }

    @Test
    @DisplayName("다른 유저가 상품을 수정하려 하면 예외가 발생해야 한다.")
    void updateProduct_AccessDeniedTest() {
        ProductDTO updateDTO = new ProductDTO();
        updateDTO.setName("수정된 상품");
        updateDTO.setDescription("수정된 설명");
        updateDTO.setPrice(15000);
        updateDTO.setShippingFee(4000);

        Long anotherUserId = 2L; // 다른 유저 ID

        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            productService.updateProduct(1L, updateDTO, anotherUserId);
        });

        assertEquals("상품을 수정할 권한이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품을 성공적으로 삭제할 수 있다.")
    void deleteProductTest() {
        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 삭제하려 하면 예외가 발생해야 한다.")
    void deleteProduct_NotFoundTest() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(2L);
        });

        assertEquals("삭제할 상품을 찾을 수 없습니다. ID: 2", exception.getMessage());
    }
}