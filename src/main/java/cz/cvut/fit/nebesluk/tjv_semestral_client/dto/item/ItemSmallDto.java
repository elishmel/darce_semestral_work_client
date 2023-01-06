package cz.cvut.fit.nebesluk.tjv_semestral_client.dto.item;

public class ItemSmallDto {
    private Long itemId;

    private Long author;

    private String name;

    private Long[] images;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean offer;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public Long[] getImages() {
        return images;
    }

    public void setImages(Long[] images) {
        this.images = images;
    }
}
