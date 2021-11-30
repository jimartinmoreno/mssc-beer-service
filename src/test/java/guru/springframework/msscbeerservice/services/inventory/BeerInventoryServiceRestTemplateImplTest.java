package guru.springframework.msscbeerservice.services.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import guru.sfg.brewery.model.BeerDto;
import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @RestClientTest Annotation for a Spring rest client test that focuses only on beans that use RestTemplateBuilder.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to rest client tests
 */
//@Disabled // utility for manual testing
@RestClientTest(BeerInventoryService.class)
@AutoConfigureJsonTesters
class BeerInventoryServiceRestTemplateImplTest {

    @Autowired
    BeerInventoryService beerInventoryService;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester <BeerInventoryDto[]> json;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        String detailsString = objectMapper.writeValueAsString(getValidBeerInventoryDtoList());
        this.server
                .expect(requestToUriTemplate("http://localhost:8082/api/v1/beer/{beerId}/inventory",
                        "45772dd4-3e82-4d49-9951-4b4d8d3a0a1b"))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }

    /**
     * Test unitario que simula la llamada al servicio rest remoto
     */

    @Test
    void getOnhandInventory() {
        //todo evolve to use UPC
        Integer qoh = beerInventoryService.getOnhandInventory(UUID.fromString("45772dd4-3e82-4d49-9951-4b4d8d3a0a1b"));
        assertThat(35).isEqualTo(qoh);
        System.out.println(qoh);
    }

    List<BeerInventoryDto> getValidBeerInventoryDtoList() {
        return List.of(BeerInventoryDto.builder()
                .id(UUID.fromString("7aa9d132-4c66-11ec-81d3-0242ac130003"))
                .beerId(UUID.fromString("45772dd4-3e82-4d49-9951-4b4d8d3a0a1b"))
                .quantityOnHand(35)
                .build());
    }

    /**
     * Es un test de integración que llama al servicio real desde un cliente TestRestTemplate
     */
    @Test
    void restTemplateClientInventoryTest() throws IOException {

        String result = "[{\"id\":\"7aa9d132-4c66-11ec-81d3-0242ac130003\",\"createdDate\":\"2021-11-23T16:01:00Z\",\"lastModifiedDate\":\"2021-11-23T16:01:00Z\",\"beerId\":\"45772dd4-3e82-4d49-9951-4b4d8d3a0a1b\",\"upc\":\"0083783375213\",\"quantityOnHand\":5}]";

        //@Autowired
        final TestRestTemplate template = new TestRestTemplate("good", "beer");
        //<BeerInventoryDto>
        ResponseEntity<BeerInventoryDto[]> responseEntity = template.getForEntity("http://localhost:8082/api/v1/beer/{beerId}/inventory", BeerInventoryDto[].class, "45772dd4-3e82-4d49-9951-4b4d8d3a0a1b");

        System.out.println("responseEntity = " + responseEntity);
        System.out.println("Body = " + responseEntity.getBody());
        System.out.println("Body Class= " + responseEntity.getBody().getClass());
        System.out.println("Body size= " + responseEntity.getBody().length);
        System.out.println("Body get= " + responseEntity.getBody()[0]);
        System.out.println("Body get class= " + responseEntity.getBody()[0].getClass());
        System.out.println("StatusCode = " + responseEntity.getStatusCode());
        System.out.println("ContentType = " + responseEntity.getHeaders().getContentType());
//        assertThat(responseEntity.responseEntity()).hasHost("other.example.com");

        this.json.write(responseEntity.getBody());

        String resultObtained = objectMapper.writeValueAsString(responseEntity.getBody());
        System.out.println("resultObtained = " + resultObtained);
        BeerInventoryDto[] beerInventoryDtos = objectMapper.readValue(resultObtained, BeerInventoryDto[].class);
        System.out.println("beerInventoryDtos = " + beerInventoryDtos[0]);

        assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().length).isPositive();
        assertThat(responseEntity.getBody()[0].getBeerId()).isEqualTo(UUID.fromString("45772dd4-3e82-4d49-9951-4b4d8d3a0a1b"));
        assertThat(responseEntity.getBody()[0].getQuantityOnHand()).isEqualTo(5);
        assertThat(result).isEqualTo(resultObtained);
        //assertThat("45772dd4-3e82-4d49-9951-4b4d8d3a0a1b").isEqualTo(JsonPath.read(resultObtained, "$.[0].id"));

        AssertionsForInterfaceTypes.assertThat(this.json.write(responseEntity.getBody())).hasJsonPathStringValue("@.[0].id");
        AssertionsForInterfaceTypes.assertThat(this.json.write(responseEntity.getBody())).extractingJsonPathNumberValue("@.[0].quantityOnHand").isEqualTo(5);

    }
}