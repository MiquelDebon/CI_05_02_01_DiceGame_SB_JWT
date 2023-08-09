package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mongodb;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mongodb.GameDTOMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mongodb.PlayerGameDTOMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb.PlayerMongoDB;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface IPlayerGamerServiceMongoDB {

    List<PlayerGameDTOMongoDB> getAllPlayersDTO();
    List<PlayerGameDTOMongoDB> getAllPlayersDTORanking();
    PlayerGameDTOMongoDB updatePlayer(RegisterRequest player, String username);
    GameDTOMongoDB saveGame(String id);
    void deleteGamesByPlayerId(String id);
    Optional<PlayerGameDTOMongoDB> findPlayerDTOById(String id);
    List<GameDTOMongoDB> findGamesByPlayerId(String id) throws UserNotFoundException;
    Optional<PlayerGameDTOMongoDB> getWorstPlayer();
    Optional<PlayerGameDTOMongoDB> getBestPlayer();
    OptionalDouble averageTotalMarks();
}
