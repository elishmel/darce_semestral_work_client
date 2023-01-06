package cz.cvut.fit.nebesluk.tjv_semestral_client.client;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ItemClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.NewItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ViewItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.tag.TagDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.service.*;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ViewController {

    ItemService itemService;

    ImageService imageService;
    TagService tagService;

    ClientService clientService;

    ViewService viewService;

    ViewController(ItemService itemService_,ImageService imageService_,TagService tagService_,ClientService clientService_,ViewService viewService_){
        itemService = itemService_;
        imageService = imageService_;
        tagService = tagService_;
        clientService = clientService_;
        viewService = viewService_;
    }

    @RequestMapping("/view")
    public String view(Model model, @RequestParam Long id){
        model.addAttribute("item",itemService.GetItemDetailed(id));
        return "view";
    }

    @GetMapping("/edit")
    public String editView(Model model,@RequestParam Long id){
        var item = itemService.GetItemDetailed(id);
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(item.getUsername())){
            return "login";
        }

        var itemTemp = itemService.GetItemDetailed(id);
        if(!itemTemp.isActive()){
            model.addAttribute("item",itemTemp);
            return "view";
        }

        var tags = tagService.GetAll();
        if(!item.getTags().isEmpty()){
            var tmp = new TagDto();

            tmp.setTag(item.getTags().stream().findFirst().get());
            if(!tags.contains(tmp)){
                tags.add(tmp);
            }
        }

        model.addAttribute("item",item);
        model.addAttribute("tags",tags);
        return "edit";
    }

    @PostMapping("/edit")
    public String editConfirm(Model model,@RequestParam Long id,@RequestParam String type,@RequestParam String name, @RequestParam String desc, @RequestParam(required = false) String tag,@RequestParam List<MultipartFile> images){
        var result = viewService.Edit(id,type,name,desc,tag,images);
        model.addAttribute("item",result);
        return "redirect:/view?id="+result.getItemId();
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam String username){
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals(username)){
            itemService.DeleteById(id);
            return "home";
        }

        return "login";

    }

    @RequestMapping("/provide")
    public String provide(Model model,@RequestParam Long id,@RequestParam String username){
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals(username) || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        var tmp = itemService.GetItemDetailed(id);
        if(!tmp.isActive()){
            model.addAttribute("item",tmp);
            return "redirect:/view?id="+tmp.getItemId();
        }

        viewService.Provide(id);

        model.addAttribute("item",itemService.GetItemDetailed(id));
        return "redirect:/view?id="+id;
    }

    @RequestMapping("/collect")
    public String receive(Model model,@RequestParam Long id,@RequestParam String username){
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals(username) || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        var tmp = itemService.GetItemDetailed(id);
        if(!tmp.isActive()){
            model.addAttribute("item",tmp);
            return "redirect:/view?id="+tmp.getItemId();
        }
        viewService.Receive(id);

        model.addAttribute("item",itemService.GetItemDetailed(id));
        return "redirect:/view?id="+id;
    }
}
