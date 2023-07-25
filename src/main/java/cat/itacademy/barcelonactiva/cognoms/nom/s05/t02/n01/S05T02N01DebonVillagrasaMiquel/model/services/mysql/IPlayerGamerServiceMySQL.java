package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface IPlayerGamerServiceMySQL {

    List<PlayerGameDTO> getAllPlayersDTO();
    List<PlayerGameDTO> getAllPlayersDTORanking();
    PlayerGameDTO savePlayer(PlayerMySQL player);
    PlayerGameDTO updatePlayer(PlayerMySQL player);
    GameDTO saveGame(int id, int result);
    void deleteGamesByPlayerId(int id);
    Optional<PlayerGameDTO> findPlayerDTOById(int id);
    List<GameDTO> findGamesByPlayerId(int id) throws UserNotFoundException;
    Optional<PlayerGameDTO> getWorstPlayer();
    Optional<PlayerGameDTO> getBestPlayer();
    OptionalDouble averageTotalMarks();
}
