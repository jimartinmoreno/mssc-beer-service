package guru.springframework.msscbeerservice.services.inventory.feign;

import guru.springframework.msscbeerservice.config.FeignClientConfig;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryServiceConstants;
import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

/**
 * @FeignClient Annotation for interfaces declaring that a REST client with that interface should be created
 * (e.g. for autowiring into another component). If SC LoadBalancer is available it will be used to load balance
 * the backend requests, and the load balancer can be configured using the same name (i.e. value) as the feign client.
 */
@FeignClient(name = "inventory-service",
        fallback = BeerInventoryServiceFeignClientFailover.class,
        configuration = FeignClientConfig.class)

// En esta configuración se usa el gateway
// url an absolute URL or resolvable hostname (the protocol is optional).
// @FeignClient(name = "inventory-service", url = "http://localhost:9090", fallback = BeerInventoryServiceFeignClientFailover.class,
//        configuration = FeignClientConfig.class)

public interface InventoryServiceFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = BeerInventoryServiceConstants.INVENTORY_PATH)
    ResponseEntity<List<BeerInventoryDto>> getOnhandInventory(@PathVariable UUID beerId);
}
