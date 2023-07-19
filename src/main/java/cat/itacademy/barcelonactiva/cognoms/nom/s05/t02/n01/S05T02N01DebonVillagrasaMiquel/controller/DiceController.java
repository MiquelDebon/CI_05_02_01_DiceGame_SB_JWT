package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerGamerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Slf4j
@RestController
@Tag(name = "IT-Academy")
@RequestMapping("players")
public class DiceController {
    //http://localhost:9005/swagger-ui/index.html

    @Autowired
    private PlayerGamerServiceImpl PGService;


    /**
     * ⚠️TODO
     *      Interface Service
     *      Spring Security
     *      Add Documentation Swagger
     *      Add Hexagonal
     *      Make it reactive
     */

    final String NO_SUCH_PLAYER = "There no player with this ID";


    /**
     *  🟢POST Crea un jugador/a.
     *  🔗http://localhost:9005/players
     */
    @Operation(
            summary = "Save one player",
            description = "Description: This method save a new player in the database")
    @Parameter(
            name = "name",
            description = "The player's name otherwise it will ANONYMOUS")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
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
    @Operation(
            summary = "Update one player",
            description = "Description: This method update a new player in the database")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
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
    @Operation(
            summary = "Play by ID player",
            description = "Description: This method is to play a round")
    @Parameter(
            name = "id",
            description = "PLay the game by PlayerID",
            required = true)
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = GameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
    @PostMapping("/{id}/games")
    public ResponseEntity<?> playGame(@PathVariable int id){
        int gameResult = LogicGame.PLAY();
        try{
            GameDTO gameDTO = PGService.saveGame(id, gameResult);
            return new ResponseEntity<>(gameDTO, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(NO_SUCH_PLAYER, HttpStatus.NOT_FOUND);
        }
    }


    /**
     *  🔵GET   Retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players</a>
     */
    @Operation(
            summary = "Get all players",
            description = "Description: This method retrieve all the player with their average mark")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
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
    @Operation(
            summary = "All games from player",
            description = "Description: This method retrieve all the games from the database by player ID")
    @Parameter(
            name = "id", description = "ID player",
            required = true, example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = GameDTO.class), mediaType = "application/json"))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
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
    @Operation(
            summary = "Delete all the games from player by id",
            description = "Description: This method deletes all the games in the database from a player")
    @Parameter(
            name = "id", description = "ID player",
            required = true, example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
    @DeleteMapping("/{id}/games")
    public ResponseEntity<?> deletePlayerGames(@PathVariable int id){
        try{
            PGService.deleteGamesByPlayerId(id);
            PlayerGameDTO player = PGService.findPlayerDTOById(id).get();
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  🔵 GET Retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players/ranking"> 🔗http://localhost:9005/players/ranking</a>
     */

    @Operation(
            summary = "Delete all the games from player by id",
            description = "Description: This method deletes all the games in the database from a player")
    @Parameter(
            name = "id",
            description = "ID player",
            required = true,
            example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
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
    @Operation(
            summary = "The worst player",
            description = "Description: This method retrieve the worst player by the average mark")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
    @GetMapping("/ranking/loser")
    public ResponseEntity<?> getWorstPlayer(){
        try{
            Optional<PlayerGameDTO> player = PGService.getWorstPlayer();
            if(player.isPresent()){
                return new ResponseEntity<>(player, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🔵 GET Retorna el  jugador amb millor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/winnerr"> 🔗http://localhost:9005/players/ranking/winner</a>
     */
    @Operation(
            summary = "The best player",
            description = "Description: This method retrieve the best player by the average mark")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad request buddy",
            content = @Content)
    @GetMapping("/ranking/winner")
    public ResponseEntity<?> getBestPlayer(){
        try{
            Optional<PlayerGameDTO> player =  PGService.getBestPlayer();
            if(player.isPresent()){
                return new ResponseEntity<>(player, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Average success all players",
            description = "Description: This method retrieve the average mark of success from all player",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
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
