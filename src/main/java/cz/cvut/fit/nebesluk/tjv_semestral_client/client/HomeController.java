package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping(value = {"/home","/"})
    public String home(){
        return "home";
    }
}
