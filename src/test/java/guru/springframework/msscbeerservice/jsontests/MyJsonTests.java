package guru.springframework.msscbeerservice.jsontests;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.bootstrap.BeerLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class MyJsonTests {

    @Autowired
    private JacksonTester<BeerDto> json;

    @Test
    void serialize() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        // Assert against a `.json` file in the same package as the test
        assertThat(this.json.write(beerDto)).isEqualToJson(new ClassPathResource("beer.json"));
        // Or use JSON path based assertions
        assertThat(this.json.write(beerDto)).hasJsonPathStringValue("@.id");
        assertThat(this.json.write(beerDto)).extractingJsonPathStringValue("@.beerName").isEqualTo("My Beer");
    }

    @Test
    void deserialize() throws Exception {
        String content = "{\"id\":\"1361c7e3-be5e-4811-96db-3920bc166c37\",\"version\":0,\"createdDate\":null,\"lastModifiedDate\":null,\"beerName\":\"My Beer\",\"beerStyle\":\"ALE\",\"upc\":\"8631863391731\",\"price\":\"12.99\",\"quantityOnHand\":10}";
        assertThat(this.json.parse(content)).isEqualTo(getValidBeerDto());
        assertThat(this.json.parseObject(content).getBeerName()).isEqualTo("My Beer");
        assertThat(this.json.parseObject(content).getQuantityOnHand()).isEqualTo(10);
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