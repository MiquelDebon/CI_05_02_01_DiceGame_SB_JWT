package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.GameMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IGameRepositoryMySQL;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RepositorySQLGameTest {
    /**
     * Test done with Junit
     * Arrange Act Assert
     * ID Autogenerated by Hibernate
     */

    @Autowired
    private IGameRepositoryMySQL gameMySQLRepository;


    PlayerMySQL playerMySQL1 = new PlayerMySQL("Miquel");
    PlayerMySQL playerMySQL2 = new PlayerMySQL("Daniel");
    GameMySQL gameMySQL_1 = new GameMySQL(10, playerMySQL1);
    GameMySQL gameMySQL_2 = new GameMySQL(7, playerMySQL1);
    GameMySQL gameMySQL_3 = new GameMySQL(3, playerMySQL2);
    GameMySQL gameMySQL_4 = new GameMySQL(5, playerMySQL2);


    @AfterEach
    public void SetAfter(){
        gameMySQLRepository.deleteAll();
    }

    @Test
    public void gameMySQLRepo_Save_ReturnGame(){

        GameMySQL savedGame = gameMySQLRepository.save(gameMySQL_1);
        List<GameMySQL> list = gameMySQLRepository.findAll();

        Assertions.assertThat(savedGame).isNotNull();
        Assertions.assertThat(savedGame.getId()).isGreaterThan(0);
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void gameMySQLRepo_GetAll_ListPlayers(){
        gameMySQLRepository.save(gameMySQL_1);
        gameMySQLRepository.save(gameMySQL_2);
        gameMySQLRepository.save(gameMySQL_3);


        List<GameMySQL> list = gameMySQLRepository.findAll();
        list.forEach(System.out::println);
        System.out.println(list.toString());
        log.warn(list.toString());

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void gameMySQLRepo_findByID_ReturnGame(){
        gameMySQLRepository.save(gameMySQL_1);
        int id = gameMySQL_1.getId();

        log.warn(String.valueOf(id));
        log.warn(gameMySQLRepository.findAll().toString());

        GameMySQL game = gameMySQLRepository.findById(id).get();

        Assertions.assertThat(game).isNotNull();
        Assertions.assertThat(game.getId()).isGreaterThan(0);
        Assertions.assertThat(game.getMark()).isEqualTo(10);
    }

    @Test
    public void gameMySQLRepo_deleteOne_ReturnIsEmpty(){
        gameMySQLRepository.save(gameMySQL_1);
        List<GameMySQL> previousList = gameMySQLRepository.findAll();

        gameMySQLRepository.deleteById(gameMySQL_1.getId());
        List<GameMySQL> postList = gameMySQLRepository.findAll();

        Assertions.assertThat(previousList.size()).isEqualTo(1);
        Assertions.assertThat(postList).isEmpty();
    }

    @Test
    public void gameMySQL_findGamesByPlayer_ResultGameList(){
        gameMySQLRepository.save(gameMySQL_1);    //player1
        gameMySQLRepository.save(gameMySQL_2);    //player1
        gameMySQLRepository.save(gameMySQL_3);    //player2

        List<GameMySQL> list1Game = gameMySQLRepository.findByPlayerId(playerMySQL1.getId());
        List<GameMySQL> list2Game = gameMySQLRepository.findByPlayerId(playerMySQL2.getId());

        Assertions.assertThat(list1Game.size()).isEqualTo(2);
        Assertions.assertThat(list2Game.size()).isEqualTo(1);
    }

    @Test
    public void gameMySQLRepo_deleteGamesByPlayer_Return(){
        gameMySQLRepository.save(gameMySQL_1);    //player1
        gameMySQLRepository.save(gameMySQL_2);    //player1...
        gameMySQLRepository.save(gameMySQL_3);    //player2

        List<GameMySQL> previousList_1 = gameMySQLRepository.findByPlayerId(playerMySQL1.getId());
        List<GameMySQL> previousList_2 = gameMySQLRepository.findByPlayerId(playerMySQL2.getId());

        gameMySQLRepository.deleteByPlayerId(playerMySQL1.getId());
        gameMySQLRepository.deleteByPlayerId(playerMySQL2.getId());
        List<GameMySQL> postList_1 = gameMySQLRepository.findByPlayerId(playerMySQL1.getId());
        List<GameMySQL> postList_2 = gameMySQLRepository.findByPlayerId(playerMySQL2.getId());


        Assertions.assertThat(previousList_1.size()).isEqualTo(2);
        Assertions.assertThat(postList_1.size()).isEqualTo(0);
        Assertions.assertThat(postList_1).isEmpty();

        Assertions.assertThat(previousList_2.size()).isEqualTo(1);
        Assertions.assertThat(postList_2.size()).isEqualTo(0);
        Assertions.assertThat(postList_2).isEmpty();

    }


}
