package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ItemClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    ItemService itemService;

    ViewController(ItemService itemService_){
        itemService = itemService_;
    }

    @RequestMapping("/view")
    public String view(Model model, @RequestParam Long id){
        model.addAttribute("item",itemService.GetItemDetailed(id));
        return "view";
    }
}
