package com.app.userapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import com.app.common.repository.ProductRepository;
import com.app.common.repository.ProductChangeRepository;
import com.app.common.repository.ReviewRepository;
import com.app.common.entity.Product;
import com.app.common.entity.ProductChange;
import com.app.userapi.service.ProductService;
import com.app.userapi.service.dto.ProductValidationResult;
import com.app.userapi.service.dto.SaveProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Map;
import org.mockito.Spy;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductChangeRepository productChangeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	void testProductChangeSaved() {
	    var product = new Product();
	    product.setName("Product name");

	    when(productChangeRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));

	    var validationResult = new ProductValidationResult(product, null);
	    var dto = new SaveProductDto(null, "Product new name", null);

	    productService.save(validationResult, dto);

        verify(productChangeRepository).save(any(ProductChange.class));
        verify(productChangeRepository, never()).delete(any(ProductChange.class));
	}

	@Test
	void testProductChangeDeleted() {
	    var product = new Product();
	    product.setName("Product name");

        var productChange = new ProductChange();
        productChange.setFieldValues(objectMapper.valueToTree(Map.of(
            "name", "Product new name"
        )));
	    when(productChangeRepository.findById(any(Integer.class))).thenReturn(Optional.of(productChange));

	    var validationResult = new ProductValidationResult(product, null);
	    var dto = new SaveProductDto(null, "Product name", null);
	    productService.save(validationResult, dto);

        verify(productChangeRepository, never()).save(any(ProductChange.class));
        verify(productChangeRepository).delete(any(ProductChange.class));
	}

}
