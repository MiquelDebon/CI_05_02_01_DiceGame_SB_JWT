package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto.PlayerGameDTO;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.services.PlayerGamerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;


public class DiceTestServices {

    private PlayerGamerService playerGamerService = new PlayerGamerService();

    @Test
    @DisplayName("Test the method to get the best player ")
    void bestPlayerMethod(){
        PlayerGameDTO returnedPlayer = playerGamerService.getBestPlayer();
        Assertions.assertNotNull(returnedPlayer);
//        Assert.notNull(returnedPlayer,"Error");
    }

    @Test
    void playerGamedTO(){
        List<PlayerGameDTO> list = playerGamerService.getAllPlayersDTO();
        Assertions.assertNotNull(list.get(0));
    }
}
