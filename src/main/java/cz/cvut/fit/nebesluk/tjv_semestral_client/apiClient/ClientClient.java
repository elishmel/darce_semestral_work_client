package cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.ClientDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.NewClientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Component
public class ClientClient {

    private WebTarget clientEndpoint;
    private WebTarget specificClientEndpointTemplate;

    private WebTarget usernameClientEndpointTemplate;
    private WebTarget clientAuthEndpoint;


    public ClientClient(@Value("${server.url}") String apiUrl){
        var client = ClientBuilder.newClient();
        clientEndpoint = client.target(apiUrl+"/api/client");
        specificClientEndpointTemplate = clientEndpoint.path("/{id}");
        usernameClientEndpointTemplate = clientEndpoint.path("/username/{username}");
        clientAuthEndpoint = clientEndpoint.path("/auth");
    }

    public Optional<ClientDto> GetByUsername(String username){
        var result = usernameClientEndpointTemplate.resolveTemplate("username",username).request()
                .get();
        if(result.getStatus() != 200){
            return Optional.empty();
        }
        return Optional.of(result.readEntity(ClientDto.class));
    }

    public ClientDto GetById(Long id){
        return specificClientEndpointTemplate.resolveTemplate("id",id).request()
                .get(ClientDto.class);
    }

    public ClientDto Register(NewClientDto dto){
        var result = clientAuthEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(dto,MediaType.APPLICATION_JSON_TYPE));

        return result.readEntity(ClientDto.class);
    }

    public boolean CheckLogin(String baseAuthorization){
        return clientAuthEndpoint.request().header("Authorization",baseAuthorization)
                .get(boolean.class);
    }

}
