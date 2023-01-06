package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ClientClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    ClientService clientService;
    ItemService itemService;
    ImageService imageService;

    Authentication authentication;

    UserService userService;

    LoginService loginService;

    public UserController(LoginService loginService_,ClientService clientService_,ItemService itemService_,ImageService imageService_,UserService userService_){
        clientService = clientService_;
        userService = userService_;
        itemService = itemService_;
        loginService = loginService_;
        imageService = imageService_;
        authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @RequestMapping("/user")
    public String user(Model model, @RequestParam(required = false) Long id){

        if(id == null && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
            id = clientService.GetByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().getClient_id();
        } else if (id == null && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return "redirect:/home";
        }

        var client = clientService.GetById(id);

        model.addAttribute("user",client);
        model.addAttribute("userProfilePicture",client.getProfilePicture() != null ? imageService.GetById(client.getProfilePicture()).getUrl() : "http://localhost:8081/no-pictures.png");
        model.addAttribute("allItems",itemService.GetAllFromAuthor(id));
        return "user";
    }

    @RequestMapping("/user/edit")
    public String userEdit(Model model){

        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        var client = clientService.GetByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();

        model.addAttribute("client",client);
        return "userEdit";

    }

    @PostMapping("/user/edit")
    public String userEdit(Model model, @RequestParam Long id, HttpServletRequest request, @RequestParam String user, @RequestParam String name, @RequestParam String pwd, @RequestParam MultipartFile image){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        var result = userService.Edit(id,user,name,pwd,image);
        loginService.Login(user,pwd,request);
        model.addAttribute("user",result);
        model.addAttribute("userProfilePicture",result.getProfilePicture() != null ? imageService.GetById(result.getProfilePicture()).getUrl() : "http://localhost:8081/no-pictures.png");
        model.addAttribute("allItems",itemService.GetAllFromAuthor(id));
        return "user";
    }

    @RequestMapping("/user/delete")
    public String userDelete(Model model, HttpServletRequest request, HttpServletResponse response){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        var user = clientService.GetByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        loginService.LogOut(request,response);
        clientService.Delete(user.orElseThrow().getClient_id());
        return "redirect:/home";
    }

}
