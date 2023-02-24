package dev.lmlouis.login.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Map;
@RestController
public class LoginController {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService; //  stocker le token de façon sécurisée et immuable : 


    @RequestMapping("/**") // tout appel qui ne vise pas le slach ou le slach admin soit dirigé vers le welcom github user
    @RolesAllowed("USER")
    public String getUser(){
        return "Welcome, USER";
    }

    @RequestMapping("/admin")
    @RolesAllowed("ADMIN")
    public String getAdmin(){
        return "Welcome, ADMIN";
    }
    

    /* methode qui recupère les info d'un user depuis  plusieurs fournisseurs d'identité, */
    @RequestMapping("/*")
    public String getUserInfo(Principal userDetails){
        StringBuffer userInfo = new StringBuffer(); // permet de manipuler des des chaine de caractère on peut aussi utiliser un hashmap

        if(userDetails instanceof UsernamePasswordAuthenticationToken){
            userInfo.append(getUsernamePasswordLoginInfo(userDetails)); // si il provient de loginForm
         }
         else if(userDetails instanceof OAuth2AuthenticationToken){
            userInfo.append(getOauth2LoginInfo(userDetails)); // // si il provient de fournisseur d'identité tel que github
             }

        return "connected "+userInfo.toString();
    }


    private StringBuffer getUsernamePasswordLoginInfo(Principal userDetails){
        StringBuffer usernameInfo = new StringBuffer();
        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) userDetails); // récupérer le nom de l’utilisateur, après avoir authentifié le token en utilisant la méthode getPrincipal()

        if (token.isAuthenticated()) {
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome, "+ u.getUsername());
            
        } else {
            usernameInfo.append("NA");
            
        }
        return usernameInfo;

    }


    private StringBuffer getOauth2LoginInfo(Principal userDetails){
        StringBuffer protectedInfo = new StringBuffer();
        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) userDetails);
        OAuth2AuthorizedClient authClient = this.authorizedClientService
                                                .loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
        if(authToken.isAuthenticated()){

            Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();
            
            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append("Welcome, " + userAttributes.get("name")+"<br><br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
            protectedInfo.append("Access Token: " + userToken+"<br><br>");
        }
        else{
            protectedInfo.append("NA");
        }
        // Extraire le principal, le token d’accès, et le ID Token depuis Google.
        OAuth2User principal = ((OAuth2AuthenticationToken)userDetails).getPrincipal() ;
        OidcIdToken idToken = getIdToken(principal);
        if(idToken != null) {

            protectedInfo.append("idToken value: " + idToken.getTokenValue()+"<br><br>");
            protectedInfo.append("Token mapped values <br><br>");
            
            // Décoder l’ID Token pour extraire les informations du claim.
            Map<String, Object> claims = idToken.getClaims();
         
               for (String key : claims.keySet()) {
               protectedInfo.append("  " + key + ": " + claims.get(key)+"<br>");
               }
         }
                                                 
        return protectedInfo;
    }


    
    private OidcIdToken getIdToken(OAuth2User principal){
        if(principal instanceof DefaultOidcUser) {
          DefaultOidcUser oidcUser = (DefaultOidcUser)principal;
          return oidcUser.getIdToken();
        }
        return null;
     }


}
