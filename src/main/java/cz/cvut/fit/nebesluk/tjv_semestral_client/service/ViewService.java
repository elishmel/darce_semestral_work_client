package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ItemClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.NewItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ViewItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ViewService {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemClient itemClient;

    @Autowired
    ImageService imageService;

    @Autowired
    ClientService clientService;

    public ViewItemDto Edit( Long id,  String type,  String name,  String desc,String tag, List<MultipartFile> images){
        NewItemDto item = new NewItemDto();

        if(images.isEmpty()){
            item.setImages(itemService.GetItemDetailed(id).getImages().keySet());
        } else{
            Set<Long> pictures = new HashSet<>();
            images.stream().forEach(image -> {
                var im = imageService.CreateImage(image);
                if(im != null){
                    pictures.add(im.getImage_id());
                } else {
                    return;
                }
            });
            item.setImages(pictures);
        }

        item.setName(name);
        item.setDescription(desc);
        item.setAuthorId(clientService.GetByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow().getClient_id());
        var tags = new HashSet<String>();
        tags.add(tag);
        item.setTags(tags);

        return itemService.GetItemDetailed(itemService.UpdateById(id,item).orElseThrow().getItemId());

    }

    public void Provide(Long id){
        var userId = clientService.GetByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow().getClient_id();
        itemClient.provide(id,userId);
    }

    public void Receive(Long id){
        var userId = clientService.GetByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow().getClient_id();
        itemClient.receive(id,userId);
    }
}
