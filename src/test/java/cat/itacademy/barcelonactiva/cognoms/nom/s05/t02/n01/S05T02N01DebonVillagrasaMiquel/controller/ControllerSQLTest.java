package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.mysql.DiceControllerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql.PlayerGamerServiceMySQLImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DiceControllerMySQL.class)
@AutoConfigureMockMvc
public class ControllerSQLTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerGamerServiceMySQLImpl service;

//    @Test
//    public void getAllPlayers() throws  Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9005/players")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//    }
}
