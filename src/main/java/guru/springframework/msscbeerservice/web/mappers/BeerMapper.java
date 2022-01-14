package guru.springframework.msscbeerservice.web.mappers;

import guru.sfg.brewery.model.BeerDto;
import guru.springframework.msscbeerservice.domain.Beer;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * @DecoratedWith
 * Specifies a decorator to be applied to a generated mapper, which e.g. can be used to amend mappings performed by generated
 * mapping methods.
 * A typical decorator implementation will be an abstract class and only implement/override a subset of the methods of the
 * mapper type which it decorates. All methods not implemented or overridden by the decorator will be implemented by the code
 * generator by delegating to the generated mapper implementation.
 */
@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerMapperDecorator.class)
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    BeerDto beerToBeerDtoWithInventory(Beer beer);

    Beer beerDtoToBeer(BeerDto dto);
}
