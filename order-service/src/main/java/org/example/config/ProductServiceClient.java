package org.example.config;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient {
    
    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public ProductServiceClient(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getDefaultProductPrice")
    public BigDecimal getProductPrice(Long productId) {
        return circuitBreakerFactory.create("productService")
            .run(() -> {
                ProductDTO product = restTemplate.getForObject(
                    "http://product-service/api/products/" + productId,
                    ProductDTO.class
                );
                return product.getPrice();
            }, throwable -> getDefaultProductPrice(productId, throwable));
    }

    private BigDecimal getDefaultProductPrice(Long productId, Throwable t) {
        // Log l'erreur et retourne une valeur par défaut ou lance une exception personnalisée
        log.error("Error getting product price for id: " + productId, t);
        throw new ProductServiceException("Product service is not available");
    }
}