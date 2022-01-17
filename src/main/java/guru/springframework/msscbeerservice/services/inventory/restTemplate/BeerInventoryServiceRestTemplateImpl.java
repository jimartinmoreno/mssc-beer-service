package guru.springframework.msscbeerservice.services.inventory.restTemplate;

import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryServiceConstants;
import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Profile("!local-discovery") Solo se usa si no usamos service dicovery
 * prefix = The prefix of the properties that are valid to bind to this object.
 * ignoreUnknownFields = true -> Flag to indicate that when binding to this object unknown fields should be ignored.
 * An unknown field could be a sign of a mistake in the Properties.
 */
@Profile("!local-discovery")
@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = true) //
@Component
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService, BeerInventoryServiceConstants {

    // Synchronous client to perform HTTP requests
    private final RestTemplate restTemplate;

    // Lo inyecta Spring desde application.properties
    private String beerInventoryServiceHost;

    /**
     * @Value Annotation used at the field or method/constructor parameter level that indicates a default value expression
     * for the annotated element.
     * <p>
     * A common use case is to inject values using #{systemProperties.myProp} style SpEL (Spring Expression Language) expressions.
     * Alternatively, values may be injected using ${my.app.myProp} style property placeholders.
     */
    public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder,
                                                @Value("${sfg.brewery.inventory-user}") String user,
                                                @Value("${sfg.brewery.inventory-password}") String password) {
        this.restTemplate = restTemplateBuilder.basicAuthentication(user, password).build();
    }

    public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
        this.beerInventoryServiceHost = beerInventoryServiceHost;
    }

    @Override
    public Integer getOnhandInventory(UUID beerId) {

        log.debug("Calling Inventory Service");
        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate
                .exchange(beerInventoryServiceHost + BeerInventoryServiceConstants.INVENTORY_PATH, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<BeerInventoryDto>>() {
                        },
                        beerId);

        //sum from inventory list
        Integer onHand = Objects.requireNonNull(responseEntity.getBody()).stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
        log.debug("getOnhandInventory QuantityOnHand: " + onHand);
        return onHand;
    }
}
