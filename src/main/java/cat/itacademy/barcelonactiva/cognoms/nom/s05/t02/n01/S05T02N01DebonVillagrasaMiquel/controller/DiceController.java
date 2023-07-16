package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerGamerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;

@Slf4j
@RestController
@RequestMapping("players")
public class DiceController {
    @Autowired
    private PlayerGamerService PGService;

    /**
     * ⚠️TODO
     *      Add Hexagonal
     *      Add Documentation Swagger
     *      Make it reactive
     */


    /**
     *  🟢POST Crea un jugador/a.
     *  🔗http://localhost:9005/players
     */
    @PostMapping("/")
    public ResponseEntity<?> savePlayer(@RequestParam(required = false) String name){
        PlayerGameDTO returnPlayer;
        log.info("Controller - Save method");
        try{
            if(name == null){
                returnPlayer = PGService.savePlayer(new Player(null, "ANONYMOYS"));
            }else{
                returnPlayer = PGService.savePlayer(new Player(null, name));
            }
            return new ResponseEntity<>(returnPlayer, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  🟠PUT  Modifica el nom del jugador/a.
     *   🔗http://localhost:9005/players
     */
    @PutMapping()
    public ResponseEntity<?> updatePlayer(@RequestBody Player newPlayer){
        try{
            PlayerGameDTO updatedDTO = PGService.updatePlayer(newPlayer);
            if(updatedDTO == null){
                return new ResponseEntity<>("Invalid player", HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>("Invalid player", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  🟢POST un jugador/a específic realitza una tirada dels daus.
     *  @see <a href="http://localhost:9005/players/2/games"> 🔗 http://localhost:9005/players/2/games </a>
     */
    @PostMapping("/{id}/games")
    public ResponseEntity<?> playGame(@PathVariable int id){
        int gameResult = LogicGame.PLAY();
        try{
            Player player = PGService.findPlayerById(id);
            GameDTO gameDTO = PGService.saveGame(player, gameResult);
            return new ResponseEntity<>(gameDTO, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     *  🔵GET   Retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players</a>
     */
    @GetMapping()
    public ResponseEntity<?> getAllPlayers(){
        try{
            List<PlayerGameDTO> returnList = PGService.getAllPlayersDTO();
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  🔵GET Retorna el llistat de jugades per un jugador/a.
     *  @see <a href="http://localhost:9005/players/2"> 🔗http://localhost:9005/players/2</a>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getGamePlayers(@PathVariable int id){
        try{
            List<GameDTO> returnList =  PGService.findGamesByPlayerId(id);
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  🔴DELETE Elimina les tirades del jugador/a.
     *  🔗http://localhost:9005/players/2/games
     */
    @DeleteMapping("/{id}/games")
    public ResponseEntity<?> deletePlayerGames(@PathVariable int id){
        try{
            PGService.deleteGamesByPlayerId(id);
            PlayerGameDTO player = PGService.findPlayerDTOById(id);
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  🔵 GET Retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players/ranking"> 🔗http://localhost:9005/players/ranking</a>
     */
    @GetMapping("/ranking")
    public ResponseEntity<?> getRankingPlayers(){
        try{
            List<PlayerGameDTO> returnList = PGService.getAllPlayersDTORanking();
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🔵 GET Retorna el jugador/a  amb pitjor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/loser"> 🔗http://localhost:9005/players/ranking/loser</a>
     */
    @GetMapping("/ranking/loser")
    public ResponseEntity<?> getWorstPlayer(){
        try{
            PlayerGameDTO player = PGService.getWorstPlayer();
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🔵 GET Retorna el  jugador amb pitjor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/winnerr"> 🔗http://localhost:9005/players/ranking/winner</a>
     */
    @GetMapping("/ranking/winner")
    public ResponseEntity<?> getBestPlayer(){
        try{
            PlayerGameDTO player =  PGService.getBestPlayer();
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/totalAverageMark")
    public ResponseEntity<?> getAverageTotalMark(){
        OptionalDouble averageMark = PGService.averageTotalMarks();
        if(averageMark.isPresent()){
            Double result = Math.round(averageMark.getAsDouble() * 100.00) / 100.00;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
