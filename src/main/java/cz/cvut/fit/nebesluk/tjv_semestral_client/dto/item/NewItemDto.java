package cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item;

import java.util.Set;

public class NewItemDto {

    private Long authorId;

    private String name;

    private String description;

    private Set<String> tags;

    private Set<Long> images;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<Long> getImages() {
        return images;
    }

    public void setImages(Set<Long> images) {
        this.images = images;
    }
}
