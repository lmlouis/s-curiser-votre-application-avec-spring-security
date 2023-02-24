package dev.lmlouis.login.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
@RestController
public class LoginController {
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

    @RequestMapping("/*")
    public String getGitHub(Principal user){
        return user.toString();
    }
}
