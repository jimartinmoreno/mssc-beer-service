package guru.springframework.msscbeerservice.services.inventory.feign;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
///inventory-failover
@FeignClient(name = "inventory-failover")
public interface InventoryFailoverFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/inventory-failover")
    ResponseEntity<List<BeerInventoryDto>> getOnhandInventory();
}
