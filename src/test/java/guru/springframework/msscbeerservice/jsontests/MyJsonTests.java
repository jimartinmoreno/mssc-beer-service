package guru.springframework.msscbeerservice.jsontests;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Annotation for a JSON test that focuses only on JSON serialization.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to JSON tests (i.e. @JsonComponent, Jackson Module)
 * By default, tests annotated with JsonTest will also initialize JacksonTester, JsonbTester and GsonTester fields.
 *
 * More fine-grained control can be provided via the @AutoConfigureJsonTesters annotation.
 *
 * When using JUnit 4, this annotation should be used in combination with @RunWith(SpringRunner.class).
 */
@JsonTest
class MyJsonTests {

    @Autowired
    private JacksonTester<BeerDto> json;

    @Test
    void serialize() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        JsonContent<BeerDto> jsonContent = this.json.write(beerDto);
        // Assert against a `.json` file in the same package as the test
        assertThat(jsonContent).isEqualToJson(new ClassPathResource("beer.json"));
        // Or use JSON path based assertions
        assertThat(jsonContent).hasJsonPathStringValue("@.id");
        assertThat(jsonContent).extractingJsonPathStringValue("@.beerName").isEqualTo("My Beer");
    }

    @Test
    void deserialize() throws Exception {
        String content = "{\"id\":\"1361c7e3-be5e-4811-96db-3920bc166c37\",\"version\":0,\"createdDate\":null,\"lastModifiedDate\":null,\"beerName\":\"My Beer\",\"beerStyle\":\"ALE\",\"upc\":\"8631863391731\",\"price\":\"12.99\",\"quantityOnHand\":10}";
        ObjectContent<BeerDto> objectContent = this.json.parse(content);
        assertThat(objectContent).isEqualTo(getValidBeerDto());
        assertThat(objectContent.getObject().getBeerName()).isEqualTo("My Beer");

        BeerDto beerDto = this.json.parseObject(content);
        assertThat(beerDto.getBeerName()).isEqualTo("My Beer");
        assertThat(beerDto.getQuantityOnHand()).isEqualTo(10);
    }

    BeerDto getValidBeerDto() {
        return BeerDto.builder()
                .beerName("My Beer")
                .version(0)
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("12.99"))
                .upc("8631863391731")
                .quantityOnHand(10)
                .id(UUID.fromString("1361c7e3-be5e-4811-96db-3920bc166c37"))
                .build();
    }

}