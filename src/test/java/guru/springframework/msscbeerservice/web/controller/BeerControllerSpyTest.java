package guru.springframework.msscbeerservice.web.controller;

import guru.sfg.brewery.model.BeerDto;
import guru.springframework.msscbeerservice.services.beer.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Test de integración que carga todo el contexto y prueba end to end el controlador con el servicio real
 */
//@SpyBeans({
//        @SpyBean(BeerService.class),
//})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BeerControllerSpyTest {

    @Autowired
    BeerController controller;

    //@Autowired
    @SpyBean
    BeerService beerService;

    @Test
    void getRealBeerById() throws Exception {
        ResponseEntity<BeerDto> responseEntity = controller.getBeerById(UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb"), false);
        assertThat("Mango Bobs").isEqualTo(responseEntity.getBody().getBeerName());
    }
}