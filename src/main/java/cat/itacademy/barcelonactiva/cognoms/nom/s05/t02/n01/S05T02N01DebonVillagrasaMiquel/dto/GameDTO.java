package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private int id;
    private int mark;
    private String message;

    public GameDTO(int id, int mark) {
        this.id = id;
        this.mark = mark;
        message = mark > 7 ? "WIN" : "LOSE";
    }
}
