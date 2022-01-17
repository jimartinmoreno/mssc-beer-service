package guru.springframework.msscbeerservice.services.beer;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    /**
     * @Cacheable Annotation indicating that the result of invoking a method (or all methods in a class) can be cached.
     */
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {

        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if (!ObjectUtils.isEmpty(beerName) && !ObjectUtils.isEmpty(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (!ObjectUtils.isEmpty(beerName) && ObjectUtils.isEmpty(beerStyle)) {
            //search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (ObjectUtils.isEmpty(beerName) && !ObjectUtils.isEmpty(beerStyle)) {
            //search beer_service style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventoryOnHand) {
            beerPagedList = new BeerPagedList(beerPage.getContent().stream()
                    .map(beerMapper::beerToBeerDtoWithInventory).toList(),
                    //                    PageRequest.of(beerPage.getPageable().getPageNumber(), beerPage.getPageable().getPageSize()),
                    beerPage.getPageable(), beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage.getContent().stream().map(beerMapper::beerToBeerDto).toList(),
                    //                    PageRequest.of(beerPage.getPageable().getPageNumber(), beerPage.getPageable().getPageSize()),
                    beerPage.getPageable(), beerPage.getTotalElements());
        }
        log.debug("listBeers - beerPagedList: " + beerPagedList.getContent());
        return beerPagedList;
    }

    @Override
    public List<BeerDto> listBeers(String beerName, Boolean showInventoryOnHand) {
        log.debug("listBeers - beerName: " + beerName);
        List<Beer> beerList = beerRepository.findAllByBeerName(beerName);
        List<BeerDto> beerDtoList = null;
        if (showInventoryOnHand) {
            beerDtoList = beerList.stream()
                    .map(beerMapper::beerToBeerDtoWithInventory).toList();
        } else {
            beerDtoList = beerList.stream()
                    .map(beerMapper::beerToBeerDto).toList();
        }
        log.debug("listBeers - beerDtoList: " + beerDtoList);
        return beerDtoList;
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        log.debug("getById - beerId: " + beerId);
        log.debug("getById - showInventoryOnHand: " + showInventoryOnHand);
        if (showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(beerRepository.findById(beerId)
                    .orElseThrow(NotFoundException::new));
        } else {
            log.debug("getById - beerRepository.findById: " + beerRepository.findById(beerId));
            return beerMapper.beerToBeerDto(beerRepository.findById(beerId)
                    .orElseThrow(NotFoundException::new));
        }
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        log.debug("saveNewBeer - beerDto: " + beerDto);
        Beer beerTosave = beerMapper.beerDtoToBeer(beerDto);
        beerTosave.setMinOnHand(0);
        beerTosave.setQuantityToBrew(0);
        return beerMapper.beerToBeerDto(beerRepository.save(beerTosave));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        log.debug("updateBeer - beerId: " + beerId);
        log.debug("updateBeer - beerDto: " + beerDto);
        Beer beer = beerRepository.findById(beerId)
                .orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public BeerDto getByUpc(String upc) {
        log.debug("getByUpc - upc: " + upc);
        return beerMapper.beerToBeerDto(beerRepository.findByUpc(upc));
    }
}
