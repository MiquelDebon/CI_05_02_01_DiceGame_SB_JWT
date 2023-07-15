package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.GameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerGamerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("players")
public class DiceController {
    @Autowired
    private PlayerGamerService PGService;

    /**
     * ⚠️TODO
     *      Send ResponseEntity<?>
     *      Add TimeStamp
     *      Add Documentation Swagger
     *      Make it reactive
     */


    /**
     *  🟢POST Crea un jugador/a.
     *  🔗http://localhost:9005/players
     */
    @PostMapping("/")
    public PlayerGameDTO savePlayer(@RequestParam(required = false) String name){
        if(name == null){
            return PGService.savePlayer(new Player(null, "ANONYMOYS"));
        }
        return PGService.savePlayer(new Player(null, name));
    }

    /**
     *  🟠PUT  Modifica el nom del jugador/a.
     *   🔗http://localhost:9005/players
     */
    @PutMapping()
    public PlayerGameDTO updatePlayer(@RequestBody Player newPlayer){
        PlayerGameDTO updatedDTO = PGService.updatePlayer(newPlayer);
        //TODO if null -> throw 400
        return updatedDTO;
    }

    /**
     *  🟢POST un jugador/a específic realitza una tirada dels daus.
     *  @see <a href="http://localhost:9005/players/2/games"> 🔗 http://localhost:9005/players/2/games </a>
     */
    @PostMapping("/{id}/games")
    public GameDTO playGame(@PathVariable int id){
        int gameResult = LogicGame.PLAY();
        Player player = PGService.findPlayerById(id);
        return PGService.saveGame(player, gameResult);
    }


    /**
     *  🔵GET   Retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players</a>
     */
    @GetMapping()
    public List<PlayerGameDTO> getAllPlayers(){
         return PGService.getAllPlayersDTO();
    }

    /**
     *  🔵GET Retorna el llistat de jugades per un jugador/a.
     *  @see <a href="http://localhost:9005/players/2"> 🔗http://localhost:9005/players/2</a>
     */
    @GetMapping("/{id}")
    public List<GameDTO> getGamePlayers(@PathVariable int id){
        return PGService.findGamesByPlayerId(id);
    }

    /**
     *  🔴DELETE Elimina les tirades del jugador/a.
     *  🔗http://localhost:9005/players/2/games
     */
    @DeleteMapping("/{id}/games")
    public PlayerGameDTO deletePlayerGames(@PathVariable int id){
        PGService.deleteGamesByPlayerId(id);
        PlayerGameDTO player = PGService.findPlayerDTOById(id);
        return player;
    }

    /**
     *  🔵 GET Retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players/ranking"> 🔗http://localhost:9005/players/ranking</a>
     */
    @GetMapping("/ranking")
    public List<PlayerGameDTO> getRankingPlayers(){
        return PGService.getAllPlayersDTORanking();
    }


    /**
     *  🔵 GET Retorna el jugador/a  amb pitjor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/loser"> 🔗http://localhost:9005/players/ranking/loser</a>
     */
    @GetMapping("/ranking/loser")
    public PlayerGameDTO getWorstPlayer(){
        return PGService.getWorstPlayer();
    }


    /**
     *  🔵 GET Retorna el  jugador amb pitjor percentatge d’èxit.
     *  @see <a href="http://localhost:9005/players/ranking/winnerr"> 🔗http://localhost:9005/players/ranking/winner</a>
     */
    @GetMapping("/ranking/loser")
    public PlayerGameDTO getBestPlayer(){
        return PGService.getBestPlayer();
    }






}
