package guru.springframework.msscbeerservice.web.controller;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.bootstrap.BeerLoader;
import guru.springframework.msscbeerservice.services.beer.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.*;

/**
 * Test unitario de la capa MVC, solamente se prueba el controller y se mockea la respuesta del servicio
 *
 * @WebMvcTest >> If you want to focus only on the web layer and not start a complete ApplicationContext
 * <p>
 * Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to MVC tests
 * (i.e. @Controller, @ControllerAdvice, @JsonComponent, Converter/GenericConverter, Filter, WebMvcConfigurer
 * and HandlerMethodArgumentResolver beans but not @Component, @Service or @Repository beans).
 */

import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de integración que carga todo el contexto y prueba end to end el controlador con un servicio Mockeado
 */
@SpringBootTest
class BeerControllerMockTest {

    @Autowired
    BeerController controller;

    /**
     * @MockBean Annotation that can be used to add mocks to a Spring ApplicationContext. Can be used as a class level annotation
     * or on fields in either @Configuration classes, or test classes that are @RunWith the SpringRunner.
     */
    @MockBean
    BeerService beerService;

    @Test
    void getBeerById() throws Exception {
        given(beerService.getById(any(), anyBoolean())).willReturn(getValidBeerDto());

        ResponseEntity<BeerDto> responseEntity = controller.getBeerById(UUID.randomUUID(), false);
        then(beerService).should(times(1)).getById(any(), anyBoolean());
        assertThat("My Beer").isEqualTo(responseEntity.getBody().getBeerName());
    }

    BeerDto getValidBeerDto() {
        return BeerDto.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }
}