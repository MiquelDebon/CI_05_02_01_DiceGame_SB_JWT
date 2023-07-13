package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IplayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl {
    @Autowired
    private IplayerRepository playerRepository;

    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    public Player savePlayer(Player newPlayer){
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    public Player updatePlayer(Player updatedPlayer){
        playerRepository.save(updatedPlayer);
        return updatedPlayer;
    }

    public Optional<Player> deletePlayer(int id){
        Optional<Player> deletedPlayer = playerRepository.findById(id);
        playerRepository.deleteById(id);
        return deletedPlayer;
    }

    public Player getPlayer(int id){
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }


}
