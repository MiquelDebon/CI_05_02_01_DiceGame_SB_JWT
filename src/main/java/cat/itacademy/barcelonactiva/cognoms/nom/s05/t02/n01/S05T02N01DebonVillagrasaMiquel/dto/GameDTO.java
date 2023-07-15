package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private int mark;
    private String message;

    public GameDTO( int mark) {
        this.mark = mark;
        message = mark > 7 ? "WIN" : "LOSE";
    }
}
