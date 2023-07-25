package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity PLayer Information")
@Document(collection = "player")
public class PlayerMongoDB {
    @Id
    @Schema(defaultValue = "PlayerID", description = "Here goes the player's ID")
    private String id;

    @Schema(defaultValue = "PlayerName", description = "Here goes the player's name")
    private String name;

    private String registerDate;


    public PlayerMongoDB(String name){
        this.name = name;
        registerDate = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                .format(new java.util.Date());
    }
}
