package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Game")
public class GameMySQL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int mark;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "player_id")
    @JsonIgnore  //is used to ignore the logical property used in serialization and deserialization
    private PlayerMySQL player;

    public GameMySQL(int mark, PlayerMySQL player){
        id = null;
        this.mark = mark;
        this.player = player;
    }
}
