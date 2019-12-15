package spring.security.jdbcauthentication.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home(){
        return ("home");
    }

    @GetMapping("")
    public String user(){
        return ("user");
    }

    @GetMapping("/admin")
    public String admin(){
        return "Welcome admin";
    }

}
