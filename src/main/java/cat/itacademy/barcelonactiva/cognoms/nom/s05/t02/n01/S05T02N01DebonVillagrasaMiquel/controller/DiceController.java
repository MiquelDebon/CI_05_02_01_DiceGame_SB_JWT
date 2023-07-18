package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerGamerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;

@Slf4j
@RestController
@RequestMapping("players")
public class DiceController {
    //http://localhost:9005/swagger-ui/index.html

    @Autowired
    private PlayerGamerService PGService;


    /**
     * ⚠️TODO
     *      Interface Service
     *      Spring Security
     *      Add Documentation Swagger
     *      Add Hexagonal
     *      Make it reactive
     */


    /**
     *  🟢POST Crea un jugador/a.
     *  🔗http://localhost:9005/players
     */
    @Operation(
            tags = "IT-Academy",
            summary = "Save one player",
            description = "Description: This method save a new player in the database",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "The player's name otherwise it will ANONYMOUS",
                            required = false)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
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
            tags = "IT-Academy",
            summary = "Update one player",
            description = "Description: This method update a new player in the database",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Expected a player JSON",
                    content = @Content(schema = @Schema(implementation = Player.class))),

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(
                                    implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
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
            tags = "IT-Academy",
            summary = "Play by ID player",
            description = "Description: This method is to play a round",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = GameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
    @Parameter(name = "id", description = "PLay the game by PlayerID", required = true)
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
    @Operation(
            tags = "IT-Academy",
            summary = "Get all players",
            description = "Description: This method retrieve all the player with their average mark"
    )
    @ApiResponses({ // using this annotation to try, but I prefer all inside @Operation
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful response",
                    content = @Content(schema = @Schema(
                        implementation = PlayerGameDTO.class),
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request buddy",
                    content = @Content
            )
    })
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
            tags = "IT-Academy",
            summary = "All games from player",
            description = "Description: This method retrieve all the games from the database by player ID")
    @Parameter(name = "id", description = "ID player", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = GameDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad request buddy", content = @Content)
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
            tags = "IT-Academy",
            summary = "Delete all the games from player by id",
            description = "Description: This method deletes all the games in the database from a player",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID player",
                            required = true,
                            example = "1"
                    )
            },

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(
                                    implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
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

    @Operation(
            tags = "IT-Academy",
            summary = "Delete all the games from player by id",
            description = "Description: This method deletes all the games in the database from a player",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID player",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(
                                    implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
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
            tags = "IT-Academy",
            summary = "The worst player",
            description = "Description: This method retrieve the worst player by the average mark",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(
                                    implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
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
     *  🔵 GET Retorna el  jugador amb millor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/winnerr"> 🔗http://localhost:9005/players/ranking/winner</a>
     */
    @Operation(
            tags = "IT-Academy",
            summary = "The best player",
            description = "Description: This method retrieve the best player by the average mark",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(
                                    implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content
                    )
            }
    )
    @GetMapping("/ranking/winner")
    public ResponseEntity<?> getBestPlayer(){
        try{
            PlayerGameDTO player =  PGService.getBestPlayer();
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            tags = "IT-Academy",
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
