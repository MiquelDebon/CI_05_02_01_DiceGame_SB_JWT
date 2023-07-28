package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "API Rest Swagger"),
		tags = @Tag(name = "IT-Academy", description = "Controller methods to deal with the Game")

)
public class S05T02N01DebonVillagrasaMiquelApplication {

	public static void main(String[] args) {
		SpringApplication.run(S05T02N01DebonVillagrasaMiquelApplication.class, args);
	}

}
