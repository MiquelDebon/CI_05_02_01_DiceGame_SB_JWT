package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.mysql;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.DuplicateUserNameException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.EmptyDataBaseException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.BaseDescriptionException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.UserNotFoundException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.dto.mysql.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql.PlayerGamerServiceMySQLImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Slf4j
@Tag(name = "IT-Academy - MySQL")
@RestController
@RequestMapping("players")
public class DiceControllerMySQL {
    //http://localhost:9005/swagger-ui/index.html

    @Autowired
    private PlayerGamerServiceMySQLImpl PGService;



    /**
     *  🟢POST Crea un jugador/a.
     *  🔗http://localhost:9005/players
     */
    @Operation(
            summary = "Save one player",
            description = "Description: This method save a new player in the database",
            parameters = @Parameter(
                    name = "name",
                    description = "The player's name otherwise it will ANONYMOUS"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content)
            }
    )
    @PostMapping("/")
    public ResponseEntity<?> savePlayer(@RequestParam(required = false) String name){
        PlayerGameDTO returnPlayer;
        log.info("Controller - Save method");
        try{
            if(name == null){
                returnPlayer = PGService.savePlayer(new PlayerMySQL(null, "ANONYMOYS"));
            }else{
                returnPlayer = PGService.savePlayer(new PlayerMySQL(null, name));
            }
            return new ResponseEntity<>(returnPlayer, HttpStatus.CREATED);
        }catch (DuplicateUserNameException e){
            throw e;
        }catch (Exception e){
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🟠PUT  Modifica el nom del jugador/a.
     *   🔗http://localhost:9005/players
     */
    @Operation(
            summary = "Update one player",
            description = "Description: This method update a new player in the database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content)
            }
    )
    @PutMapping()
    public ResponseEntity<?> updatePlayer(@RequestBody PlayerMySQL newPlayer){
        try{
            PlayerGameDTO updatedDTO = PGService.updatePlayer(newPlayer);
            return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
        }catch (UserNotFoundException | DuplicateUserNameException e){
            throw e;
        }catch (Exception e){
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  🟢POST un jugador/a específic realitza una tirada dels daus.
     *  @see <a href="http://localhost:9005/players/2/games"> 🔗 http://localhost:9005/players/2/games </a>
     */
    @Operation(
            summary = "Play by ID player",
            description = "Description: This method is to play a round",
            parameters = @Parameter(
                    name = "id",
                    description = "PLay the game by PlayerID",
                    required = true),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = GameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "404",
                            description = BaseDescriptionException.NO_USER_BY_THIS_ID,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content)
            }
    )
    @PostMapping("/{id}/games")
    public ResponseEntity<?> playGame(@PathVariable int id){
        try{
            GameDTO gameDTO = PGService.saveGame(id);
            return new ResponseEntity<>(gameDTO, HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw e;
        }catch (Exception e){
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🔵GET   Retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players</a>
     */
    @Operation(
            summary = "Get all players",
            description = "Description: This method retrieve all the player with their average mark",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "204",
                            description = BaseDescriptionException.EMPTY_DATABASE,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error",
                            content = @Content)
            }
    )
    @GetMapping()
    public ResponseEntity<?> getAllPlayers(){
        try{
            List<PlayerGameDTO> returnList = PGService.getAllPlayersDTO();
            log.info("Controller - get All Players");
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        }catch (EmptyDataBaseException e){
            throw e;
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
            description = "Description: This method retrieve all the games from the database by player ID",
            parameters = @Parameter(
                    name = "id", description = "ID player",
                    required = true, example = "1"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = GameDTO.class), mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "404",
                            description = BaseDescriptionException.NO_USER_BY_THIS_ID,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getGamePlayers(@PathVariable int id){
        try{
            List<GameDTO> returnList =  PGService.findGamesByPlayerId(id);
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        } catch (UserNotFoundException e){
            throw e;
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🔴DELETE Elimina les tirades del jugador/a.
     *  🔗http://localhost:9005/players/2/games
     */
    @Operation(
            summary = "Delete all the games from player by id",
            description = "Description: This method deletes all the games in the database from a player",
            parameters = @Parameter(
                    name = "id", description = "ID player",
                    required = true, example = "1"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}/games")
    public ResponseEntity<?> deletePlayerGames(@PathVariable int id){
        try{
            PlayerGameDTO player = PGService.findPlayerDTOById(id).get();
            PGService.deleteGamesByPlayerId(id);
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw e;
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  🔵 GET Retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players/ranking"> 🔗http://localhost:9005/players/ranking</a>
     */

    @Operation(
            summary = "Delete all the games from player by id",
            description = "Description: This method deletes all the games in the database from a player",
            parameters = @Parameter(
                    name = "id",
                    description = "ID player",
                    required = true,
                    example = "1"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "204",
                            description = BaseDescriptionException.EMPTY_DATABASE,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request buddy",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = BaseDescriptionException.INTERNAL_ERROR,
                            content = @Content)
            }

    )
    @GetMapping("/ranking")
    public ResponseEntity<?> getRankingPlayers(){
        try{
            List<PlayerGameDTO> returnList = PGService.getAllPlayersDTORanking();
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        }catch (EmptyDataBaseException e){
            throw e;
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     *  🔵 GET Retorna el jugador/a  amb pitjor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/loser"> 🔗http://localhost:9005/players/ranking/loser</a>
     */
    @Operation(
            summary = "The worst player",
            description = "Description: This method retrieve the worst player by the average mark",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "404",
                            description = BaseDescriptionException.EMPTY_DATABASE,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = BaseDescriptionException.INTERNAL_ERROR,
                            content = @Content)
            }
    )
    @GetMapping("/ranking/loser")
    public ResponseEntity<?> getWorstPlayer(){
        try{
            Optional<PlayerGameDTO> player = PGService.getWorstPlayer();
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (EmptyDataBaseException e){
            throw e;
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
            description = "Description: This method retrieve the best player by the average mark",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(schema = @Schema(implementation = PlayerGameDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "404",
                            description = BaseDescriptionException.EMPTY_DATABASE,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = BaseDescriptionException.INTERNAL_ERROR,
                            content = @Content)

            }
    )
    @GetMapping("/ranking/winner")
    public ResponseEntity<?> getBestPlayer(){
        try{
            Optional<PlayerGameDTO> player =  PGService.getBestPlayer();
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (EmptyDataBaseException e){
            throw e;
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
                            responseCode = "204",
                            description = BaseDescriptionException.EMPTY_DATABASE,
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = BaseDescriptionException.INTERNAL_ERROR,
                            content = @Content)
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


    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> adminEndPoint(){
        return new ResponseEntity<>("great admin", HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<?> userEndPoint(){
        return new ResponseEntity<>("great user or admin", HttpStatus.OK);
    }




}
