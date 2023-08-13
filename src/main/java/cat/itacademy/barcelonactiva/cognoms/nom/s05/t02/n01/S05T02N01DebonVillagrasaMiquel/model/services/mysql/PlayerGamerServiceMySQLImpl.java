package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.LogicGame;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.BaseDescriptionException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.GameMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IplayerRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IGameRepositoryMySQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerGamerServiceMySQLImpl implements IPlayerGamerServiceMySQL {

    //TODO Add success rate to Player to not calculate it each time

    @Autowired
    private IGameRepositoryMySQL gameRepository;
    @Autowired
    private IplayerRepositoryMySQL playerRepositorySQL;
    @Autowired
    private AuthenticationMySQLService authenticationMySQLService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     *
     * 🔁 ----------  MAPPERS -----------
     *
     */

    public PlayerGameDTO playerDTOfromPlayer(PlayerMySQL player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.averageMarkPLayer(player.getId()));
    }
    public PlayerGameDTO rankingPlayerDTOfromPlayer(PlayerMySQL player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.successRate(player.getId()));
    }
    public GameDTO gameDTOfromGame(GameMySQL game){
        return new GameDTO(game.getMark());
    }


    /**
     *
     *  ℹ️    ------- METHODS ----------------
     *
     */


    @Override
    public List<PlayerGameDTO> getAllPlayersDTO(){

        List<PlayerMySQL> playerMySQLList = playerRepositorySQL.findAll();
        if(playerMySQLList.size() > 0){
            return playerMySQLList.stream()
                    .map(p -> this.playerDTOfromPlayer(p))
                    .collect(Collectors.toList());
        }else{
            log.error(BaseDescriptionException.EMPTY_DATABASE);
            throw new EmptyDataBaseException(BaseDescriptionException.EMPTY_DATABASE);
        }

    }

    @Override
    public List<PlayerGameDTO> getAllPlayersDTORanking(){
        List<PlayerMySQL> playerMySQLList = playerRepositorySQL.findAll();
        if(playerMySQLList.size() > 0){
            return playerMySQLList.stream()
                    .map(p -> rankingPlayerDTOfromPlayer(p))
                    .sorted(Comparator.comparing(PlayerGameDTO::getAverageMark).reversed())
                    .collect(Collectors.toList());
        }else{
            log.error(BaseDescriptionException.EMPTY_DATABASE);
            throw new EmptyDataBaseException(BaseDescriptionException.EMPTY_DATABASE);
        }
    }

    @Override
    public PlayerGameDTO updatePlayer(RegisterRequest updatedPlayer, String currentEmail){

        PlayerMySQL newPlayer = playerRepositorySQL.findByEmail(currentEmail).get();
        authenticationMySQLService.checkDuplicatedName(updatedPlayer.getFirstname());

        newPlayer.setName(updatedPlayer.getFirstname());
        newPlayer.setSurname(updatedPlayer.getLastname());
        newPlayer.setPassword(passwordEncoder.encode(updatedPlayer.getPassword()));

        if(!currentEmail.equalsIgnoreCase(updatedPlayer.getEmail())){
            authenticationMySQLService.checkDuplicatedEmail(updatedPlayer.getEmail());
            newPlayer.setEmail(updatedPlayer.getEmail());
            log.warn("Log out and log in again, otherwise the token will fail because the username won't match");
        }

        playerRepositorySQL.save(newPlayer);
        return this.playerDTOfromPlayer(newPlayer);

    }

    @Override
    public Optional<PlayerGameDTO> findPlayerDTOById(int id){
        Optional<PlayerMySQL> player = playerRepositorySQL.findById(id);
        if(player.isPresent()){
            return Optional.of(this.playerDTOfromPlayer(player.get()));
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public List<GameDTO> findGamesByPlayerId(int id){
        if(playerRepositorySQL.existsById(id)){
            return gameRepository.findByPlayerId(id).stream()
                    .map(this::gameDTOfromGame)
                    .collect(Collectors.toList());
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public GameDTO saveGame(int playerId){
        int result = LogicGame.PLAY();
        PlayerMySQL player = playerRepositorySQL.findById(playerId).
                orElseThrow(() -> new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID));

        GameMySQL savedGame = gameRepository.save(new GameMySQL(result, player));
        return gameDTOfromGame(savedGame);
    }

    @Override
    public List<GameDTO> deleteGamesByPlayerId(int id){
        if(playerRepositorySQL.existsById(id)){
            return gameRepository.deleteByPlayerId(id).stream()
                    .map(this::gameDTOfromGame)
                    .collect(Collectors.toList());
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
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
    public double successRate(int id){
        int rounds = gameRepository.findByPlayerId(id).size();
        if(rounds == 0) return 0;
        else {
            int wonRounds = (int) gameRepository.findByPlayerId(id).stream()
                    .map(GameMySQL::getMark)
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
