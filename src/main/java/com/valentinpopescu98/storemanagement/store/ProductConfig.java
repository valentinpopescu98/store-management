package com.valentinpopescu98.storemanagement.store;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunnerProduct(ProductRepository repository) {
        return args -> {
            Product product1 = new Product(
                    "Telefon mobil Apple iPhone 15 Pro Max, 256GB, 5G, Black Titanium",
                    "Telefon mobil Apple iPhone 15 Pro Max, 256GB, 5G, Black Titanium",
                    669999L,
                    null,
                    25L
            );

            Product product2 = new Product(
                    "Telefon mobil Samsung Galaxy S24 Ultra, Dual SIM, 12GB RAM, 256GB, 5G, Titanium Gray",
                    "Telefon mobil Samsung Galaxy S24 Ultra, Dual SIM, 12GB RAM, 256GB, 5G, Titanium Gray",
                    623999L,
                    719999L,
                    49L
            );

            Product product3 = new Product(
                    "Telefon mobil Google Pixel 8 Pro, 256GB, 12GB RAM, 5G, Obsidian",
                    "Google Pixel 8 Pro\nCu Google AI si cea mai buna camera Pixel, " +
                            "este cel mai puternic si personal Pixel de pana acum.",
                    502849L,
                    null,
                    87L
            );

            repository.saveAll(List.of(product1, product2, product3));
        };
    }

}
