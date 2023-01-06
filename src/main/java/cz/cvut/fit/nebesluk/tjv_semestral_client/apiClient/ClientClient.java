package cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.ClientDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.NewClientDto;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void Register(NewClientDto dto){
        var result = clientAuthEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(dto,MediaType.APPLICATION_JSON_TYPE));

    }

    public Optional<ClientDto> putClient(Long id, NewClientDto dto){

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
        HttpAuthenticationFeature basic = HttpAuthenticationFeature.basic(auth.getName(),auth.getCredentials().toString());


        var res =  specificClientEndpointTemplate.resolveTemplate("id",id).request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(dto,MediaType.APPLICATION_JSON_TYPE));

        if(res.getStatus() != 200){
            throw new RuntimeException(res.getStatusInfo().getReasonPhrase());
        }

        return Optional.of(res.readEntity(ClientDto.class));
    }

    public void deleteClient(Long id){

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof AnonymousAuthenticationToken){
            throw new RuntimeException("Login is required");
        }
        HttpAuthenticationFeature basic = HttpAuthenticationFeature.basic(auth.getName(),auth.getCredentials().toString());

        var res = specificClientEndpointTemplate.resolveTemplate("id",id)
                .request().delete();

        if(res.getStatus() != 200){
            throw new RuntimeException(res.getStatusInfo().getReasonPhrase());
        }
    }

    public boolean CheckLogin(String username,String password){

        HttpAuthenticationFeature basic = HttpAuthenticationFeature.basic(username,password);

        return clientAuthEndpoint.register(basic).request()
                .get(boolean.class);
    }

}
