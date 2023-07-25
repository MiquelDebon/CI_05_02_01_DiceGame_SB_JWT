package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "game")
public class GameMongoDB {
    @Id
    private String id;

    private int mark;

    private String playerId;

    public GameMongoDB(int mark, String player_id){
        this.mark = mark;
        this.playerId = player_id;
    }
}
