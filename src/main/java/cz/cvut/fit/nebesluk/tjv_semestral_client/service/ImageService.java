package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.ImageClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.image.ImageDto;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Component
public class ImageService {

    ImageClient imageClient;
    
    public ImageService(ImageClient imageClient_){
        imageClient = imageClient_;
    }
    
    public ImageDto GetById(Long id){
        return imageClient.GetById(id);
    }

    public Collection<ImageDto> GetAll(){
        return imageClient.GetAll();
    }

    public ImageDto CreateImage(MultipartFile image){


        final FormDataMultiPart fdmp = new FormDataMultiPart();
        try{
            image.transferTo(Paths.get(image.getOriginalFilename()));
            fdmp.bodyPart(new FileDataBodyPart("image",new File(image.getOriginalFilename())));
        } catch (IOException e){
            return null;
        }
        var r = imageClient.CreateImage(fdmp);
        try{
            Files.delete(Paths.get(image.getOriginalFilename()));
        } catch (IOException e){

        }
        return r;
    }

}
