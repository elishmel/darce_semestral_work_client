package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ClientClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ClientService;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ItemService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    ClientService clientService;
    ItemService itemService;

    Authentication authentication;

    public UserController(ClientService clientService_,ItemService itemService_){
        clientService = clientService_;
        itemService = itemService_;
        authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @RequestMapping("/user")
    public String user(Model model, @RequestParam Long id){

        model.addAttribute("user",clientService.GetById(id));
        model.addAttribute("allItems",itemService.GetAllFromAuthor(id));
        return "user";
    }

}
