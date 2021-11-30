package guru.springframework.msscbeerservice.services.beer;

import guru.springframework.msscbeerservice.services.beer.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.context.annotation.ComponentScan;

//@ActiveProfiles(value = {"localmysql"})
@ComponentScan(basePackages = {"guru.springframework.msscbeerservice.services.beer",
        "guru.springframework.msscbeerservice.services.inventory",
        "guru.springframework.msscbeerservice.web.mappers"})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest(showSql = true)
@AutoConfigureWebClient
@AutoConfigureMockRestServiceServer
@Slf4j
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @BeforeEach
    void setUp() {
        log.info("beerService: " + beerService);
    }

    @Test
    void listBeers() {
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