package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.mysql;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mysql/miquel")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return  ResponseEntity.ok("Hello from secure endpoint");
    }
}
