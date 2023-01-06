package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ClientClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ImageClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ItemClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.ClientDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.*;
import org.springframework.stereotype.Component;

import javax.swing.text.View;
import java.awt.*;
import java.util.*;
import java.util.List;

@Component
public class ItemService {

    ItemClient itemClient;
    ImageClient imageClient;
    ClientClient clientClient;

    ItemService(ItemClient itemClient_, ImageClient imageClient_, ClientClient clientClient_){
        itemClient = itemClient_;
        imageClient = imageClient_;
        clientClient = clientClient_;
    }

    private ListDto ToListDto(ItemSmallDto dto, String username, String path){
        var listDto = new ListDto();
        listDto.setAuthor(dto.getAuthor());
        listDto.setImage(path);
        listDto.setName(dto.getName());
        listDto.setItemId(dto.getItemId());
        listDto.setUsername(username);
        listDto.setOffer(dto.isOffer());
        return listDto;
    }

    public Collection<ListDto> GetAllActive(){
        var plainItems = itemClient.getActive();
        return getListDtos(plainItems);
    }

    public Collection<ListDto> GetAll(){
        var plainItems = itemClient.getAll();
        return getListDtos(plainItems);
    }

    public Collection<ListDto> GetAllFromAuthor(Long id){
        var plainItems = itemClient.getByAuthor(id);
        return getListDtos(plainItems);
    }

    public void DeleteById(Long id){
        itemClient.selectItem(id);
        itemClient.deleteItem();
        itemClient.unselectCurrentItem();
    }


    public Optional<ItemDto> UpdateById(Long id,NewItemDto dto){
        itemClient.selectItem(id);
        var result = itemClient.putItem(dto);
        itemClient.unselectCurrentItem();
        return Optional.of(result);
    }

    private Collection<ListDto> getListDtos(Collection<ItemSmallDto> plainItems) {
        Collection<ListDto> listings = new ArrayList<>();
        for (var item :
                plainItems) {

            listings.add(ToListDto(item,
                    clientClient.GetById(item.getAuthor()).getUsername(),
                    item.getImages().length > 0 ? imageClient.GetById(item.getImages()[0]).getUrl() : "http://localhost:8081/no-pictures.png"));
        }
        return listings;
    }

    public Collection<ListDto> GetAllListing(){
        var plainItems = itemClient.getAll();
        return getListDtos(plainItems);
    }

    public Collection<ListDto> GetAllRequestListing(){
        var plainItems = itemClient.getRequests();
        return getListDtos(plainItems);
    }

    public Collection<ListDto> GetAllOfferListing(){
        var plainItems = itemClient.getOffers();
        return getListDtos(plainItems);
    }

    public Collection<ListDto> GetTermAllListing(String term){
        var plainItems = itemClient.getSearchTerm(term);
        return getListDtos(plainItems);
    }

    public Collection<ListDto> GetAllTags(String tag){
        var plainItems = itemClient.getWithTags(tag);
        return getListDtos(plainItems);
    }

    public Optional<ItemDto> CreateOffer(NewItemDto dto){
        return itemClient.createOffer(dto);
    }

    public Optional<ItemDto> CreateRequest(NewItemDto dto){
        return itemClient.createRequest(dto);
    }

    public ViewItemDto GetItemDetailed(Long id){
        ViewItemDto dto = new ViewItemDto();
        itemClient.selectItem(id);
        var item = itemClient.getItem();
        var author = clientClient.GetById(item.getAuthorId());
        var receiver = item.getReceiver() != null ? clientClient.GetById(item.getReceiver()) : null;
        Map<Long,String> images = new HashMap<>();
        item.getImages().parallelStream().forEach(image -> {var img = imageClient.GetById(image); images.put(img.getImage_id(),img.getUrl());});
        dto.setActive(item.isActive());
        dto.setDescription(item.getDescription());
        dto.setAuthorId(item.getAuthorId());
        dto.setImages(images);
        dto.setTags(item.getTags());
        dto.setName(item.getName());
        dto.setUsername(author.getUsername());
        dto.setReceiverId(item.getReceiver());
        dto.setReceiverUsername(receiver != null ? receiver.getUsername() : null);
        dto.setItemId(item.getItemId());
        dto.setOffer(item.isOffer());
        itemClient.unselectCurrentItem();
        return dto;
    }
}
