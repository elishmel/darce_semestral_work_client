package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.ClientDto;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.NewClientDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserService {

    ClientService clientService;

    ImageService imageService;

    public UserService(ClientService clientService_,ImageService imageService_){
        clientService = clientService_;
        imageService = imageService_;
    }


    public ClientDto Edit(Long id,String user, String name, String pwd, MultipartFile image){
        NewClientDto client = new NewClientDto();

        var c = clientService.GetById(id);
        if(image.isEmpty()){
            client.setImage(c.getProfilePicture());
        } else {
            var img = imageService.CreateImage(image);
            client.setImage(img.getImage_id());
        }


        client.setPassword(pwd);
        client.setRealName(name);
        client.setUsername(user);

        return clientService.UpdateById(id,client).orElseThrow();


    }


}
