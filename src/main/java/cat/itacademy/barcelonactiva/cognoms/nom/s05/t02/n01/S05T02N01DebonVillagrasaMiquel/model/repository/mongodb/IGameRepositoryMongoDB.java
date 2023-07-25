package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mongodb;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb.GameMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.GameMySQL;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGameRepositoryMongoDB extends MongoRepository<GameMongoDB, Integer> {
    void deleteByPlayerId(int playerId);
    List<GameMongoDB> findByPlayerId(int id);
}
