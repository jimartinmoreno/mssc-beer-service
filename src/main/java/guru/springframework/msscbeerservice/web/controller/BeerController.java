package guru.springframework.msscbeerservice.web.controller;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.services.beer.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by jt on 2019-05-12.
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = {"application/json"}, path = "beer")
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {

//        if (showInventoryOnHand == null) {
//            showInventoryOnHand = false;
//        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        log.debug("listBeers - beerList: " + beerList.getContent());
        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    @GetMapping("beer/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {
        //        if (showInventoryOnHand == null) {
        //            showInventoryOnHand = false;
        //        }
        log.info(" getBeerById - beerId: " + beerId);
        return new ResponseEntity<>(beerService.getById(beerId, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping("beerUpc/{upc}")
    public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
        log.info(" updateBeerById - upc: " + upc);
        return new ResponseEntity<>(beerService.getByUpc(upc), HttpStatus.OK);
    }

    @PostMapping(path = "beer")
    public ResponseEntity saveNewBeer(@RequestBody @Validated BeerDto beerDto, Errors errors) {
        log.info(" saveNewBeer - beerDto: " + beerDto);
        log.info(" saveNewBeer - errors: " + errors.hasErrors());
        errors.getAllErrors().forEach(System.out::println);
        return new ResponseEntity<>(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("beer/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto, Errors errors) {
        log.info(" updateBeerById - beerId: " + beerId);
        log.info(" updateBeerById - beerDto: " + beerDto);
        log.info(" updateBeerById - errors: " + errors.hasErrors());
        errors.getAllErrors().forEach(System.out::println);

        return new ResponseEntity<>(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
    }

}
