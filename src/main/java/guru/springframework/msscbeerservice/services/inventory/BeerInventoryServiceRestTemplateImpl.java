package guru.springframework.msscbeerservice.services.inventory;

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
 * Created by jt on 2019-06-07.
 */
@Profile("!local-discovery")
@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = true)
@Component
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {

    public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";
    private final RestTemplate restTemplate;

    private String beerInventoryServiceHost;

    /**
     * @Value Annotation used at the field or method/constructor parameter level that indicates a default value expression
     * for the annotated element.
     */
    public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder,
                                                @Value("${sfg.brewery.inventory-user}") String inventoryUser,
                                                @Value("${sfg.brewery.inventory-password}") String inventoryPassword) {
        this.restTemplate = restTemplateBuilder.basicAuthentication(inventoryUser, inventoryPassword).build();
    }

    public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
        this.beerInventoryServiceHost = beerInventoryServiceHost;
    }

    @Override
    public Integer getOnhandInventory(UUID beerId) {

        log.debug("Calling Inventory Service");
        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate
                .exchange(beerInventoryServiceHost + INVENTORY_PATH, HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BeerInventoryDto>>() {},
                        beerId);

        //sum from inventory list
        Integer onHand = Objects.requireNonNull(responseEntity.getBody()).stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
        log.debug("getOnhandInventory onHand: " + onHand);
        return onHand;
    }
}
