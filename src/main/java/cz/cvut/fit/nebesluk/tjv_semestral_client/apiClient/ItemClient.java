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
import java.util.*;

@Component
public class ItemClient {

    private WebTarget itemsEndpoint;
    private WebTarget selectedItemEndpointTemplate;
    private WebTarget selectedItemEndpoint;

    private WebTarget requestsEndpoint;

    private WebTarget offersEndpoint;

    private WebTarget tagsEndpointTemplate;

    private WebTarget authorEndpointTemplate;

    private WebTarget searchEndpointTemplate;

    private WebTarget activeEndpoint;

    private WebTarget itemReceiverEndpointTemplate;

    private WebTarget itemProviderEndpointTemplate;

    public ItemClient(@Value("${server.url}") String apiUrl){
        var client = ClientBuilder.newClient();
        itemsEndpoint = client.target(apiUrl+"/api/item");
        selectedItemEndpointTemplate = itemsEndpoint.path("/{id}");
        requestsEndpoint = itemsEndpoint.path("/request");
        offersEndpoint = itemsEndpoint.path("/offer");
        tagsEndpointTemplate = itemsEndpoint.path("/tag/{tag}");
        authorEndpointTemplate = itemsEndpoint.path("/author/{authorId}");
        searchEndpointTemplate = itemsEndpoint.path("/all/{term}");
        activeEndpoint = itemsEndpoint.path("/active");
        itemReceiverEndpointTemplate = itemsEndpoint.path("/{itemId}/request/{receiverId}");
        itemProviderEndpointTemplate = itemsEndpoint.path("/{itemId}/offer/{providerId}");
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

    public void provide(Long itemId, Long userId){
        var result = itemProviderEndpointTemplate.resolveTemplate("itemId",itemId).resolveTemplate("providerId",userId).request().post(Entity.entity(null,MediaType.APPLICATION_JSON_TYPE));

        if(result.getStatus() != 200){
            throw new RuntimeException(result.getStatusInfo().getReasonPhrase());
        }
    }

    public void receive(Long itemId, Long userId){
        var result = itemReceiverEndpointTemplate.resolveTemplate("itemId",itemId).resolveTemplate("receiverId",userId).request().post(Entity.entity(null,MediaType.APPLICATION_JSON_TYPE));

        if(result.getStatus() != 200){
            throw new RuntimeException(result.getStatusInfo().getReasonPhrase());
        }
    }

    public Collection<ItemSmallDto> getActive(){

        var result = activeEndpoint.request().get();

        if(result.getStatus() != 200){
            return new ArrayList<>();
        }

        return Arrays.stream(result.readEntity(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getAll(){

        var result = itemsEndpoint.request()
                .get();

        if(result.getStatus() != 200){
            return new ArrayList<>();
        }

        return Arrays.stream(result.readEntity(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getRequests(){

        var result = requestsEndpoint.request()
                .get();

        if(result.getStatus() != 200){
            return new ArrayList<>();
        }

        return Arrays.stream(result.readEntity(ItemSmallDto[].class)).toList();
    }

    public Optional<ItemDto> createRequest(NewItemDto entity){

        var result = requestsEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(entity,MediaType.APPLICATION_JSON_TYPE));

        if(result.getStatus() != 200){
            return Optional.empty();
        }

        return Optional.of(result.readEntity(ItemDto.class));
    }

    public Collection<ItemSmallDto> getOffers(){

        var result = offersEndpoint.request()
                .get();

        if(result.getStatus() != 200){
            return new ArrayList<>();
        }

        return Arrays.stream(result.readEntity(ItemSmallDto[].class)).toList();
    }

    public Optional<ItemDto> createOffer(NewItemDto entity){
        Objects.requireNonNull(entity);

        var result = offersEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(entity,MediaType.APPLICATION_JSON_TYPE));

        if(result.getStatus() != 200){
            return Optional.empty();
        }

        return Optional.of(result.readEntity(ItemDto.class));
    }

    public Collection<ItemSmallDto> getWithTags(String tag){
        var tempEndpoint = tagsEndpointTemplate.resolveTemplate("tag",tag);
        var result = tempEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();

        if(result.getStatus() != 200){
            return new ArrayList<ItemSmallDto>();
        }

        return Arrays.stream(
                result.readEntity(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getByAuthor(Long id){
        var result = authorEndpointTemplate.resolveTemplate("authorId",id).request()
                .get();

        if(result.getStatus() != 200){
            return new ArrayList<ItemSmallDto>();
        }

        return Arrays.stream(result.readEntity(ItemSmallDto[].class)).toList();
    }

    public Collection<ItemSmallDto> getSearchTerm(String term){
        var result = searchEndpointTemplate.resolveTemplate("term",term).request()
                .get();

        if(result.getStatus() != 200){
            return new ArrayList<>();
        }

        return Arrays.stream(result.readEntity(ItemSmallDto[].class)).toList();
    }

}
