package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IGameRepository;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IplayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlayerGamerService {
    @Autowired
    private IGameRepository gameRepository;
    @Autowired
    private IplayerRepository playerRepository;

    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private GameServiceImpl gameService;

    public PlayerGameDTO playerDTOfromPlayer(int id){
        Player player = playerRepository.findById(id).get();
        return new PlayerGameDTO(player.getId(), player.getName(), gameService.averageMarkPLayer(id));
    }


    /**
     *
     */

    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }


    public Player savePlayer(Player newPlayer){
        if(newPlayer.getName().equals("ANONYMOYS")){
            playerRepository.save(newPlayer);
            return newPlayer;
        }else{
            boolean repitedName = false;
            repitedName = playerRepository.findAll()
                    .stream().map(Player::getName)
                    .anyMatch((n)-> n.equalsIgnoreCase(newPlayer.getName()));
            if(!repitedName){
                playerRepository.save(newPlayer);
                return newPlayer;
            }else{
                log.error("Duplicated Name");
                return null;
            }
        }
    }

    //TODO control no duplicate name
    public Player updatePlayer(Player updatedPlayer){
        boolean repitedName = false;
        repitedName = playerRepository.findAll()
                .stream().map(Player::getName)
                .filter((n) -> n.equalsIgnoreCase(updatedPlayer.getName()))
                .count() > 1;
        if(!repitedName){
            playerRepository.save(updatedPlayer);
            return updatedPlayer;
        }else{
            log.error("Duplicated Name");
            return null;
        }
    }

    public Optional<Player> deletePlayer(int id){
        Optional<Player> deletedPlayer = playerRepository.findById(id);
        playerRepository.deleteById(id);
        return null;
    }

    public Player findById(int id){
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }




}
