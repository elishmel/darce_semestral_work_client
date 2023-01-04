package cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.tag.TagDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Arrays;
import java.util.Collection;

@Component
public class TagClient {

    private WebTarget tagEndpoint;

    public TagClient(@Value("${server.url}") String apiUrl){
        var client = ClientBuilder.newClient();
        tagEndpoint = client.target(apiUrl+"/api/tag");
    }

    public Collection<TagDto> GetAllTags(){
        return Arrays.stream(tagEndpoint.request().get(TagDto[].class)).toList();
    }
}
