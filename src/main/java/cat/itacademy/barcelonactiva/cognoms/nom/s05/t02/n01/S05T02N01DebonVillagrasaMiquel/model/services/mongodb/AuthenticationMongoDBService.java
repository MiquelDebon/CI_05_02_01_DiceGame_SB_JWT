package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mongodb;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.LoginRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.DuplicateUserEmailException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.DuplicateUserNameException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mongodb.PlayerMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.Role;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mongodb.IplayerRepositoryMongoDB;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class AuthenticationMongoDBService {

    private final IplayerRepositoryMongoDB repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IplayerRepositoryMongoDB playerRepository;


    public AuthenticationResponse register(RegisterRequest request){
        checkDuplicatedEmail(request.getEmail());
        checkDuplicatedName(request.getFirstname());

        PlayerMongoDB user;
        if(!request.getEmail().contains("@admin.com")){
            user = buildPlayer(request, Role.USER);
            repository.save(user);
        }else{
            user = buildPlayer(request, Role.ADMIN);
        }
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }


    public AuthenticationResponse authenticate (LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    void checkDuplicatedEmail(String email){
        if(playerRepository.findAll()
                .stream().map(PlayerMongoDB::getEmail)
                .anyMatch((n)-> n.equalsIgnoreCase(email))
        ){
            throw new DuplicateUserEmailException("Duplicated email");
        }
    }

    void checkDuplicatedName(String name){
        if (
            !name.equalsIgnoreCase("ANONYMOUS")
            &&
            playerRepository.findAll()
                    .stream().map(PlayerMongoDB::getName)
                    .anyMatch((n)-> n.equalsIgnoreCase(name))
        ){
            throw new DuplicateUserNameException("Duplicated name");
        }
    }

    public PlayerMongoDB buildPlayer(RegisterRequest request, Role role){
        return PlayerMongoDB.builder()
                .name(request.getFirstname())
                .surname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .registerDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                        .format(new java.util.Date()))
                .build();
    }



}
