package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.Translator_EntityDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.GameServiceImpl;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerServiceImpl;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("players")
public class DiceController {
    @Autowired
    private GameServiceImpl gameService;
    @Autowired
    private PlayerServiceImpl playerService;

    /**
     *  http://localhost:9005/players [Player]
     *
     *
     * URL’s:
     * POST /players: crea un jugador/a.
     * PUT  /players: modifica el nom del jugador/a.
     *
     * TODO POST /players/{id}/games/ : un jugador/a específic realitza una tirada dels daus.
     * TODO DELETE /players/{id}/games: elimina les tirades del jugador/a.
     * GET /players/: retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     *      http://localhost:9005/players
     * GET /players/{id}/games: retorna el llistat de jugades per un jugador/a.
     *      http://localhost:9005/players/2
     * TODO GET /players/ranking: retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     * TODO GET /players/ranking/loser: retorna el jugador/a  amb pitjor percentatge d’èxit.
     * TODO GET /players/ranking/winner: retorna el  jugador amb pitjor percentatge d’èxit.
     */

    @PostMapping("/")
    public Player savePlayer(@RequestParam String name){
        return playerService.savePlayer(new Player(null, name));
    }

    @PutMapping()
    public Player updatePlayer(Player newPlayer){
        return playerService.updatePlayer(newPlayer);
    }

    // TODO add percentage d'exit
    @GetMapping()
    public List<PlayerGameDTO> getAllPlayers(){
         List<Player> list = playerService.getAllPlayers();
         return list.stream()
                 .map((p)->Translator_EntityDTO.playerDTOfromPlayer(p, gameService.averageMarkPLayer(p.getId())))
                 .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public List<Game> getGamePlayers(@PathVariable int id){
        return gameService.findGamesByPlayerId(id);
    }
    @PostMapping("/{id}/games")
    public Game playGame(@PathVariable int id){
        int gameResult = LogicGame.PLAY();
        System.err.println(gameResult);
        Player player = playerService.findById(id);
        return gameService.saveGame(player, gameResult);
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
