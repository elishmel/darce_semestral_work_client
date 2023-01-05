package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    LoginService loginService;
    
    public LoginController(LoginService loginService_){
        loginService = loginService_;
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/loginUser")
    public String login(@RequestParam String user, @RequestParam String pwd, HttpServletRequest request, Model model){
        if(loginService.Login(user,pwd,request)){
            return "home";
        }
        model.addAttribute("error",true);
        return "login";
    }
}
