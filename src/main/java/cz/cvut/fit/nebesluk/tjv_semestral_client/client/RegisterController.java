package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ClientService;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ImageService;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    RegisterService registerService;
    ClientService clientService;
    ImageService imageService;

    public RegisterController(RegisterService registerService_,ClientService clientClient_,ImageService imageClient_){
        registerService = registerService_;
        clientService = clientClient_;
        imageService = imageClient_;
    }

    @RequestMapping("/register")
    public String register(Model model, @RequestParam(required = false) boolean error){
        if(error){
            model.addAttribute("error",true);
        }
        return "register";
    }

    @PostMapping("/registerUser")
    public String createUser(@RequestParam String user, @RequestParam String name, @RequestParam String pwd, @RequestParam MultipartFile picture, Model model, HttpServletRequest request){
        if(registerService.UserExists(user)){
            model.addAttribute("error",true);
            return "register";
        }

        return registerService.Register(user,name,picture,pwd,request);
    }

}
