package guru.springframework.msscbeerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @EnableFeignClients Scans for interfaces that declare they are feign clients (via FeignClient @FeignClient).
 * Configures component scanning directives for use with org.springframework.context.annotation.Configuration
 * @Configuration classes.
 */
@EnableFeignClients // Puede que fuera mejor opción definirlo dentro de la propia clase de configuración FeignCLientConfig
@SpringBootApplication
public class BeerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeerServiceApplication.class, args);
    }
}
