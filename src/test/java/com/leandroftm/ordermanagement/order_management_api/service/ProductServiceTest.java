package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdatePriceRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Category;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.InvalidProductPriceException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.InvalidStockException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.ProductNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.repository.CategoryRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private final long id = 10L;

    //CREATE

    @Test
    void shouldCreateProductSuccessfully() {
        Category savedCategory = new Category("Category Test");

        CreateProductRequest request = new CreateProductRequest(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );

        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );
        savedProduct.setCategory(savedCategory);
        ReflectionTestUtils.setField(savedProduct, "id", id);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(savedCategory));

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        long createdId = productService.create(1L, request);

        assertEquals(createdId, savedProduct.getId());

        verify(productRepository).save(argThat(product ->
                product.getName().equals("Product Name Test") &&
                        product.getCategory().getName().equals("Category Test")));

        verifyNoMoreInteractions(productRepository);
    }

    //GET

    @Test
    void shouldReturnAllProductsSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = List.of(new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        ));

        products.forEach(product -> product.setCategory(new Category("Category Test")));

        Page<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductResponse> responses = productService.findAll(pageable);

        assertNotNull(responses);
        assertEquals("Product Name Test", responses.getContent().get(0).name());
        assertEquals("Category Test", responses.getContent().get(0).categoryName());

        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnProductSuccessfully() {
        Category savedCategory = new Category("Category Test");

        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );

        savedProduct.setCategory(savedCategory);

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));

        ProductResponse response = productService.findById(id);

        assertNotNull(response);
        assertEquals("Product Name Test", response.name());
        verify(productRepository).findById(id);
    }

    @Test
    void shouldReturnProductsByCategorySuccessfully() {
        Category savedCategory = new Category("Category Test");
        ReflectionTestUtils.setField(savedCategory, "id", 1L);

        Pageable pageable = PageRequest.of(0, 10);
        List<ProductResponse> productResponses = List.of(new ProductResponse(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                1L,
                "Category Test"
        ));

        Page<ProductResponse> page = new PageImpl<>(productResponses, pageable, productResponses.size());

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findAllByCategory(1L, pageable)).thenReturn(page);

        Page<ProductResponse> responses = productService.findAllProductsByCategory(1L, pageable);

        assertNotNull(responses);
        assertEquals("Product Name Test", responses.getContent().get(0).name());
        assertEquals("Category Test", responses.getContent().get(0).categoryName());
        verify(categoryRepository).existsById(1L);
        verify(productRepository).findAllByCategory(1L, pageable);
        verifyNoMoreInteractions(categoryRepository, productRepository);
    }

    // UPDATE
    //PUT

    @Test
    void shouldUpdateProductSuccessfully() {
        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );
        UpdateProductRequest request = new UpdateProductRequest(
                "New Product Test",
                ""
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));

        when(productRepository.existsByNameAndIdNot(request.name(), id)).thenReturn(false);

        productService.update(id, request);

        verify(productRepository).findById(id);
        verify(productRepository).existsByNameAndIdNot(request.name(), id);
        verify(productRepository).save(argThat(product ->
                product.getName().equals("New Product Test")));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldUpdateProductPriceSuccessfully() {
        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );
        UpdatePriceRequest request = new UpdatePriceRequest(
                new BigDecimal("50.00")
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));

        productService.updatePrice(id, request);
        verify(productRepository).findById(id);
        verify(productRepository).save(argThat(product ->
                product.getPrice().equals(new BigDecimal("50.00"))));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldIncreaseProductStockSuccessfully() {
        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));
        productService.increaseStock(id, 10);

        verify(productRepository).findById(id);
        verify(productRepository).save(argThat(product ->
                product.getStock() == 20));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldDecreaseProductStockSuccessfully() {
        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));
        productService.decreaseStock(id, 2);

        verify(productRepository).findById(id);
        verify(productRepository).save(argThat(product ->
                product.getStock() == 8));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldDisableProductSuccessfully() {
        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );
        savedProduct.setActive(true); //ENSURE PRODUCT IS ENABLED FOR TEST
        assertTrue(savedProduct.isActive());

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));

        productService.disableProduct(id);
        assertFalse(savedProduct.isActive());

        verify(productRepository).findById(id);
        verify(productRepository).save(argThat(product ->
                !product.isActive()));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldEnableProductSuccessfully() {
        Product savedProduct = new Product(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );
        savedProduct.setActive(false);// ENSURE PRODUCT IS DISABLED FOR TEST
        assertFalse(savedProduct.isActive());

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));

        productService.enableProduct(id);
        assertTrue(savedProduct.isActive());

        verify(productRepository).findById(id);
        verify(productRepository).save(argThat(Product::isActive));
        verifyNoMoreInteractions(productRepository);
    }

    // # TEST EXCEPTIONS
    @Test
    void shouldReturnNotFoundWhenCategoryDoesNotExists() {
        CreateProductRequest request = new CreateProductRequest(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                10
        );

        assertThrows(CategoryNotFoundException.class, () -> productService.create(id, request));

        verifyNoInteractions(productRepository);
    }

    @Test
    void shouldReturnBadRequestWhenProductPriceIsInvalid() {
        CreateProductRequest request = new CreateProductRequest(
                "Product Name Test",
                "",
                new BigDecimal("-100.00"),
                10
        );

        assertThrows(InvalidProductPriceException.class, () -> productService.create(id, request));

        verifyNoInteractions(productRepository);
    }

    @Test
    void shouldReturnBadRequestWhenProductStockIsInvalid() {
        CreateProductRequest request = new CreateProductRequest(
                "Product Name Test",
                "",
                new BigDecimal("100.00"),
                -10
        );

        assertThrows(InvalidStockException.class, () -> productService.create(id, request));

        verifyNoInteractions(productRepository);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExists() {
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(id));

        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnBadRequestWhenProductCategoryDoesNotExists() {
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> productService.findAllProductsByCategory(1L, pageable));
        verifyNoMoreInteractions(categoryRepository);
    }

    //TODO
    //UPDATE EXCEPTIONS PUT PATCH POST
}
