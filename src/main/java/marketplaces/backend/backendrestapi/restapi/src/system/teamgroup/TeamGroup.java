package marketplaces.backend.backendrestapi.restapi.src.system.teamgroup;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Document(collection = "teamGroups")
public class TeamGroup {

    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;

    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    @NotNull(message = "userId must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING)
    private String userId;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;

    private String roles;
    private String authorities;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate = new Date();

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public List<String> getRoleList(){

        if(this.roles != null && this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<String>();
    }

    public List<String> getAuthoritieList(){

        if(this.authorities != null && this.authorities.length() > 0){
            return Arrays.asList(this.authorities.split(","));
        }
        return new ArrayList<String>();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

}
