package cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ItemDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.ItemSmallDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item.NewItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Component
public class ItemClient {

    private WebTarget itemsEndpoint;
    private WebTarget selectedItemEndpointTemplate;
    private WebTarget selectedItemEndpoint;

    private WebTarget requestsEndpoint;

    private WebTarget offersEndpoint;

    private WebTarget tagsEndpoint;

    private WebTarget authorEndpointTemplate;

    private WebTarget searchEndpointTemplate;

    private WebTarget activeEndpoint;

    public ItemClient(@Value("${server.url}") String apiUrl){
        var client = ClientBuilder.newClient();
        itemsEndpoint = client.target(apiUrl+"/api/item");
        selectedItemEndpointTemplate = itemsEndpoint.path("/{id}");
        requestsEndpoint = itemsEndpoint.path("/request");
        offersEndpoint = itemsEndpoint.path("/offer");
        tagsEndpoint = itemsEndpoint.path("/tags");
        authorEndpointTemplate = itemsEndpoint.path("/author/{authorId}");
        searchEndpointTemplate = itemsEndpoint.path("/all/{term}");
        activeEndpoint = itemsEndpoint.path("/active");
    }

    ////
    // Working with selected item
    ////

    public void selectItem(Long id){
        selectedItemEndpoint = selectedItemEndpointTemplate.resolveTemplate("id",id);
    }

    public void unselectCurrentItem(){
        selectedItemEndpoint = null;
    }

    public ItemDto getItem(){
        if(selectedItemEndpoint != null){
            return selectedItemEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                    .get(ItemDto.class);
        } else {
            throw new NullPointerException("No item selected");
        }
    }

    public ItemDto putItem(NewItemDto entity){
        if(selectedItemEndpoint != null){
            return selectedItemEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                    .put(Entity.entity(entity,MediaType.APPLICATION_JSON_TYPE),ItemDto.class);
        } else {
            throw new NullPointerException("No item selected");
        }
    }

    public void deleteItem(){
        if(selectedItemEndpoint != null){
            var response = selectedItemEndpoint.request()
                    .delete();

            if(response.getStatus() != 200){
                throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
            }
        } else {
            throw new NullPointerException("No item selected");
        }
    }

    ////
    // Working with all items
    ////

    public Collection<ItemSmallDto> getActive(){
        return Arrays.stream(activeEndpoint.request().get(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getAll(){
        return Arrays.stream(itemsEndpoint.request()
                .get(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getRequests(){
        return Arrays.stream(requestsEndpoint.request()
                .get(ItemSmallDto[].class)).toList();
    }

    public ItemDto createRequest(NewItemDto entity){
        return requestsEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(entity,MediaType.APPLICATION_JSON_TYPE),ItemDto.class);
    }

    public Collection<ItemSmallDto> getOffers(){
        return Arrays.stream(requestsEndpoint.request()
                .get(ItemSmallDto[].class)).toList();
    }

    public ItemDto createOffer(NewItemDto entity){
        Objects.requireNonNull(entity);
        return offersEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(entity,MediaType.APPLICATION_JSON_TYPE),ItemDto.class);
    }

    public Collection<ItemSmallDto> getWithTags(String tag){
        var tempEndpoint = tagsEndpoint.path("?tags="+tag);
        return Arrays.stream(tempEndpoint.request()
                .get(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getByAuthor(Long id){
        return Arrays.stream(authorEndpointTemplate.resolveTemplate("authorId",id).request()
                .get(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getSearchTerm(String term){
        return Arrays.stream(searchEndpointTemplate.resolveTemplate("term",term).request()
                .get(ItemSmallDto[].class)).toList();
    }

}
