package dev.lmlouis.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class  SpringSecurityTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        // créer un faux objet dans la couche web. 
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                            .apply(springSecurity())
                            .build();
    }
    
    // Tester que le mock object est bien dans la couche web.  
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // Tester l’authentification en ajoutant les identifiants corrects grâce à la méthode
    @Test
    public void userLoginTest() throws Exception {
        mockMvc.perform(formLogin("/login").user("springuser").password("spring123")).andExpect(authenticated());
    }


    // tester que les identifiants incorrects ne donnent pas lieu à l’authentification de l’utilisateur, avec la méthode  unauthenticated()
    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("springuser").password("wrongpassword")).andExpect(unauthenticated());
    }
}
