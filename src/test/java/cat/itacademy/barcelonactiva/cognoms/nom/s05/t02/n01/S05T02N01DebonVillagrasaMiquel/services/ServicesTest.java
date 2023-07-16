package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IGameRepository;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IplayerRepository;
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
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTest {

    @Autowired
    private PlayerGamerService service;
    @MockBean
    private IGameRepository gameRepository;
    @MockBean
    private IplayerRepository playerRepository;

    @Test
    public void getPlayer(){
        when(playerRepository.findAll()).thenReturn(
                Stream.of(new Player(1, "testName1"), new Player(2, "testName2"))
                        .collect(Collectors.toList()));
        assertEquals(2, service.getAllPlayersDTORanking().size());
        assertEquals("testName1",service.getAllPlayersDTORanking().get(0).getName());
    }

    @Test
    public void getOnePlayer(){
        when(playerRepository.findById(1)).thenReturn(
                Optional.of(new Player(1, "testName1")));
        assertEquals("testName1", service.findPlayerById(1).getName());
    }

    @Test
    public void getGamesById(){
        Player playerTest = new Player(1, "testPlayer");
        when(gameRepository.findByPlayerId(1)).thenReturn(
                Stream.of(new Game(1, 5, playerTest), new Game(2, 10, playerTest))
                        .collect(Collectors.toList()));
        System.out.println(service.findGamesByPlayerId(1));
        assertEquals(2, service.findGamesByPlayerId(1).size());
        assertEquals("LOSE", service.findGamesByPlayerId(1).get(0).getMessage());
        assertEquals("WIN", service.findGamesByPlayerId(1).get(1).getMessage());
    }

    @Test
    public void savePLayer(){
        Player playerTest = new Player(1, "testPlayer");
        PlayerGameDTO returnDTO = service.playerDTOfromPlayer(playerTest);
        when(playerRepository.save(playerTest)).thenReturn(playerTest);
        assertEquals(returnDTO, service.savePlayer(playerTest));
    }

    @Test
    public void saveGame(){
        Player playerTest = new Player(1, "testPlayer");
        int mark = 2;
        Game gameTest = new Game(mark, playerTest);
        GameDTO returnGameDTO = service.gameDTOfromGame(gameTest);
        when(gameRepository.save(gameTest)).thenReturn(gameTest);
        assertEquals(returnGameDTO, service.saveGame(playerTest, mark));
    }

    @Test
    public void deleteGamesByPlayerId(){
        Integer id = 1;
        Player playerTest = new Player(id, "testPlayer");
        for(int mark=1; mark<5; mark++){
            Game gameTest = new Game(mark, playerTest);
            when(gameRepository.save(gameTest)).thenReturn(gameTest);
            service.saveGame(playerTest, mark);
        }
//        when(gameRepository.findByPlayerId(id)).thenReturn(5);
//
//        assertEquals(5,service.findGamesByPlayerId(id).size());
        service.deleteGamesByPlayerId(id);
        assertEquals(0,service.findGamesByPlayerId(id).size());
    }

}
