package cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.ClientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Component
public class ClientClient {

    private WebTarget clientEndpoint;
    private WebTarget specificClientEndpointTemplate;


    public ClientClient(@Value("${server.url}") String apiUrl){
        var client = ClientBuilder.newClient();
        clientEndpoint = client.target(apiUrl+"/api/client");
        specificClientEndpointTemplate = clientEndpoint.path("/{id}");
    }

    public ClientDto GetById(Long id){
        return specificClientEndpointTemplate.resolveTemplate("id",id).request()
                .get(ClientDto.class);
    }
}
