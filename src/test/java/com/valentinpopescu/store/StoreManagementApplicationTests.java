package com.valentinpopescu.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.model.Product;
import com.valentinpopescu.store.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StoreManagementApplicationTests {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ProductRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void findAllWhileUnauthenticatedResponse401() throws Exception {
        mvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userRbacOkAndForbidden() throws Exception {
        repository.save(new Product("m1", "Necklace Pandora", new BigDecimal("3500")));

        mvc.perform(get("/api/products")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productCode").value("m1"));

        var add = new ProductCreateRequest("m2", "Watch Rolex", new BigDecimal("50000"));
        mvc.perform(post("/api/products")
                        .with(httpBasic("user","user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(add)))
                .andExpect(status().isForbidden());

        var change = new PriceChangeRequest(new BigDecimal("2550.50"));
        mvc.perform(patch("/api/products/{code}/price", "m1")
                        .with(httpBasic("user","user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(change)))
                .andExpect(status().isForbidden());

        mvc.perform(delete("/api/products/{code}", "m1")
                        .with(httpBasic("user","user123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminRbacOkFullFlowWorks() throws Exception {
        var productCode = "y1";

        mvc.perform(get("/api/products")
                        .with(httpBasic("admin","admin123")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        var create = new ProductCreateRequest(productCode, "Laptop Dell", new BigDecimal("2500.00"));
        mvc.perform(post("/api/products")
                        .with(httpBasic("admin","admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productCode").value(productCode))
                .andExpect(jsonPath("$.name").value("Laptop Dell"));

        mvc.perform(get("/api/products")
                        .with(httpBasic("admin","admin123")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productCode").value(productCode))
                .andExpect(jsonPath("$[0].name").value("Laptop Dell"));

        mvc.perform(get("/api/products/{productCode}", productCode)
                        .with(httpBasic("admin","admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode").value(productCode))
                .andExpect(jsonPath("$.price", is(2500.00)));

        var change = new PriceChangeRequest(new BigDecimal("1999.99"));
        mvc.perform(patch("/api/products/{productCode}/price", productCode)
                        .with(httpBasic("admin","admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(change)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(1999.99)));

        mvc.perform(get("/api/products/{productCode}", productCode)
                        .with(httpBasic("admin","admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(1999.99)));

        mvc.perform(delete("/api/products/{productCode}", productCode)
                        .with(httpBasic("admin","admin123")))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/products/{productCode}", productCode)
                        .with(httpBasic("admin","admin123")))
                .andExpect(status().isNotFound());
    }

    @Test
    void adminCreateBadRequest() throws Exception {
        String invalidBody = """
      { "productCode": "x1", "name": "Sneakers Adidas" }
      """;

        mvc.perform(post("/api/products")
                        .with(httpBasic("admin","admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void adminCreateDuplicateBadRequest() throws Exception {
        var request = new ProductCreateRequest("duplicate",
                "Phone Motorolla", new BigDecimal("300.00"));

        mvc.perform(post("/api/products")
                        .with(httpBasic("admin","admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/products")
                        .with(httpBasic("admin","admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
