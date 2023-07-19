package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface IPlayerGamerService {

    List<PlayerGameDTO> getAllPlayersDTO();
    List<PlayerGameDTO> getAllPlayersDTORanking();
    PlayerGameDTO savePlayer(Player player);
    PlayerGameDTO updatePlayer(Player player);
    GameDTO saveGame(int id, int result);
    void deleteGamesByPlayerId(int id);
    Optional<PlayerGameDTO> findPlayerDTOById(int id);
    List<GameDTO> findGamesByPlayerId(int id);
    Optional<PlayerGameDTO> getWorstPlayer();
    Optional<PlayerGameDTO> getBestPlayer();
    OptionalDouble averageTotalMarks();
}
