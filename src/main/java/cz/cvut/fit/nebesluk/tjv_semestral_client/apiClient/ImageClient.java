package cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient;

import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.image.ImageDto;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collection;

@Component
public class ImageClient {
    private WebTarget imageEndpoint;

    private WebTarget specificImageEndpointTemplate;

    public ImageClient(@Value("${server.url}")String apiUrl){
        var client = ClientBuilder.newClient();
        imageEndpoint = client.target(apiUrl+"/api/image");
        imageEndpoint.register(MultiPartFeature.class);
        specificImageEndpointTemplate = imageEndpoint.path("/{id}");
    }

    public ImageDto GetById(Long id){
        return specificImageEndpointTemplate.resolveTemplate("id",id).request()
                .get(ImageDto.class);
    }

    public Collection<ImageDto> GetAll(){
        return Arrays.stream(imageEndpoint.request().get(ImageDto[].class)).toList();
    }

    public ImageDto CreateImage(FormDataMultiPart image){
        return imageEndpoint.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(image,MediaType.MULTIPART_FORM_DATA_TYPE),ImageDto.class);
    }
}
