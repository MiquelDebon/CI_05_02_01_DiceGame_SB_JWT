package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.mysql.auth;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.AuthenticationRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.DuplicateUserEmailException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.ExceptionHandler.DuplicateUserNameException;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.PlayerMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.entity.mysql.Role;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.repository.mysql.IplayerRepositoryMySQL;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class AuthenticationMySQLService {

    private final IplayerRepositoryMySQL repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IplayerRepositoryMySQL playerRepository;


    public AuthenticationResponse register(RegisterRequest request){
        try{
            checkDuplicatedEmail(request.getEmail());
            checkDuplicatedName(request.getFirstname());

            var user = PlayerMySQL.builder()
                    .name(request.getFirstname())
                    .surname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .registerDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                            .format(new java.util.Date()))
                    .build();
            repository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }catch (DuplicateUserEmailException e){
            throw new DuplicateUserEmailException("Error duplicated email");
        }catch (DuplicateUserNameException e){
            throw new DuplicateUserEmailException("Error duplicated name");
        }

    }


    public AuthenticationResponse authenticate (AuthenticationRequest request){
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
                .stream().map(PlayerMySQL::getEmail)
                .anyMatch((n)-> n.equalsIgnoreCase(email))
        ){
            throw new DuplicateUserEmailException("Duplicated name");
        }
    }

    void checkDuplicatedName(String name){
        if (
            !name.equalsIgnoreCase("ANONYMOUS")
            &&
            playerRepository.findAll()
                    .stream().map(PlayerMySQL::getName)
                    .anyMatch((n)-> n.equalsIgnoreCase(name))
        ){
            throw new DuplicateUserNameException("Duplicated name");
        }
    }



}
