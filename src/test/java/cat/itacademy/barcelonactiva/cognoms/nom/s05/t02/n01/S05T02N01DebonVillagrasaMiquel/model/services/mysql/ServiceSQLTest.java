package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.GameMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IGameRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IplayerRepositoryMySQL;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;

@Slf4j
@DisplayName("Service test")
@ExtendWith(MockitoExtension.class)
public class ServiceSQLTest {

    @InjectMocks
    private PlayerGamerServiceMySQLImpl underTestService;
    @Mock private IplayerRepositoryMySQL playerRepositorySQL;
    @Mock private IGameRepositoryMySQL gameRepositorySQL;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock AuthenticationMySQLService authenticationMySQLService;


    private PlayerMySQL player1;
    private PlayerMySQL player2;
    private GameMySQL game1;
    private GameMySQL game2;
    private List<GameMySQL> games ;
    private List<PlayerMySQL> listPlayerMySQL;
    private List<GameMySQL> listGameDTO;
    private RegisterRequest registerRequest;

    @BeforeEach
    public void setUp(){
        player1 = new PlayerMySQL(1, "Miquel");
        player2 = new PlayerMySQL(2, "Manolo");

        game1 = new GameMySQL(1, player1);
        game2 = new GameMySQL(2, player1);
        games = Arrays.asList(game1, game2);

        listPlayerMySQL = Arrays.asList(
                new PlayerMySQL(1, "Miquel"),
                new PlayerMySQL(2, "Marta"),
                new PlayerMySQL(3, "Jorge"));

        listGameDTO = Arrays.asList(
                new GameMySQL(1, 3, player1),
                new GameMySQL(2, 5, player1));

        registerRequest = new RegisterRequest("Miquel", "Debon",
                "mdebonbcn@gmail.com", "password");
    }


    @Test
    public void playerSQLService_findByID_ReturnPlayerDTO(){
        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(playerRepositorySQL.findById(1)).thenReturn(Optional.of(player1));
        Optional<PlayerGameDTO> playerReturned = underTestService.findPlayerDTOById(1);

        Assertions.assertThat(player1).isNotNull();
        Assertions.assertThat(player1.getId()).isEqualTo(playerReturned.get().getId());
    }

    @Test
    public void gameSQLService_finPlayerByPlayerId_ReturnException(){
        Mockito.when(playerRepositorySQL.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            underTestService.findPlayerDTOById(anyInt());
        });
    }

    @Test
    public void playerSQLService_getAllPlayerDTO_ReturnListPlayersDTO(){
        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(playerRepositorySQL.existsById(2)).thenReturn(true);
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(games);
        Mockito.when(gameRepositorySQL.findByPlayerId(2)).thenReturn(new ArrayList<GameMySQL>());

        Mockito.when(playerRepositorySQL.findAll()).thenReturn(
                        Stream.of(player1, player2).collect(Collectors.toList()));
        List<PlayerGameDTO> actualList = underTestService.getAllPlayersDTO();

        Assertions.assertThat(underTestService.getAllPlayersDTO()).isNotEmpty();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
    }


    @Test
    public void playerSQLService_getAllPlayerDTO_ReturnException(){
        Mockito.when(playerRepositorySQL.findAll()).thenReturn(new ArrayList<PlayerMySQL>());
        assertThrows(EmptyDataBaseException.class, () -> {
            underTestService.getAllPlayersDTO();
        });
    }


    @Test
    public void gamesSQLService_findGamesByPlayerId_ReturnGameList(){
        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(gameRepositorySQL.findByPlayerId(1)).thenReturn(games);

        underTestService.findGamesByPlayerId(1);
        Assertions.assertThat(games.size()).isEqualTo(2);
        Assertions.assertThat(games).isNotEmpty();
    }

    @Test
    public void gamesSQLService_findGamesByPlayerId_returnException(){
        Mockito.when(playerRepositorySQL.existsById(anyInt())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                underTestService.findGamesByPlayerId(anyInt()));
    }


    @Test
    public void gameSQLService_saveGame_returnSavedGame(){
        Mockito.when(playerRepositorySQL.findById(1)).thenReturn(Optional.of(player1));
        Mockito.when(gameRepositorySQL.save(any(GameMySQL.class))).thenReturn(game1);

        GameDTO actualGameDTO = underTestService.saveGame(1);

        Assertions.assertThat(actualGameDTO).isNotNull();
    }

    @Test
    public void gameSQLService_saveGame_returnUserNotFoundException(){
        Mockito.when(playerRepositorySQL.findById(1))
                .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> {
            underTestService.saveGame(1);
        });

        Mockito.verify(gameRepositorySQL, never()).save(any());
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
        List<PlayerGameDTO> actualList = underTestService.getAllPlayersDTORanking();

        Assertions.assertThat(underTestService.getAllPlayersDTO()).isNotEmpty();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
    }
    @Test
    public void playerSQLService_getAllPlayerDTORanking_ReturnException(){
        Mockito.when(playerRepositorySQL.findAll()).thenReturn(new ArrayList<PlayerMySQL>());
        assertThrows(EmptyDataBaseException.class, () -> {
            underTestService.getAllPlayersDTORanking();
        });
    }


    @Test
    public void gameSQLService_deleteGameByPlayerId_ReturnListGameDTO(){
        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(gameRepositorySQL.deleteByPlayerId(1)).thenReturn(games);

        // Delete games
        underTestService.deleteGamesByPlayerId(1);
        List<GameDTO> deletedGames = underTestService.deleteGamesByPlayerId(1);

        // Verify
        Assertions.assertThat(deletedGames.size()).isEqualTo(2);
        Mockito.verify(gameRepositorySQL, Mockito.times(2)).deleteByPlayerId(1);
    }
    @Test
    public void gameSQLService_deleteGamesByPlayerId_returnException(){
        Mockito.when(playerRepositorySQL.existsById(anyInt())).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                ()-> underTestService.deleteGamesByPlayerId(anyInt()));
    }


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

        Optional<PlayerGameDTO> player = underTestService.getWorstPlayer();
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

        Optional<PlayerGameDTO> player = underTestService.getBestPlayer();
        Assertions.assertThat(player).isNotEmpty();
        Assertions.assertThat(player.get().getId()).isEqualTo(2);
    }

    @Test
    public void playerSQL_getAverageTotalMarks_returnOptionalDouble(){
        Mockito.when(playerRepositorySQL.findAll()).thenReturn(listPlayerMySQL);
        Mockito.when(playerRepositorySQL.existsById(anyInt())).thenReturn(true);
        Mockito.when(gameRepositorySQL.findByPlayerId(anyInt())).thenReturn(listGameDTO);

        OptionalDouble optionalDouble = underTestService.averageTotalMarks();

        Assertions.assertThat(optionalDouble).isNotEmpty();
    }


    @Test
    public void playerSQLService_updatePlayer_ReturnUpdatedPlayer(){
        Mockito.when(playerRepositorySQL.findByEmail(anyString())).thenReturn(Optional.of(player1));
        Mockito.when(playerRepositorySQL.existsById(1)).thenReturn(true);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        underTestService.updatePlayer(registerRequest, "email");
    }


}
