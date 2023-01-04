package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import cz.cvut.fit.nebesluk.tjv_semestral_client.apiClient.TagClient;
import cz.cvut.fit.nebesluk.tjv_semestral_client.dto.tag.TagDto;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TagService {

    TagClient tagClient;

    TagService(TagClient tagClient_){
        tagClient = tagClient_;
    }

    public Collection<TagDto> GetAll(){
        return tagClient.GetAllTags();
    }

}
