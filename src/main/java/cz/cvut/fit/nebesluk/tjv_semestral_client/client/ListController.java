package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ItemClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ItemSmallDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ListDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ItemService;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class ListController {

    ItemService itemService;
    TagService tagService;

    ListController(ItemService itemService_,TagService tagService_){
        itemService = itemService_;tagService=tagService_;
    }

    @RequestMapping(value = {"/list"})
    public String view(Model model, @RequestParam(required = false) String type, @RequestParam(required = false) String term){

        model.addAttribute("tags",tagService.GetAll());

        if(type == null){
            model.addAttribute("allItems",itemService.GetAll());
        } else if (type.equals("offer")){
            model.addAttribute("allItems",itemService.GetAllOfferListing());
            model.addAttribute("type","offer");
        } else if (type.equals("request")){
            model.addAttribute("allItems",itemService.GetAllRequestListing());
            model.addAttribute("type","request");
        } else if(type.equals("search") && term != null){
            model.addAttribute("allItems",itemService.GetTermAllListing(term));
            model.addAttribute("searchValue",term);
        } else if(type.equals("tag") && term != null){
            model.addAttribute("allItems",itemService.GetAllTags(term));
        }
        return "list";
    }

}
