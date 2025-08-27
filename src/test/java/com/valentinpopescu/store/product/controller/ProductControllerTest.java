package com.valentinpopescu.store.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentinpopescu.store.exceptions.GlobalExceptionHandler;
import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;
import com.valentinpopescu.store.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
@Import({ ProductControllerTest.TestSecurityConfig.class, GlobalExceptionHandler.class })
class ProductControllerTest {

    @MockitoBean
    ProductService service;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(roles = "USER")
    void addProductWithUserResultForbidden() throws Exception {
        var request = new ProductCreateRequest(
                "g1", "Vacuum cleaner Dyson", new BigDecimal("3.50"));

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(service, never()).add(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProductWithAdminResultCreated() throws Exception {
        var request = new ProductCreateRequest("x1", "Shampoo Clear", new BigDecimal("25.50"));
        var response = new ProductResponse(10L, "x1", "Shampoo Clear", new BigDecimal("25.50"));
        when(service.add(any()))
                .thenReturn(response);

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productCode").value("x1"))
                .andExpect(jsonPath("$.name").value("Shampoo Clear"))
                .andExpect(jsonPath("$.price", closeTo(25.50, 1e-9)));

        verify(service).add(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProductMissingPriceThrowsBadRequest() throws Exception {
        String body = """
      { "productCode":"X1", "name":"X" }
      """;

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", anyOf(containsString("must not be null"),
                        containsString("required"))));
        verify(service, never()).add(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findByProductCodeWithUserResultOk() throws Exception {
        when(service.findByProductCode("a1"))
                .thenReturn(new ProductResponse(
                        1L, "a1", "Chainsaw Stihl", new BigDecimal("250.00")));

        mvc.perform(get("/api/products/{productCode}", "a1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode").value("a1"))
                .andExpect(jsonPath("$.name").value("Chainsaw Stihl"))
                .andExpect(jsonPath("$.price", closeTo(250.00, 1e-9)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAllProductsWithUserResultOk() throws Exception {
        when(service.findAll())
                .thenReturn(List.of(
                        new ProductResponse(1L, "p1", "Samsung TV", new BigDecimal("800.00")),
                        new ProductResponse(2L, "p2", "iPhone 15", new BigDecimal("1500.00"))
                )
        );

        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productCode").value("p1"))
                .andExpect(jsonPath("$[1].name").value("iPhone 15"));

        verify(service).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void changePriceWithUserResultForbidden() throws Exception {
        var body = mapper.writeValueAsString(
                new PriceChangeRequest(new BigDecimal("9.99")));

        mvc.perform(patch("/api/products/{productCode}/price", "h1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());

        verify(service, never()).changePrice(anyString(), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changePriceWithAdminResultOk() throws Exception {
        var body = mapper.writeValueAsString(
                new PriceChangeRequest(new BigDecimal("9.99")));
        when(service.changePrice(anyString(), any()))
                .thenReturn(new ProductResponse(1L, "b1", "Big Mac", new BigDecimal("9.99")));

        mvc.perform(patch("/api/products/{productCode}/price", "b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(9.99));

        verify(service).changePrice(eq("b1"), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteWithUserResultForbidden() throws Exception {
        mvc.perform(delete("/api/products/{productCode}", "m1"))
                .andExpect(status().isForbidden());

        verify(service, never()).deleteByProductCode(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWithAdminResultNoContent() throws Exception {
        mvc.perform(delete("/api/products/{productCode}", "k1"))
                .andExpect(status().isNoContent());

        verify(service).deleteByProductCode("k1");
    }

    @Test
    void findAllWhenUnauthenticatedAndUnauthorized() throws Exception {
        mvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .httpBasic(Customizer.withDefaults())
                    .formLogin(form -> form.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll()
                    );

            return http.build();
        }
    }
}