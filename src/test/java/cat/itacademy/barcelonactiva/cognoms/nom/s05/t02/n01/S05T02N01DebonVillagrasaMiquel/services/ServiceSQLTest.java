package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.GameMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IGameRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IplayerRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql.PlayerGamerServiceMySQLImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@DisplayName("Service test")
@ExtendWith(MockitoExtension.class)
public class ServiceSQLTest {

    @InjectMocks
    private PlayerGamerServiceMySQLImpl serviceMySQL;
    @Mock
    private IplayerRepositoryMySQL playerRepositorySQL;
    @Mock
    private IGameRepositoryMySQL gameRepositorySQL;
    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void playerSQLService_findByID_ReturnPlayerDTO(){
        PlayerMySQL playerExpected = new PlayerMySQL(1, "Miquel");

        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(playerRepositorySQL.findById(1)).thenReturn(Optional.of(playerExpected));
        Optional<PlayerGameDTO> playerReturned = serviceMySQL.findPlayerDTOById(1);

        Assertions.assertThat(playerExpected).isNotNull();
        Assertions.assertThat(playerExpected.getId()).isEqualTo(playerReturned.get().getId());
    }

    @Test
    public void playerSQLService_getAllPlayerDTO_ReturnListPlayersDTO(){
        PlayerMySQL player1 = new PlayerMySQL(1, "Miquel");
        PlayerMySQL player2 = new PlayerMySQL(2, "Marta");
        GameMySQL gameMySQL1 = new GameMySQL(1, player1);
        GameMySQL gameMySQL2 = new GameMySQL(2, player1);
        List<GameMySQL> games = Arrays.asList(gameMySQL1, gameMySQL2);

        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(playerRepositorySQL.existsById(2)).thenReturn(true);
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(games);
        Mockito.when(gameRepositorySQL.findByPlayerId(2)).thenReturn(new ArrayList<GameMySQL>());

        Mockito.when(playerRepositorySQL.findAll()).thenReturn(
                        Stream.of(player1, player2).collect(Collectors.toList()));
        List<PlayerGameDTO> actualList = serviceMySQL.getAllPlayersDTO();

        Assertions.assertThat(serviceMySQL.getAllPlayersDTO()).isNotEmpty();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
    }


    @Test
    public void playerSQLService_getAllPlayerDTO_ReturnException(){
        Mockito.when(playerRepositorySQL.findAll()).thenReturn(new ArrayList<PlayerMySQL>());
        assertThrows(EmptyDataBaseException.class, () -> {
            serviceMySQL.getAllPlayersDTO();
        });
    }


    @Test
    public void gamesSQLService_findGamesByPlayerId_ReturnGameList(){
        PlayerMySQL playerExpected = new PlayerMySQL(1, "Miquel");
        GameMySQL gameMySQL1 = new GameMySQL(1, playerExpected);
        GameMySQL gameMySQL2 = new GameMySQL(2, playerExpected);

        List<GameMySQL> games = Arrays.asList(gameMySQL1, gameMySQL2);

        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(games);

        serviceMySQL.findGamesByPlayerId(1);
        Assertions.assertThat(games.size()).isEqualTo(2);
        Assertions.assertThat(games).isNotEmpty();
    }


    @Test
    public void gameSQLService_saveGame_returnSavedGame(){
        PlayerMySQL playerExpected = new PlayerMySQL(1, "Miquel");
        GameMySQL gameMySQL1 = new GameMySQL(3, playerExpected);

        Mockito.when(playerRepositorySQL.findById(1)).thenReturn(Optional.of(playerExpected));
        Mockito.when(gameRepositorySQL.save(any(GameMySQL.class))).thenReturn(gameMySQL1);

        GameDTO actualGameDTO = serviceMySQL.saveGame(1);

        Assertions.assertThat(actualGameDTO).isNotNull();
    }

    @Test
    public void gameSQLService_saveGame_returnUserNotFoundException(){
        Mockito.when(playerRepositorySQL.findById(1))
                .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> {
            serviceMySQL.saveGame(1);
        });
    }

    @Test
    public void playerSQLService_getAllPlayerDTORanking_ReturnListPlayersDTO(){
        PlayerMySQL player1 = new PlayerMySQL(1, "Miquel");
        PlayerMySQL player2 = new PlayerMySQL(2, "Marta");
        GameMySQL gameMySQL1 = new GameMySQL(1, player1);
        GameMySQL gameMySQL2 = new GameMySQL(2, player1);
        List<GameMySQL> games = Arrays.asList(gameMySQL1, gameMySQL2);

        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(playerRepositorySQL.existsById(2)).thenReturn(true);
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(games);
        Mockito.when(gameRepositorySQL.findByPlayerId(2)).thenReturn(new ArrayList<GameMySQL>());

        Mockito.when(playerRepositorySQL.findAll()).thenReturn(
                Stream.of(player1, player2).collect(Collectors.toList()));
        List<PlayerGameDTO> actualList = serviceMySQL.getAllPlayersDTORanking();

        Assertions.assertThat(serviceMySQL.getAllPlayersDTO()).isNotEmpty();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
    }

    @Test
    public void playerSQLService_getAllPlayerDTORanking_ReturnException(){
        Mockito.when(playerRepositorySQL.findAll()).thenReturn(new ArrayList<PlayerMySQL>());
        assertThrows(EmptyDataBaseException.class, () -> {
            serviceMySQL.getAllPlayersDTORanking();
        });
    }


    @Test
    public void gameSQLService_deleteGameByPlayerId_ReturnVoid(){
        PlayerMySQL player = new PlayerMySQL(1, "Miquel");
        GameMySQL gameMySQL1 = new GameMySQL(1, player);
        GameMySQL gameMySQL2 = new GameMySQL(2, player);

        List<GameMySQL> games = Arrays.asList(gameMySQL1, gameMySQL2);

        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(gameRepositorySQL.deleteByPlayerId(1)).thenReturn(games);

        // Delete games
        serviceMySQL.deleteGamesByPlayerId(1);
        List<GameDTO> deletedGames = serviceMySQL.deleteGamesByPlayerId(1);

        // Verify that the saved game is not returned after deletion
        Assertions.assertThat(deletedGames.size()).isEqualTo(2);
    }

//    @Test
//    public void gamedelte2(){
//        Integer id = 1;
//        PlayerMySQL playerTest = new PlayerMySQL(id, "testPlayer");
//        for(int mark=1; mark<5; mark++){
//            GameMySQL gameTest = new GameMySQL(mark, playerTest);
//            Mockito.when(gameRepositorySQL.save(gameTest)).thenReturn(gameTest);
//            serviceMySQL.saveGame(id);
//        }
//        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
//
////        Mockito.when(gameRepositorySQL.findByPlayerId(id)).thenReturn(5);
//
//        org.junit.jupiter.api.Assertions.assertEquals(5,serviceMySQL.findGamesByPlayerId(id).size());
//        serviceMySQL.deleteGamesByPlayerId(id);
//        org.junit.jupiter.api.Assertions.assertEquals(0,serviceMySQL.findGamesByPlayerId(id).size());
//    }


    @Test
    public void playerSQLService_GetWorstPlayer_ReturnPlayerGameDTO(){
        PlayerMySQL player1 = new PlayerMySQL(1, "Miquel");
        PlayerMySQL player2 = new PlayerMySQL(2, "Marta");
        GameMySQL gameMySQL1 = new GameMySQL(1, player1);
        GameMySQL gameMySQL2 = new GameMySQL(10, player2);

        Mockito.when(playerRepositorySQL.findAll()).thenReturn(
                Stream.of(player1, player2).collect(Collectors.toList()));
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(
                Stream.of(gameMySQL1).collect(Collectors.toList()));
        Mockito.when(gameRepositorySQL.findByPlayerId(2)).thenReturn(
                Stream.of(gameMySQL2).collect(Collectors.toList()));

        Optional<PlayerGameDTO> player = serviceMySQL.getWorstPlayer();
        Assertions.assertThat(player).isNotEmpty();
        Assertions.assertThat(player.get().getId()).isEqualTo(1);
        Assertions.assertThat(player.get().getClass()).isEqualTo(PlayerGameDTO.class);
    }

    @Test
    public void playerSQLService_GetBestPlayer_ReturnPlayerGameDTO(){
        PlayerMySQL player1 = new PlayerMySQL(1, "Miquel");
        PlayerMySQL player2 = new PlayerMySQL(2, "Marta");
        GameMySQL gameMySQL1 = new GameMySQL(1, player1);
        GameMySQL gameMySQL2 = new GameMySQL(10, player2);

        Mockito.when(playerRepositorySQL.findAll()).thenReturn(
                Stream.of(player1, player2).collect(Collectors.toList()));
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(
                Stream.of(gameMySQL1).collect(Collectors.toList()));
        Mockito.when(gameRepositorySQL.findByPlayerId(2)).thenReturn(
                Stream.of(gameMySQL2).collect(Collectors.toList()));

        Optional<PlayerGameDTO> player = serviceMySQL.getBestPlayer();
        Assertions.assertThat(player).isNotEmpty();
        Assertions.assertThat(player.get().getId()).isEqualTo(2);
    }

//    @Deprecated
//    @Test
//    public void playerSQLService_updatePlayer_ReturnUpdatedPlayer(){
//        PlayerMySQL oldPlayer = new PlayerMySQL(1, "Miquel");
//        PlayerMySQL updatedPlayer = new PlayerMySQL(1, "Marta");
//
//        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
//        AuthenticationMySQLService authenticationMySQLService = mock(AuthenticationMySQLService.class);
//
//        Mockito.when(authenticationMySQLService.checkDuplicatedName(updatedPlayer.getName()));
//
//    }


}
