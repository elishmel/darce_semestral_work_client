package cz.cvut.fit.nebesluk.tjv_semestral_client.dto.tag;

public class TagDto {

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TagDto)) return false;
        return tag.equals(((TagDto)obj).getTag());
    }
}
