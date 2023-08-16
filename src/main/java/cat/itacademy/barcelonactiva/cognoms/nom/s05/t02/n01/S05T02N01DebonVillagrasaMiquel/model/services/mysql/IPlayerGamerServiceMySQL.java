package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface IPlayerGamerServiceMySQL {

    List<PlayerGameDTO> getAllPlayersDTO();
    List<PlayerGameDTO> getAllPlayersDTORanking();
    PlayerGameDTO updatePlayer(RegisterRequest updatedPlayer, String currentEmail);
    GameDTO saveGame(int id);
    List<GameDTO> deleteGamesByPlayerId(int id);
    PlayerGameDTO findPlayerDTOById(int id);
    List<GameDTO> findGamesByPlayerId(int id) throws UserNotFoundException;
    PlayerGameDTO getWorstPlayer();
    PlayerGameDTO getBestPlayer();
    Double averageTotalMarks();
}
