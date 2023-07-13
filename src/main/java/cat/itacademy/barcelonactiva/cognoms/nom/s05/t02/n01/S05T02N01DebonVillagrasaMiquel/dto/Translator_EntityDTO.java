package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.entity.Player;

public class Translator_EntityDTO {
    public static PlayerGameDTO playerDTOfromPlayer(Player player, double averageMark){
        return new PlayerGameDTO(player.getId(), player.getName(), averageMark);
    }
}
