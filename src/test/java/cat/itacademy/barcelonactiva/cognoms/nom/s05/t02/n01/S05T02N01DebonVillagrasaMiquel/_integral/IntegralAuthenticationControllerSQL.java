package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel._integral;

import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.LoginRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller.mysql.AuthenticationMySQLController;
import cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.model.services.mysql.AuthenticationMySQLService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = AuthenticationMySQLService.class)
//@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class IntegralAuthenticationControllerSQL {

//    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AuthenticationMySQLService authServices;
    @InjectMocks
    private AuthenticationMySQLController authController;


    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        registerRequest = RegisterRequest.builder()
                .firstname("miquel")
                .lastname("debon")
                .email("miquel.debon@gmail.com")
                .password("password").build();
        loginRequest = LoginRequest.builder()
                .email("miquel.debon@gmail.com")
                .password("password").build();
        authenticationResponse = AuthenticationResponse.builder()
                .token("token").build();
    }

    @Test
    public void diceController_playGame_ReturnGameDTO() throws Exception{
        given(authServices.register(registerRequest)).willReturn(authenticationResponse);

        mockMvc.perform(post("/api/mysql/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(registerRequest)))
                        .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(authServices, times(1)).register(registerRequest);
    }




}
