package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IplayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl {
    @Autowired
    private IplayerRepository playerRepository;

    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    //TODO ACCEPT ANONIMOUS NAME
    public Player savePlayer(Player newPlayer){
        boolean repitedName = false;
        repitedName = playerRepository.findAll()
                        .stream().map(Player::getName)
                        .anyMatch((n)-> n.equalsIgnoreCase(newPlayer.getName()));
        if(!repitedName){
            playerRepository.save(newPlayer);
            return newPlayer;
        }else{
            throw new RuntimeException();
        }
    }

    //TODO control no duplicate name
    public Player updatePlayer(Player updatedPlayer){
        playerRepository.save(updatedPlayer);
        return updatedPlayer;
    }

    public Optional<Player> deletePlayer(int id){
        Optional<Player> deletedPlayer = playerRepository.findById(id);
        playerRepository.deleteById(id);
        return deletedPlayer;
    }

    public Player findById(int id){
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }




}
