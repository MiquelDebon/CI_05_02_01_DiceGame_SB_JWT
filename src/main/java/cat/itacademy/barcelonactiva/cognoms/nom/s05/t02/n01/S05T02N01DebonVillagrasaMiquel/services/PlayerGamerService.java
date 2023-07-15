package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IGameRepository;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IplayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerGamerService {
    @Autowired
    private IGameRepository gameRepository;
    @Autowired
    private IplayerRepository playerRepository;


    /**
     * TODO MAPPER!!!
     *
     */

    public PlayerGameDTO playerDTOfromPlayer(int id){
        Player player = playerRepository.findById(id).get();
        return new PlayerGameDTO(player.getId(), player.getName(), this.averageMarkPLayer(id));
    }

    public PlayerGameDTO playerDTOfromPlayer(Player player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.averageMarkPLayer(player.getId()));
    }
    public PlayerGameDTO rankingPlayerDTOfromPlayer(Player player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.succesRate(player.getId()));
    }

    /**
     *
     */

    public List<PlayerGameDTO> getAllPlayersDTO(){
        return playerRepository.findAll().stream()
                .map(p -> this.playerDTOfromPlayer(p))
                .collect(Collectors.toList());
    }

    public List<PlayerGameDTO> getAllPlayersDTORanking(){
        return playerRepository.findAll().stream()
                .map( p -> rankingPlayerDTOfromPlayer(p))
                .sorted(Comparator.comparing(PlayerGameDTO::getAverageMark).reversed())
                .collect(Collectors.toList());
    }



    public PlayerGameDTO savePlayer(Player newPlayer){
        if(newPlayer.getName().equals("ANONYMOYS")){
            playerRepository.save(newPlayer);
            return this.playerDTOfromPlayer(newPlayer);
        }else{
            boolean repitedName = false;
            repitedName = playerRepository.findAll()
                    .stream().map(Player::getName)
                    .anyMatch((n)-> n.equalsIgnoreCase(newPlayer.getName()));
            if(!repitedName){
                playerRepository.save(newPlayer);
                return this.playerDTOfromPlayer(newPlayer);
            }else{
                log.error("Duplicated Name");
                return null;
            }
        }
    }

    public PlayerGameDTO updatePlayer(Player updatedPlayer){
        boolean repitedName = false;
        repitedName = playerRepository.findAll()
                .stream().map(Player::getName)
                .filter((n) -> n.equalsIgnoreCase(updatedPlayer.getName()))
                .count() > 1;
        if(!repitedName){
            playerRepository.save(updatedPlayer);
            return this.playerDTOfromPlayer(updatedPlayer);
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

    public PlayerGameDTO findPlayerDTOById(int id){
        return this.playerDTOfromPlayer(playerRepository.findById(id).get());
    }

    public Player findPlayerById(int id){
        return playerRepository.findById(id).get();
    }


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

    public List<Game> findAll(){
        return gameRepository.findAll();
    }

    public Optional<Game> findGameById(int id){
        return gameRepository.findById(id);
    }

    public List<Game> deleteGamesByPlayerId(int id){
        List<Game> gameList = findGamesByPlayerId(id);
        gameRepository.deleteByPlayerId(id);
        return gameList;
    }


    public double succesRate(int id){
        int rounds = gameRepository.findByPlayerId(id).size();
        if(rounds == 0) return 0;
        else {
            int wonRounds = (int) gameRepository.findByPlayerId(id).stream()
                    .map(Game::getMark)
                    .filter(m -> m >= 7)
                    .count();
            return (double) Math.round(((double) wonRounds / rounds) * 10000) /100;
        }
    }


}
