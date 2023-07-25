package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "game")
public class GameMongoDB {
    @Id
    private int id;

    private int mark;

    private PlayerMongoDB player;

    public GameMongoDB(int mark, PlayerMongoDB player){
        this.mark = mark;
        this.player = player;
    }
}
