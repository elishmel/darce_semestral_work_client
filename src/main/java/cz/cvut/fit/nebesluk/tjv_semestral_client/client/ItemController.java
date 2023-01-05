package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.TagClient;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    TagClient tagClient;
    public ItemController(TagClient tagClient_){
        tagClient = tagClient_;
    }

    @RequestMapping("/new")
    public String newItem(Model model, @RequestParam String type){

        var context = SecurityContextHolder.getContext();

        if(context.getAuthentication().isAuthenticated() && ((Authentication)context.getAuthentication() instanceof AnonymousAuthenticationToken) ){
            return "login";

        }
        model.addAttribute("tags",tagClient.GetAllTags());
        model.addAttribute("type",type);
        return "item";
    }


}
