package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.GameMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IGameRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IplayerRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql.PlayerGamerServiceMySQLImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTest {

    @Autowired
    private PlayerGamerServiceMySQLImpl serviceMySQL;
    @MockBean
    private IGameRepositoryMySQL gameRepository;
    @MockBean
    private IplayerRepositoryMySQL playerRepository;

    @Test
    public void getPlayers(){
        when(playerRepository.findAll()).thenReturn(
                Stream.of(new PlayerMySQL(1, "testName1"), new PlayerMySQL(2, "testName2"))
                        .collect(Collectors.toList()));
        assertEquals(2, serviceMySQL.getAllPlayersDTO().size());
        assertEquals(2, serviceMySQL.getAllPlayersDTORanking().size());
        assertEquals("testName1", serviceMySQL.getAllPlayersDTORanking().get(0).getName());
    }

    @Test
    public void getPlayersException(){
        when(playerRepository.findAll()).thenReturn(null);
        assertThrows(EmptyDataBaseException.class, () -> {
            serviceMySQL.getAllPlayersDTO();
        });
    }

    @Test
    public void getOnePlayer(){
        when(playerRepository.findById(1)).thenReturn(
                Optional.of(new PlayerMySQL(1, "testName1")));
        assertEquals("testName1", serviceMySQL.findPlayerDTOById(1).get().getName());
    }

    @Test
    public void getGamesById(){
//        Player playerTest = new Player(1, "testPlayer");
//        when(gameRepository.findByPlayerId(1)).thenReturn(
//                Stream.of(new Game(1, 5, playerTest), new Game(2, 10, playerTest))
//                        .collect(Collectors.toList()));
//        System.out.println(service.findGamesByPlayerId(1));
//        assertEquals(2, service.findGamesByPlayerId(1).size());
//        assertEquals("LOSE", service.findGamesByPlayerId(1).get(0).getMessage());
//        assertEquals("WIN", service.findGamesByPlayerId(1).get(1).getMessage());
    }

    @Test
    public void savePLayer(){
        PlayerMySQL playerTest = new PlayerMySQL(1, "testPlayer");
        PlayerGameDTO returnDTO = serviceMySQL.playerDTOfromPlayer(playerTest);
        when(playerRepository.save(playerTest)).thenReturn(playerTest);
        assertEquals(returnDTO, serviceMySQL.savePlayer(playerTest));
    }

    @Test
    public void saveGame(){
        PlayerMySQL playerTest = new PlayerMySQL(1, "testPlayer");
        int mark = 2;
        int id = 1;
        GameMySQL gameTest = new GameMySQL(mark, playerTest);
        GameDTO returnGameDTO = serviceMySQL.gameDTOfromGame(gameTest);
        when(gameRepository.save(gameTest)).thenReturn(gameTest);
        assertEquals(returnGameDTO, serviceMySQL.saveGame(id, mark));
    }

    @Test
    public void deleteGamesByPlayerIdold(){
        Integer id = 1;
        PlayerMySQL playerTest = new PlayerMySQL(id, "testPlayer");
        for(int mark=1; mark<5; mark++){
            GameMySQL gameTest = new GameMySQL(mark, playerTest);
            when(gameRepository.save(gameTest)).thenReturn(gameTest);
            serviceMySQL.saveGame(id, mark);
        }
//        when(gameRepository.findByPlayerId(id)).thenReturn(5);
//
//        assertEquals(5,service.findGamesByPlayerId(id).size());
//        service.deleteGamesByPlayerId(id);
//        assertEquals(0,service.findGamesByPlayerId(id).size());
    }

    @Test
    public void deleteGamesByPlayerId(){
        Integer id = 1;
        serviceMySQL.deleteGamesByPlayerId(id);
        verify(gameRepository, times(1)).deleteByPlayerId(id);
    }

}
