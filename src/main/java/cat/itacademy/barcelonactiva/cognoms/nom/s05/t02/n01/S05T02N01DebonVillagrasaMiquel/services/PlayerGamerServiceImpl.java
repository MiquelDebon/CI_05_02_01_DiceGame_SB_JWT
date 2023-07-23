package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.ExceptionHandler.ExceptionsMessage;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.DuplicateUserNameException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IGameRepository;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.repository.IplayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
//@AllArgsConstructor
public class PlayerGamerServiceImpl implements IPlayerGamerService{
    @Autowired
    private IGameRepository gameRepository;
    @Autowired
    private IplayerRepository playerRepository;


    /**
     *
     * 🔁 ----------  MAPPERS -----------
     *
     */

    public PlayerGameDTO playerDTOfromPlayer(Player player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.averageMarkPLayer(player.getId()));
    }
    public PlayerGameDTO rankingPlayerDTOfromPlayer(Player player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.succesRate(player.getId()));
    }
    public GameDTO gameDTOfromGame(Game game){
        return new GameDTO(game.getMark());
    }

    /**
     *
     *  ℹ️    ------- METHODS ----------------
     *
     */


    @Override
    public List<PlayerGameDTO> getAllPlayersDTO(){
        try{
            return playerRepository.findAll().stream()
                    .map(p -> this.playerDTOfromPlayer(p))
                    .collect(Collectors.toList());
        }catch (RuntimeException e){
            log.error(ExceptionsMessage.EMPTY_DATABASE);
            throw new EmptyDataBaseException(ExceptionsMessage.EMPTY_DATABASE);
        }
    }

    @Override
    public List<PlayerGameDTO> getAllPlayersDTORanking(){
        try {
            return playerRepository.findAll().stream()
                    .map(p -> rankingPlayerDTOfromPlayer(p))
                    .sorted(Comparator.comparing(PlayerGameDTO::getAverageMark).reversed())
                    .collect(Collectors.toList());
        }catch (RuntimeException e){
            log.error(ExceptionsMessage.EMPTY_DATABASE);
            throw new EmptyDataBaseException(ExceptionsMessage.EMPTY_DATABASE);
        }
    }

    @Override
    public PlayerGameDTO savePlayer(Player newPlayer){
        if(newPlayer.getName().equals("ANONYMOYS")){
            playerRepository.save(newPlayer);
            return this.playerDTOfromPlayer(newPlayer);
        }else{
            boolean repitedName = playerRepository.findAll()
                    .stream().map(Player::getName)
                    .anyMatch((n)-> n.equalsIgnoreCase(newPlayer.getName()));
            if(!repitedName){
                playerRepository.save(newPlayer);
                return this.playerDTOfromPlayer(newPlayer);
            }else{
                log.error(ExceptionsMessage.DUPLICATED_USER_NAME);
                throw new DuplicateUserNameException(ExceptionsMessage.DUPLICATED_USER_NAME);
            }
        }


    }

    @Override
    public PlayerGameDTO updatePlayer(Player updatedPlayer){
        boolean repitedName = false;
        repitedName = playerRepository.findAll()
                .stream().map(Player::getName)
                .anyMatch((n) -> n.equalsIgnoreCase(updatedPlayer.getName()));
        if(!repitedName){
            playerRepository.save(updatedPlayer);
            return this.playerDTOfromPlayer(updatedPlayer);
        }else{
            log.error(ExceptionsMessage.DUPLICATED_USER_NAME);
            throw new DuplicateUserNameException(ExceptionsMessage.DUPLICATED_USER_NAME);
        }
    }

    @Override
    public Optional<PlayerGameDTO> findPlayerDTOById(int id){
        Optional<Player> player = playerRepository.findById(id);
        if(player.isPresent()){
            return Optional.of(this.playerDTOfromPlayer(player.get()));
        }else{
            log.error(ExceptionsMessage.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(ExceptionsMessage.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public List<GameDTO> findGamesByPlayerId(int id){
        if(playerRepository.existsById(id)){
            return gameRepository.findByPlayerId(id).stream()
                    .map(this::gameDTOfromGame)
                    .collect(Collectors.toList());
        }else{
            log.error(ExceptionsMessage.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(ExceptionsMessage.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public GameDTO saveGame(int id, int result){
        Optional<Player> player = playerRepository.findById(id);
        if(player.isPresent()){
            Game savedGame = gameRepository.save(new Game(result, player.get()));
            return gameDTOfromGame(savedGame);
        }else {
            log.error(ExceptionsMessage.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(ExceptionsMessage.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public void deleteGamesByPlayerId(int id){
        if(playerRepository.existsById(id)){
            gameRepository.deleteByPlayerId(id);
        }else{
            log.error(ExceptionsMessage.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(ExceptionsMessage.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public Optional<PlayerGameDTO> getWorstPlayer(){
        List<PlayerGameDTO> playersList = this.getAllPlayersDTORanking();
        return Optional.of(playersList.stream()
                .min(Comparator.comparing(PlayerGameDTO::getAverageMark))
                .orElseThrow(NoSuchElementException::new));
    }

    @Override
    public Optional<PlayerGameDTO> getBestPlayer(){
        List<PlayerGameDTO> playersList = this.getAllPlayersDTORanking();
        return Optional.of(playersList.stream()
                .max(Comparator.comparing(PlayerGameDTO::getAverageMark))
                .orElseThrow(NoSuchElementException::new));
    }

    @Override
    public OptionalDouble averageTotalMarks(){
        return this.getAllPlayersDTO().stream()
                .mapToDouble(PlayerGameDTO::getAverageMark)
                .average();
    }


    /**
     *
     * support methods
     *
     */
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

    public Double averageMarkPLayer(int idPlayer){
        List<GameDTO> games = findGamesByPlayerId(idPlayer);
        return  Math.round((games.stream()
                .mapToDouble(GameDTO::getMark)
                .average()
                .orElse(Double.NaN)) * 100.00) / 100.00;

    }


}
