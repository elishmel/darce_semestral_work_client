package cz.cvut.fit.nebesluk.semestral.nebesluk_darce.client;

import cz.cvut.fit.nebesluk.semestral.nebesluk_darce.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ListController {

    ItemService itemService;

    ListController(ItemService itemService_){
        itemService = itemService_;
    }

    @RequestMapping(value = {"/list"})
    public String view(Model model, @RequestParam(required = false) String type, @RequestParam(required = false) String term){

        if(type == null){
            model.addAttribute("allItems",itemService.GetAll());
        } else if (type.equals("offer")){
            model.addAttribute("allItems",itemService.GetAllOffers());
            model.addAttribute("type","offer");
        } else if (type.equals("request")){
            model.addAttribute("allItems",itemService.GetAllRequests());
            model.addAttribute("type","request");
        } else if(type.equals("search") || term != null){
            model.addAttribute("allItems",itemService.GetAllWithTermInName(term));
            model.addAttribute("searchValue",term);
        }
        return "list";
    }

}
