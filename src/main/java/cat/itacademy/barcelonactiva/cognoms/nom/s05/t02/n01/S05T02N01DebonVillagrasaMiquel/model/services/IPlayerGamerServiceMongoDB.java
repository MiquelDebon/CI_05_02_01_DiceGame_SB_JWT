package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb.PlayerMongoDB;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface IPlayerGamerServiceMongoDB {

    List<PlayerGameDTO> getAllPlayersDTO();
    List<PlayerGameDTO> getAllPlayersDTORanking();
    PlayerGameDTO savePlayer(PlayerMongoDB player);
    PlayerGameDTO updatePlayer(PlayerMongoDB player);
    GameDTO saveGame(int id, int result);
    void deleteGamesByPlayerId(int id);
    Optional<PlayerGameDTO> findPlayerDTOById(int id);
    List<GameDTO> findGamesByPlayerId(int id) throws UserNotFoundException;
    Optional<PlayerGameDTO> getWorstPlayer();
    Optional<PlayerGameDTO> getBestPlayer();
    OptionalDouble averageTotalMarks();
}
