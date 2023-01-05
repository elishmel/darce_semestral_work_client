package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ClientClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.ClientDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.NewClientDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Optional;

@Component
public class ClientService {

    ClientClient clientClient;

    public ClientService(ClientClient clientClient_){
        clientClient = clientClient_;
    }

    public Optional<ClientDto> GetByUsername(String username){
        return clientClient.GetByUsername(username);
    }

    public ClientDto GetById(Long id){
        return clientClient.GetById(id);
    }

    public ClientDto Register(NewClientDto dto){
        return clientClient.Register(dto);
    }

    public boolean Login(String username, String password){
        var base = Base64.encodeBase64String((username+':'+password).getBytes(Charset.forName("UTF-8")));
        return clientClient.CheckLogin(base);
    }

}
