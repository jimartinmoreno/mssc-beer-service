package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.bootstrap.BeerLoader;
import guru.springframework.msscbeerservice.services.beer.BeerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test unitario de la capa MVC, solamente se prueba el controller y se mockea la respuesta del servicio
 *
 * @WebMvcTest >> If you want to focus only on the web layer and not start a complete ApplicationContext
 * <p>
 * Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to MVC tests
 * (i.e. @Controller, @ControllerAdvice, @JsonComponent, Converter/GenericConverter, Filter, WebMvcConfigurer
 * and HandlerMethodArgumentResolver beans but not @Component, @Service or @Repository beans).
 * <p>
 * auto-configures the Spring MVC infrastructure and limits scanned beans to @Controller, @ControllerAdvice, @JsonComponent,
 * Converter, GenericConverter, Filter, HandlerInterceptor, WebMvcConfigurer, WebMvcRegistrations, and
 * HandlerMethodArgumentResolver
 * <p>
 * Regular @Component and @ConfigurationProperties beans are not scanned when the @WebMvcTest annotation is used.
 * @EnableConfigurationProperties can be used to include @ConfigurationProperties beans.
 */
@WebMvcTest(controllers = {BeerController.class})
//@AutoConfigureRestDocs
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @MockBean Annotation that can be used to add mocks to a Spring ApplicationContext. Can be used as a class level annotation
     * or on fields in either @Configuration classes, or test classes that are @RunWith the SpringRunner.
     */
    @MockBean
    BeerService beerService;

    @Captor
    private ArgumentCaptor<PageRequest> pageRequestArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor
    private ArgumentCaptor<BeerStyleEnum> beerStyleEnumArgumentCaptor;

    @Test
    void getBeerById() throws Exception {

        given(beerService.getById(any(), anyBoolean())).willReturn(getValidBeerDto());

        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void saveNewBeer() throws Exception {

        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveNewBeer(any())).willReturn(getValidBeerDto());

        mockMvc.perform(post("/api/v1/beer/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isCreated());
        then(beerService).should(times(1)).saveNewBeer(any());
    }

    @Test
    void updateBeerById() throws Exception {
        given(beerService.updateBeer(any(), any())).willReturn(getValidBeerDto());

        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());
        then(beerService).should(times(1)).updateBeer(any(), any());
    }


    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any())).willReturn(getBeerPagedList());

        MvcResult result = mockMvc.perform(get("/api/v1/beer/")
                        .queryParam("pageNumber", "1")
                        .queryParam("pageSize", "10")
                        .queryParam("beerName", "xxxx")
                        .queryParam("beerStyle", BeerStyleEnum.PALE_ALE.toString())
                        .queryParam("showInventoryOnHand", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        System.out.println("result.getResponse().getContentAsString() = " + responseString);
        System.out.println("result.getResponse().getContentType() = " + result.getResponse().getContentType());
        System.out.println("result.getResponse().getStatus() = " + result.getResponse().getStatus());

        //BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
        verify(beerService, times(1))
                .listBeers(stringArgumentCaptor.capture(),
                        beerStyleEnumArgumentCaptor.capture(),
                        pageRequestArgumentCaptor.capture(),
                        booleanArgumentCaptor.capture());

        assertThat(pageRequestArgumentCaptor.getValue()).isNotNull();
        assertThat(PageRequest.of(1, 10)).isEqualTo(pageRequestArgumentCaptor.getValue());
        assertThat(stringArgumentCaptor.getValue()).isNotNull();
        assertThat("xxxx").isEqualTo(stringArgumentCaptor.getValue());
        assertThat(beerStyleEnumArgumentCaptor.getValue()).isNotNull();
        assertThat(BeerStyleEnum.PALE_ALE).isEqualTo(beerStyleEnumArgumentCaptor.getValue());
        assertThat(booleanArgumentCaptor.getValue()).isNotNull();
        assertThat(false).isEqualTo(booleanArgumentCaptor.getValue());
    }

    BeerDto getValidBeerDto() {
        return BeerDto.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }

    BeerPagedList getBeerPagedList() {
        List<BeerDto> content = new ArrayList<>();
        content.add(getValidBeerDto());
        return new BeerPagedList(content);
    }
}