package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mongodb;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mongodb.GameDTOMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.LogicGame;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.BaseDescriptionException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mongodb.PlayerGameDTOMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb.GameMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb.PlayerMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mongodb.IGameRepositoryMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mongodb.IplayerRepositoryMongoDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerGamerServiceMongoDBImpl implements IPlayerGamerServiceMongoDB {
    @Autowired
    private IGameRepositoryMongoDB gameRepositoryMongoDB;
    @Autowired
    private IplayerRepositoryMongoDB playerRepositoryMongoDB;

    @Autowired
    private AuthenticationMongoDBService authenticationMongoDBService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    /**
     *
     * 🔁 ----------  MAPPERS -----------
     *
     */


    public PlayerGameDTOMongoDB playerDTOfromPlayer(PlayerMongoDB player){
        return new PlayerGameDTOMongoDB(player.getId(), player.getName(), this.averageMarkPLayer(player.getId()));
    }
    public PlayerGameDTOMongoDB rankingPlayerDTOfromPlayer(PlayerMongoDB player){
        return new PlayerGameDTOMongoDB(player.getId(), player.getName(), this.successRate(player.getId()));
    }
    public GameDTOMongoDB gameDTOfromGame(GameMongoDB game){
        return new GameDTOMongoDB(game.getMark());
    }


    /**
     *
     * ℹ️    ------- METHODS ----------------
     *
     */


    @Override
    public List<PlayerGameDTOMongoDB> getAllPlayersDTO(){
        try{
            return playerRepositoryMongoDB.findAll().stream()
                    .map(p -> this.playerDTOfromPlayer(p))
                    .collect(Collectors.toList());
        }catch (RuntimeException e){
            log.error(BaseDescriptionException.EMPTY_DATABASE);
            throw new EmptyDataBaseException(BaseDescriptionException.EMPTY_DATABASE);
        }
    }


    @Override
    public List<PlayerGameDTOMongoDB> getAllPlayersDTORanking(){
        try {
            return playerRepositoryMongoDB.findAll().stream()
                    .map(p -> rankingPlayerDTOfromPlayer(p))
                    .sorted(Comparator.comparing(PlayerGameDTOMongoDB::getAverageMark).reversed())
                    .collect(Collectors.toList());
        }catch (RuntimeException e){
            log.error(BaseDescriptionException.EMPTY_DATABASE);
            throw new EmptyDataBaseException(BaseDescriptionException.EMPTY_DATABASE);
        }
    }


    @Override
    public PlayerGameDTOMongoDB updatePlayer(RegisterRequest updatedPlayer, String currentEmail){

        PlayerMongoDB newPlayer = playerRepositoryMongoDB.findByEmail(currentEmail).get();
        authenticationMongoDBService.checkDuplicatedName(updatedPlayer.getFirstname());

        newPlayer.setName(updatedPlayer.getFirstname());
        newPlayer.setSurname(updatedPlayer.getLastname());
        newPlayer.setPassword(passwordEncoder.encode(updatedPlayer.getPassword()));

        if(!currentEmail.equalsIgnoreCase(updatedPlayer.getEmail())){
            authenticationMongoDBService.checkDuplicatedEmail(updatedPlayer.getEmail());
            newPlayer.setEmail(updatedPlayer.getEmail());
            log.warn("Log out and log in again, otherwise the token will fail because the username won't match");
        }

        playerRepositoryMongoDB.save(newPlayer);
        return this.playerDTOfromPlayer(newPlayer);
    }

    @Override
    public Optional<PlayerGameDTOMongoDB> findPlayerDTOById(String id){
        Optional<PlayerMongoDB> player = playerRepositoryMongoDB.findById(id);
        if(player.isPresent()){
            return Optional.of(this.playerDTOfromPlayer(player.get()));
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public List<GameDTOMongoDB> findGamesByPlayerId(String id){
        if(playerRepositoryMongoDB.existsById(id)){
            return gameRepositoryMongoDB.findByPlayerId(id).stream()
                    .map(this::gameDTOfromGame)
                    .collect(Collectors.toList());
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public GameDTOMongoDB saveGame(String id){
        int result = LogicGame.PLAY();
        Optional<PlayerMongoDB> player = playerRepositoryMongoDB.findById(id);
        if(player.isPresent()){
            GameMongoDB savedGame = gameRepositoryMongoDB.save(new GameMongoDB(result, id));
            return gameDTOfromGame(savedGame);
        }else {
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public void deleteGamesByPlayerId(String id){
        if(playerRepositoryMongoDB.existsById(id)){
            gameRepositoryMongoDB.deleteByPlayerId(id);
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public Optional<PlayerGameDTOMongoDB> getWorstPlayer(){
        List<PlayerGameDTOMongoDB> playersList = this.getAllPlayersDTORanking();
        return Optional.of(playersList.stream()
                .min(Comparator.comparing(PlayerGameDTOMongoDB::getAverageMark))
                .orElseThrow(NoSuchElementException::new));
    }

    @Override
    public Optional<PlayerGameDTOMongoDB> getBestPlayer(){
        List<PlayerGameDTOMongoDB> playersList = this.getAllPlayersDTORanking();
        return Optional.of(playersList.stream()
                .max(Comparator.comparing(PlayerGameDTOMongoDB::getAverageMark))
                .orElseThrow(NoSuchElementException::new));
    }

    @Override
    public OptionalDouble averageTotalMarks(){
        return this.getAllPlayersDTO().stream()
                .mapToDouble(PlayerGameDTOMongoDB::getAverageMark)
                .average();
    }


    /**
     *
     * support methods
     *
     */
    public double successRate(String id){
        int rounds = gameRepositoryMongoDB.findByPlayerId(id).size();
        if(rounds == 0) return 0;
        else {
            int wonRounds = (int) gameRepositoryMongoDB.findByPlayerId(id).stream()
                    .map(GameMongoDB::getMark)
                    .filter(m -> m >= 7)
                    .count();
            return (double) Math.round(((double) wonRounds / rounds) * 10000) /100;
        }
    }

    public Double averageMarkPLayer(String idPlayer){
        List<GameDTOMongoDB> games = findGamesByPlayerId(idPlayer);
        return  Math.round((games.stream()
                .mapToDouble(GameDTOMongoDB::getMark)
                .average()
                .orElse(Double.NaN)) * 100.00) / 100.00;

    }


}
