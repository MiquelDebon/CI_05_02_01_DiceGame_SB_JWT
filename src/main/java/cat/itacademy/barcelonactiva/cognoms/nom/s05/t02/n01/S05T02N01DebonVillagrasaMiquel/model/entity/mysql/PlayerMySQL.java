package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity PLayer Information")
@Table(name="Player")
public class PlayerMySQL {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(defaultValue = "PlayerID", description = "Here goes the player's ID")
    @Column(unique = true)
    private Integer id;

    //Unique except "Anonymous" using Backend not SQL
    @Column(nullable = false)
    @Schema(defaultValue = "PlayerName", description = "Here goes the player's name")
    private String name;

    @Column(nullable = false)
    private String registerDate;


    public PlayerMySQL(Integer id, String name){
        this.id = id;
        this.name = name;
        registerDate = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                .format(new java.util.Date());
    }
}