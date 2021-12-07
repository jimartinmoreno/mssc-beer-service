package guru.springframework.msscbeerservice.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(@Value("${sfg.brewery.inventory-user:good}") String inventoryUser,
                                                 @Value("${sfg.brewery.inventory-password:beer}") String inventoryPassword) {
        return requestTemplate -> {
            requestTemplate.header("user", inventoryUser);
            requestTemplate.header("password", inventoryPassword);

            requestTemplate.queries().entrySet().forEach(entry -> log.debug("Query - " + entry.getKey() +"= " + entry.getValue()));
            requestTemplate.headers().entrySet().forEach(entry -> log.debug("Header - " + entry.getKey() +"= " + entry.getValue()));

            // requestTemplate.header("Accept", MediaType.APPLICATION_JSON.getType());
        };
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${sfg.brewery.inventory-user:good}") String inventoryUser,
                                                                   @Value("${sfg.brewery.inventory-password:beer}") String inventoryPassword) {
        return new BasicAuthRequestInterceptor(inventoryUser, inventoryPassword);
    }
}
