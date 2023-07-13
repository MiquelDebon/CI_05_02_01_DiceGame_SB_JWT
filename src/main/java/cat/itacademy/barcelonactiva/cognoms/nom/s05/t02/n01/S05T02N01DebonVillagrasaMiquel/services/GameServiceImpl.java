package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
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

    public List<Game> findAll(){
        return gameRepository.findAll();
    }

    public Optional<Game> findById(int id){
        return gameRepository.findById(id);
    }

}
