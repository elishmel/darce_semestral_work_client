package cz.cvut.fit.nebesluk.tjv_semestral_client.dto.client;

import java.net.URI;
import java.time.LocalDateTime;

public class ClientDto {

    private Long client_id;

    private String username;

    private String realName;

    private LocalDateTime dateCreated;

    private LocalDateTime dateLastLogon;

    private String profilePicture;

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateLastLogon() {
        return dateLastLogon;
    }

    public void setDateLastLogon(LocalDateTime dateLastLogon) {
        this.dateLastLogon = dateLastLogon;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
