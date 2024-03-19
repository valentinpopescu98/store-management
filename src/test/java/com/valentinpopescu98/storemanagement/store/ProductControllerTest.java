package com.valentinpopescu98.storemanagement.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    private static final Product DUMMY_PRODUCT = new Product("dummy-name", "dummy-description",
            1L, null, 0L);
    private static final Long DUMMY_ID = 1L;
    private static final BindingResult BINDING_RESULT =
            new BeanPropertyBindingResult(DUMMY_PRODUCT, DUMMY_PRODUCT.getName());

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private ProductService service;
    private ProductController controller;

    @BeforeEach
    void setUp() {
        // given
        controller = new ProductController(service);
    }

    @Test
    void getOneTest() {
        // when
        ResponseEntity<?> response = controller.getOne(DUMMY_ID);

        // then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(service).getOne(captor.capture());

        Long id = captor.getValue();
        assertEquals(DUMMY_ID, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getOneEntityNotFoundExceptionThrownTest() {
        // when
        doThrow(EntityNotFoundException.class).when(service).getOne(anyLong());
        ResponseEntity<?> response = controller.getOne(anyLong());

        // then
        assertThrows(EntityNotFoundException.class, () -> service.getOne(DUMMY_ID));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllTest() {
        // when
        controller.getAll();

        // then
        verify(service).getAll();
    }

    @Test
    void getAllEmptyResultDataAccessExceptionThrownTest() {
        // when
        doThrow(EmptyResultDataAccessException.class).when(service).getAll();
        ResponseEntity<?> response = controller.getAll();

        // then
        assertThrows(EmptyResultDataAccessException.class, () -> service.getAll());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addTest() {
        // when
        controller.add(DUMMY_PRODUCT, BINDING_RESULT);

        // then
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(service).add(captor.capture());

        Product product = captor.getValue();
        assertEquals(DUMMY_PRODUCT, product);
    }

    @Test
    void addNullAndEmptyNameTest() throws Exception {
        // given
        Product product = new Product(null, "dummy-description", 1L, null, 0L);

        // when
        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Product must have a name.")))
                .andExpect(content().string(containsString("Product name must not be blank.")));

        // then
        verifyNoInteractions(service);
    }

    @Test
    void addNullAndEmptyDescriptionTest() throws Exception {
        // given
        Product product = new Product("dummy-name", null, 1L, null, 0L);

        // when
        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Product must have a description.")))
                .andExpect(content().string(containsString("Product description must not be blank.")));

        // then
        verifyNoInteractions(service);
    }

    @Test
    void addNullPriceTest() throws Exception {
        // given
        Product product = new Product("dummy-name", "dummy-description",
                null, null, 0L);

        // when
        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Product must have a price.")));

        // then
        verifyNoInteractions(service);
    }

    @Test
    void addNullStockTest() throws Exception {
        // given
        Product product = new Product("dummy-name", "dummy-description",
                1L, null, null);

        // when
        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Product must have a stock.")));

        // then
        verifyNoInteractions(service);
    }

    @Test
    void addDataIntegrityViolationExceptionThrownTest() {
        // when
        doThrow(DataIntegrityViolationException.class).when(service).add(DUMMY_PRODUCT);
        ResponseEntity<?> response2 = controller.add(DUMMY_PRODUCT, BINDING_RESULT);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> service.add(DUMMY_PRODUCT));
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    void addIllegalArgumentExceptionThrownTest() {
        // when
        doThrow(IllegalArgumentException.class).when(service).add(DUMMY_PRODUCT);
        ResponseEntity<?> response3 = controller.add(DUMMY_PRODUCT, BINDING_RESULT);

        // then
        assertThrows(IllegalArgumentException.class, () -> service.add(DUMMY_PRODUCT));
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
    }

    @Test
    void updateTest() {
        // when
        controller.update(DUMMY_ID, DUMMY_PRODUCT);

        // then
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(service).update(idCaptor.capture(), productCaptor.capture());

        Long id = idCaptor.getValue();
        assertEquals(DUMMY_ID, id);

        Product product = productCaptor.getValue();
        assertEquals(DUMMY_PRODUCT, product);
    }

    @Test
    void updateEntityNotFoundExceptionThrownTest() {
        // when
        doThrow(EntityNotFoundException.class).when(service).update(DUMMY_ID, DUMMY_PRODUCT);
        ResponseEntity<?> response1 = controller.update(DUMMY_ID, DUMMY_PRODUCT);

        // then
        assertThrows(EntityNotFoundException.class, () -> service.update(DUMMY_ID, DUMMY_PRODUCT));
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
    }

    @Test
    void updateDataIntegrityViolationExceptionThrownTests() {
        // when
        doThrow(DataIntegrityViolationException.class).when(service).update(DUMMY_ID, DUMMY_PRODUCT);
        ResponseEntity<?> response2 = controller.update(DUMMY_ID, DUMMY_PRODUCT);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> service.update(DUMMY_ID, DUMMY_PRODUCT));
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    void updateIllegalArgumentExceptionThrownTest() {
        // when
        doThrow(IllegalArgumentException.class).when(service).update(DUMMY_ID, DUMMY_PRODUCT);
        ResponseEntity<?> response3 = controller.update(DUMMY_ID, DUMMY_PRODUCT);

        // then
        assertThrows(IllegalArgumentException.class, () -> service.update(DUMMY_ID, DUMMY_PRODUCT));
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
    }

    @Test
    void removeTest() {
        // when
        controller.remove(DUMMY_ID);

        // then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(service).remove(captor.capture());

        Long id = captor.getValue();
        assertEquals(DUMMY_ID, id);
    }

    @Test
    void removeEntityNotFoundExceptionThrownTest() {
        // when
        doThrow(EntityNotFoundException.class).when(service).remove(anyLong());
        ResponseEntity<?> response = controller.remove(anyLong());

        // then
        assertThrows(EntityNotFoundException.class, () -> service.remove(DUMMY_ID));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}