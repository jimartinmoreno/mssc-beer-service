package guru.springframework.msscbeerservice.services.inventory.feign;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Component
@Slf4j
public class BeerInventoryServiceFeignClientFailover implements InventoryServiceFeignClient {

    private final InventoryFailoverFeignClient failoverFeignClient;

    @Override
    public ResponseEntity<List<BeerInventoryDto>> getOnhandInventory(UUID beerId) {
        log.info("getOnhandInventory - beerId: " + beerId);
        ResponseEntity<List<BeerInventoryDto>> responseEntity = failoverFeignClient.getOnhandInventory();
        log.info("getOnhandInventory - list: " + responseEntity.getBody());
        return responseEntity;
    }
}
