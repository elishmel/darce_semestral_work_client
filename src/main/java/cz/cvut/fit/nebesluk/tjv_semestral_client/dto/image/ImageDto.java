package cz.cvut.fit.nebesluk.tjv_semestral_client.dto.image;

public class ImageDto {
    private Long image_id;
    private String url;

    public Long getImage_id() {
        return image_id;
    }

    public void setImage_id(Long image_id) {
        this.image_id = image_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
