package guru.springframework.msscbeerservice.services.beer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

//@ActiveProfiles(value = {"localmysql"})
//@ComponentScan(basePackages = {"guru.springframework.msscbeerservice.services.beer",
//        "guru.springframework.msscbeerservice.services.inventory",
//        "guru.springframework.msscbeerservice.web.mappers"})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// @DataJpaTest(showSql = true)

/**
 * @AutoConfigureWebClient Annotation that can be applied to a test class to enable and configure auto-configuration of web clients.
 */
@AutoConfigureWebClient

/**
 * @AutoConfigureMockRestServiceServer Annotation that can be applied to a test class to enable and configure auto-configuration of a single MockRestServiceServer.
 * Only useful when a single call is made to RestTemplateBuilder.
 */
@AutoConfigureMockRestServiceServer
@Slf4j
//@Disabled
@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @BeforeEach
    void setUp() {
        log.info("beerService: " + beerService);
    }

    @Test
    void listBeers() {
        assertThat(1).isEqualTo(beerService.listBeers("Mango Bobs", false).size());
    }

    @Test
    void getById() {
    }

    @Test
    void saveNewBeer() {
    }

    @Test
    void updateBeer() {
    }

    @Test
    void getByUpc() {
    }
}