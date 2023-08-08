package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.LogicGame;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.BaseDescriptionException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.DuplicateUserNameException;
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
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service

public class PlayerGamerServiceMySQLImpl implements IPlayerGamerServiceMySQL {
    @Autowired
    private IGameRepositoryMySQL gameRepository;
    @Autowired
    private IplayerRepositoryMySQL playerRepository;
    @Autowired
    private AuthenticationMySQLService authenticationMySQLService;

    /**
     *
     * 🔁 ----------  MAPPERS -----------
     *
     */

    public PlayerGameDTO playerDTOfromPlayer(PlayerMySQL player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.averageMarkPLayer(player.getId()));
    }
    public PlayerGameDTO rankingPlayerDTOfromPlayer(PlayerMySQL player){
        return new PlayerGameDTO(player.getId(), player.getName(), this.succesRate(player.getId()));
    }
    public GameDTO gameDTOfromGame(GameMySQL game){
        return new GameDTO(game.getMark());
    }

    public PlayerMySQL playerMySQLfromRequested(RegisterRequest request){
        int id = playerRepository.findByEmail(request.getEmail()).get().getId();

//        return new PlayerMySQL(
//                id,
//                updatedPlayer.getFirstname(),
//                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
//                        .format(new java.util.Date()),
//                updatedPlayer.getLastname(),
//                updatedPlayer.getEmail(),
//                updatedPlayer.getPassword()
//        )
        return null;
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
            log.error(BaseDescriptionException.EMPTY_DATABASE);
            throw new EmptyDataBaseException(BaseDescriptionException.EMPTY_DATABASE);
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
            log.error(BaseDescriptionException.EMPTY_DATABASE);
            throw new EmptyDataBaseException(BaseDescriptionException.EMPTY_DATABASE);
        }
    }

    @Override
    public PlayerGameDTO updatePlayer(RegisterRequest updatedPlayer){
        PlayerMySQL newPlayer = playerRepository.findByEmail(updatedPlayer.getEmail()).get();

        authenticationMySQLService.checkDuplicatedName(updatedPlayer.getFirstname());
        newPlayer.setName(updatedPlayer.getFirstname());
        playerRepository.save(newPlayer);
        return this.playerDTOfromPlayer(newPlayer);


//        boolean existPlayerByEmail = playerRepository.existsByEmail(updatedPlayer.getEmail());
//        if(existPlayerByEmail){
//            boolean repitedName = false;
//            repitedName = playerRepository.findAll()
//                    .stream().map(PlayerMySQL::getName)
//                    .anyMatch((n) -> n.equalsIgnoreCase(updatedPlayer.getFirstname()));
//            if(!repitedName){
//                int id = playerRepository.findByEmail(updatedPlayer.getEmail()).get().getId();
////                newPlayer =
////                playerRepository.save(updatedPlayer);
////                return this.playerDTOfromPlayer(updatedPlayer);
//                return null;
//            }else{
//                log.error(BaseDescriptionException.DUPLICATED_USER_NAME);
//                throw new DuplicateUserNameException(BaseDescriptionException.DUPLICATED_USER_NAME);
//            }
//        }else{
//            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
//            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
//        }
    }

    @Override
    public Optional<PlayerGameDTO> findPlayerDTOById(int id){
        Optional<PlayerMySQL> player = playerRepository.findById(id);
        if(player.isPresent()){
            return Optional.of(this.playerDTOfromPlayer(player.get()));
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public List<GameDTO> findGamesByPlayerId(int id){
        if(playerRepository.existsById(id)){
            return gameRepository.findByPlayerId(id).stream()
                    .map(this::gameDTOfromGame)
                    .collect(Collectors.toList());
        }else{
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public GameDTO saveGame(int id){
        int result = LogicGame.PLAY();
        Optional<PlayerMySQL> player = playerRepository.findById(id);
        if(player.isPresent()){
            GameMySQL savedGame = gameRepository.save(new GameMySQL(result, player.get()));
            return gameDTOfromGame(savedGame);
        }else {
            log.error(BaseDescriptionException.NO_USER_BY_THIS_ID);
            throw new UserNotFoundException(BaseDescriptionException.NO_USER_BY_THIS_ID);
        }
    }

    @Override
    public void deleteGamesByPlayerId(int id){
        if(playerRepository.existsById(id)){
            gameRepository.deleteByPlayerId(id);
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
    public double succesRate(int id){
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
