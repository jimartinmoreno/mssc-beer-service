package guru.springframework.msscbeerservice.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Solo se usa si usamos service discovery con eureka. Declaramos los interceptores que se emplean en cada petición del
 * cliente Feign
 */
@Profile("local-discovery")
@Configuration
@Slf4j
public class FeignClientConfig {


    /**
     * Defines a RequestInterceptor that is Called for every request. Add data using methods on the supplied RequestTemplate.
     * @param inventoryUser
     * @param inventoryPassword
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(@Value("${sfg.brewery.inventory-user:good}") String inventoryUser, @Value("${sfg.brewery.inventory-password:beer}") String inventoryPassword) {
        return requestTemplate -> {
            requestTemplate.header("user", inventoryUser);
            requestTemplate.header("password", inventoryPassword);
            // requestTemplate.header("Accept", ContentType.APPLICATION_JSON.getMimeType());
            // requestTemplate.header("Accept", MediaType.APPLICATION_JSON.getType());

            // Pintamos todos los parametros del query string de la request
            requestTemplate.queries()
                    .entrySet()
                    .forEach(entry -> log.debug("Query - " + entry.getKey() + "= " + entry.getValue()));

            // Pintamos todos los headers de la request
            requestTemplate.headers()
                    .entrySet()
                    .forEach(entry -> log.debug("Header - " + entry.getKey() + "= " + entry.getValue()));


        };
    }

    /**
     * Defines An interceptor that adds the request header needed to use HTTP basic authentication.
     * @param inventoryUser
     * @param inventoryPassword
     * @return
     */
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${sfg.brewery.inventory-user:good}") String inventoryUser, @Value("${sfg.brewery.inventory-password:beer}") String inventoryPassword) {
        return new BasicAuthRequestInterceptor(inventoryUser, inventoryPassword);
    }
}
