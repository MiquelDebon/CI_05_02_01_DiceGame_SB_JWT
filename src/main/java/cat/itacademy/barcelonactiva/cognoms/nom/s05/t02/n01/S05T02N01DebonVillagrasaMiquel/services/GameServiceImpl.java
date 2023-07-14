package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl  {
    @Autowired
    private IGameRepository gameRepository;


    public List<Game> findGamesByPlayerId(int id){
        return gameRepository.findByPlayerId(id);
    }

    public Game saveGame(Player player, int result){
        return gameRepository.save(new Game(null, result, player));
    }

    public double averageMarkPLayer(int idPlayer){
        List<Game> games = findGamesByPlayerId(idPlayer);
        return  Math.round((games.stream()
                .mapToDouble(Game::getMark)
                .average()
                .orElse(Double.NaN)) * 100.00) / 100.00;
    }

    /**
     *
     */

    public List<Game> findAll(){
        return gameRepository.findAll();
    }

    public Optional<Game> findById(int id){
        return gameRepository.findById(id);
    }

    public List<Game> deleteByPlayerId(int id){
        List<Game> gameList = findGamesByPlayerId(id);
        deleteByPlayerId(id);
        return gameList;
    }


}
