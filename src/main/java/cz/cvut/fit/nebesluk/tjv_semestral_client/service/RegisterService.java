package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ClientClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.client.RegisterController;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client.NewClientDto;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Component
public class RegisterService {

    ClientService clientService;

    LoginService loginService;
    ImageService imageService;

    public RegisterService(ClientService clientService_,ImageService imageService_,LoginService loginService_){
        clientService = clientService_;
        loginService = loginService_;
        imageService = imageService_;
    }

    public boolean UserExists(String user){
        return clientService.GetByUsername(user).isPresent();
    }

    public String Register(String username, String realname, MultipartFile picture, String password, HttpServletRequest request){

        NewClientDto dto = new NewClientDto();
        dto.setUsername(username);
        dto.setRealName(realname);
        dto.setPassword(password);

        if(!picture.isEmpty()){
            var image = imageService.CreateImage(picture);
            dto.setImage(image.getImage_id());
        }

        clientService.Register(dto);
        loginService.Login(username,password,request);
        return "home";
    }
}
