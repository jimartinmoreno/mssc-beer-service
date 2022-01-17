package guru.springframework.msscbeerservice.bootstrap;

import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Solo se ejecuta si no hay beers en la BD creados con el script data.sql
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final String PRICE = "12.95";

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("run beer count: " + beerRepository.count());
        if (beerRepository.count() == 0) { // Check there is no beers in the repository
            loadBeerObjects();
        }
    }

    private void loadBeerObjects() {
        log.info("running loadBeerObjects: " + beerRepository.count());
        Beer b1 = Beer.builder()
                .beerName("Mango Bobs")
                .beerStyle(BeerStyleEnum.IPA.name())
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal(PRICE))
                .upc(BEER_1_UPC)
                .build();

        Beer b2 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyleEnum.PALE_ALE.name())
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal(PRICE))
                .upc(BEER_2_UPC)
                .build();

        Beer b3 = Beer.builder()
                .beerName("Pinball Porter")
                .beerStyle(BeerStyleEnum.PALE_ALE.name())
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal(PRICE))
                .upc(BEER_3_UPC)
                .build();

        b1 = beerRepository.save(b1);
        b2 = beerRepository.save(b2);
        b3 = beerRepository.save(b3);

        log.info("Created beer : " + b1);
        log.info("Created beer : " + b2);
        log.info("Created beer : " + b3);
    }
}
