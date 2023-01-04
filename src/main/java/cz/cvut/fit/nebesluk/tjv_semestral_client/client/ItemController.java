package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.TagClient;
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
        model.addAttribute("tags",tagClient.GetAllTags());
        model.addAttribute("type",type);
        return "item";
    }
}
