package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel._integral;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.mysql.DiceControllerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql.PlayerGamerServiceMySQLImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = DiceControllerMySQL.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class IntegralDiceGameControllerSQL {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlayerGamerServiceMySQLImpl service;
    @Autowired
    private ObjectMapper objectMapper;

    private PlayerMySQL playerMySQL;
    private PlayerGameDTO playerGameDTO;
    private GameDTO gameDTO;
    @BeforeEach
    public void init(){
        playerMySQL = PlayerMySQL.builder().name("Miquelee").build();
        playerGameDTO = PlayerGameDTO.builder().id(1).name("Miqueluu").averageMark(2).build();
        gameDTO = new GameDTO(3);

    }


//    @Test
//    public void diceController_playGame_ReturnGameDTO() throws Exception{
//        given(service.saveGame(ArgumentMatchers.any()));
//
//        ResultActions response = mockMvc.perform(post("/players/1/games")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsBytes(gameDTO)));
//
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//
//    }


}
