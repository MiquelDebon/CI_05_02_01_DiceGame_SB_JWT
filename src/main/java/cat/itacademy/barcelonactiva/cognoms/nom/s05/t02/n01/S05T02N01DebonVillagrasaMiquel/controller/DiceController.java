package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.Translator_EntityDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.GameServiceImpl;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerGamerService;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("players")
public class DiceController {
    @Autowired
    private GameServiceImpl gameService;
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private PlayerGamerService playerGamerService;

    /**
     * ⚠️TODO
     *      Send ResponseEntity<?>
     *      Add Documentation Swagger
     *      Make it reactive
     */

    /**
     * ☑️
     * TODO DELETE /players/{id}/games: elimina les tirades del jugador/a.
     * TODO GET /players/ranking: retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     * TODO GET /players/ranking/loser: retorna el jugador/a  amb pitjor percentatge d’èxit.
     * TODO GET /players/ranking/winner: retorna el  jugador amb pitjor percentatge d’èxit.
     */

    /**
     *  🛜POST /players: crea un jugador/a.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players [Player]
     */
    @PostMapping("/")
    public Player savePlayer(@RequestParam(required = false) String name){
        if(name == null){
            return playerService.savePlayer(new Player(null, "ANONYMOYS"));
        }
        return playerService.savePlayer(new Player(null, name));
    }

    /**
     *  🛜PUT  /players: modifica el nom del jugador/a.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players [Player]
     */
    @PutMapping()
    public Player updatePlayer(Player newPlayer){
        return playerService.updatePlayer(newPlayer);
    }

    /**
     *  🛜POST un jugador/a específic realitza una tirada dels daus.
     *  @see <a href="http://localhost:9005/players/2/games"> 🔗 http://localhost:9005/players/2/games </a>
     */
    @PostMapping("/{id}/games")
    public Game playGame(@PathVariable int id){
        int gameResult = LogicGame.PLAY();
        Player player = playerService.findById(id);
        return gameService.saveGame(player, gameResult);
    }


    /**
     *  🛜GET /players/: retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *  @see <a href="http://localhost:9005/players"> 🔗http://localhost:9005/players</a>
     */
    @GetMapping()
    public List<PlayerGameDTO> getAllPlayers(){
         List<Player> list = playerService.getAllPlayers();
         return list.stream()
                 .map((p)->Translator_EntityDTO.playerDTOfromPlayer(p, gameService.averageMarkPLayer(p.getId())))
                 .collect(Collectors.toList());
    }

    /**
     * 🛜GET /players/{id}/games: retorna el llistat de jugades per un jugador/a.
     *  @see <a href="http://localhost:9005/players/2"> 🔗http://localhost:9005/players/2</a>
     */
    @GetMapping("/{id}")
    public List<Game> getGamePlayers(@PathVariable int id){
        return gameService.findGamesByPlayerId(id);
    }

    /**
     *  🛜DELETE /players/{id}/games: elimina les tirades del jugador/a.
     *  🔗http://localhost:9005/players/2/games
     */
    @DeleteMapping()
    public PlayerGameDTO deletePlayerGames(@PathVariable int id){
        gameService.deleteByPlayerId(id);
        Player player = playerService.findById(id);
        return Translator_EntityDTO.playerDTOfromPlayer(player, gameService.averageMarkPLayer(player.getId()));
    }



    /**
     *
     * Other methods
     *
     */

    @GetMapping("/games")
    public List<Game> getGames(){
        return gameService.findAll();
    }
    @GetMapping("/player/{id}")
    public Player getPlayer(@PathVariable int id){
        return playerService.findById(id);
    }

    @GetMapping("game/{id}")
    public Game getGame(@PathVariable int id){
        return gameService.findById(id).get();
    }





}
