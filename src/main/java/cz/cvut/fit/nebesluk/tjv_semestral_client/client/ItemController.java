package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.TagClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.NewItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ClientService;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ImageService;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.ItemService;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ItemController {

    ItemService itemService;
    TagClient tagClient;

    ImageService imageService;

    ClientService clientService;
    public ItemController(TagClient tagClient_,ItemService itemService_,ClientService clientService_,ImageService imageService_){
        tagClient = tagClient_;
        imageService=imageService_;
        itemService = itemService_;
        clientService = clientService_;
    }

    @RequestMapping("/new")
    public String newItem(Model model, @RequestParam String type){

        var context = SecurityContextHolder.getContext();

        if( ((Authentication)context.getAuthentication() instanceof AnonymousAuthenticationToken) ){
            return "login";

        }
        model.addAttribute("allTags",tagClient.GetAllTags());
        model.addAttribute("type",type);
        return "item";
    }

    @PostMapping("/createItem")
    public String createNewItem(Model model,@RequestParam String type,@RequestParam String name, @RequestParam String desc, @RequestParam(required = false) String tag,@RequestParam List<MultipartFile> images){

        NewItemDto dto = new NewItemDto();

        dto.setDescription(desc);
        dto.setName(name);
        if(!tag.equals("x")){
            Set<String> tags = new HashSet<>();
            tags.add(tag);
            dto.setTags(tags);
        } else {
            dto.setTags(null);
        }
        dto.setAuthorId(
                clientService.GetByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()
                ).orElseThrow().getClient_id());

        Set<Long> pictures = new HashSet<Long>();


        if(images!= null){
            images.stream().forEach(image -> {
                var im = imageService.CreateImage(image);
                if(im != null){
                    pictures.add(im.getImage_id());
                } else {
                  return;
                }
            });
        }

        dto.setImages(pictures);

        ItemDto target = null;

        if(type.equals("request")){
            target = itemService.CreateRequest(dto).orElseThrow(RuntimeException::new);
        } else if(type.equals("offer")){
            target = itemService.CreateOffer(dto).orElseThrow(RuntimeException::new);
        }

        model.addAttribute("item",itemService.GetItemDetailed(target.getItemId()));
        return "view";
    }
}
