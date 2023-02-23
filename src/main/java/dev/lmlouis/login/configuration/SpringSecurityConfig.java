package dev.lmlouis.login.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // definit dans le context spring cette classe en tant que bean de configuration 
@EnableWebSecurity // définit la classe SpringSecurityConfig en tant que configuration Spring Security
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
    
    // Les authorisations :  Chaine de filtres de sécurité

    //  la méthode  configure() qui prend en paramètre un objet  HTTPSecurity pour faire passer toutes les requêtes HTTP à 
    //  travers la chaîne de filtres de sécurité, et configurez le formulaire de connexion par défaut avec la méthode
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // authorisation des requêttes
            .antMatchers("/admin").hasRole("ADMIN") // sur des ressources verouillées par le role ADMIN
            .antMatchers("/user").hasRole("USER") // sur des ressources verouillées par le role USER
            .anyRequest().authenticated() // et sur toute requêtte qui nécessite une authentification
            .and()
            .formLogin(); // provenant du fomulaire de login
    }

    // méthode  configure()  avec la classe AuthenticationManagerBuilder pour gérer la série de règles d’authentification. 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("springuser").password(passwordEncoder().encode("spring123")).roles("USER") // springuser a le role USER
            .and()
            .withUser("springadmin").password(passwordEncoder().encode("admin123")).roles("USER","ADMIN"); // springadmin a le role USER et ADMIN
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
