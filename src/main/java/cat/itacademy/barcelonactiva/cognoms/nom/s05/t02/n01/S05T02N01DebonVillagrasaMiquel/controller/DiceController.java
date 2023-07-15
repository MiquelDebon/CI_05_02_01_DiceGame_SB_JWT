package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

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
     *      Add Documentation Swagger
     *      Make it reactive
     */

    /**
     * ☑️
     * TODO GET /players/ranking: retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     * TODO GET /players/ranking/loser: retorna el jugador/a  amb pitjor percentatge d’èxit.
     * TODO GET /players/ranking/winner: retorna el  jugador amb pitjor percentatge d’èxit.
     */

    /**
     *  🟢POST /players: crea un jugador/a.
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
     *  🟠PUT  /players: modifica el nom del jugador/a.
     *   🔗http://localhost:9005/players
     */
    @PutMapping()
    public PlayerGameDTO updatePlayer(Player newPlayer){
        return PGService.updatePlayer(newPlayer);
    }

    /**
     *  🟢POST un jugador/a específic realitza una tirada dels daus.
     *  @see <a href="http://localhost:9005/players/2/games"> 🔗 http://localhost:9005/players/2/games </a>
     */
    @PostMapping("/{id}/games")
    public Game playGame(@PathVariable int id){
        int gameResult = LogicGame.PLAY();
        Player player = PGService.findPlayerById(id);
        return PGService.saveGame(player, gameResult);
    }


    /**
     *  🔵GET /players/: retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players</a>
     */
    @GetMapping()
    public List<PlayerGameDTO> getAllPlayers(){
         return PGService.getAllPlayersDTO();
    }

    /**
     *  🔵GET /players/{id}/games: retorna el llistat de jugades per un jugador/a.
     *  @see <a href="http://localhost:9005/players/2"> 🔗http://localhost:9005/players/2</a>
     */
    @GetMapping("/{id}")
    public List<Game> getGamePlayers(@PathVariable int id){
        return PGService.findGamesByPlayerId(id);
    }

    /**
     *  🔴DELETE /players/{id}/games: elimina les tirades del jugador/a.
     *  🔗http://localhost:9005/players/2/games
     */
    @DeleteMapping("/{id}/games")
    public PlayerGameDTO deletePlayerGames(@PathVariable int id){
        PGService.deleteGamesByPlayerId(id);
        PlayerGameDTO player = PGService.findPlayerDTOById(id);
        return player;
    }

    /**
     *  🔵 GET /players/ranking: retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players/ranking"> 🔗http://localhost:9005/players/ranking</a>
     */
    @GetMapping("/ranking")
    public List<PlayerGameDTO> getRankingPlayers(){
        return PGService.getAllPlayersDTORanking();
    }


    /**
     *
     * Other methods
     *
     */

    @GetMapping("/games")
    public List<Game> getGames(){
        return PGService.findAll();
    }
    @GetMapping("/player/{id}")
    public PlayerGameDTO getPlayer(@PathVariable int id){
        return PGService.findPlayerDTOById(id);
    }

    @GetMapping("game/{id}")
    public Game getGame(@PathVariable int id){
        return PGService.findGameById(id).get();
    }





}
