package guru.springframework.msscbeerservice.web.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de integración que prueba el flujo completo
 * @AutoConfigureMockMvc Annotation that can be applied to a test class to enable and configure auto-configuration of MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles(value = {"localmysql"})
class BeerControllerAutoConfigureMockMvcTestIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/0a818933-087d-47f2-ad83-2f986ed087eb")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveNewBeer() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(post("/api/v1/beer/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isCreated());
    }


    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(put("/api/v1/beer/0a818933-087d-47f2-ad83-2f986ed087eb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());
    }

    BeerDto getValidBeerDto() {
        return BeerDto.builder()
                .beerName("Pinball Porter")
                .beerStyle(BeerStyleEnum.PORTER)
                .price(new BigDecimal("12.99"))
                .upc(getUpc())
                .quantityOnHand(199)
                .build();
    }

    private String getUpc() {
        int leftLimit = 0; // letter 'a'
        int rightLimit = 9; // letter 'z'
        int targetStringLength = 13;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return generatedString;
    }
}