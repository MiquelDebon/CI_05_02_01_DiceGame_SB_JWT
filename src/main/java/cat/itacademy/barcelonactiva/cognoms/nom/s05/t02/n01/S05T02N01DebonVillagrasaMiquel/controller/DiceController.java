package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Game;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.GameServiceImpl;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("players")
public class DiceController {
    @Autowired
    private GameServiceImpl gameService;
    @Autowired
    private PlayerServiceImpl playerService;

    /**
     * URL’s:
     * POST: /players: crea un jugador/a.
     * PUT /players: modifica el nom del jugador/a.
     * POST /players/{id}/games/ : un jugador/a específic realitza una tirada dels daus.
     * DELETE /players/{id}/games: elimina les tirades del jugador/a.
     * GET /players/: retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.
     * GET /players/{id}/games: retorna el llistat de jugades per un jugador/a.
     * GET /players/ranking: retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
     * GET /players/ranking/loser: retorna el jugador/a  amb pitjor percentatge d’èxit.
     * GET /players/ranking/winner: retorna el  jugador amb pitjor percentatge d’èxit.
     */

    @GetMapping("/games")
    public List<Game> getGames(){
        return gameService.findAll();
    }

    @GetMapping("game/{id}")
    public Game getGame(@PathVariable int id){
        return gameService.findById(id).get();
    }

    @GetMapping("/games/{id}")
    public List<Game> getGamePlayers(@PathVariable int id){
        return gameService.findGamesByPlayerId(id);
    }

    @GetMapping("/getAllPlayers")
    public List<Player> getallPlayers(){
        return playerService.getAllPlayers();
    }

}
