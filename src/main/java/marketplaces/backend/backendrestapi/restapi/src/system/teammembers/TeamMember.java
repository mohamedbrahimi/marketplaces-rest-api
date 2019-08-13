package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.auditing.Auditable;
import marketplaces.backend.backendrestapi.restapi.src.system.team.Team;
import marketplaces.backend.backendrestapi.restapi.src.system.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "teamGroups")
@CompoundIndexes({
        // be careful if you want to update this compound index.
        //
        @CompoundIndex(
                       name = "member_team_idx",
                       unique = true,
                       def = "{'member': 1, 'team': 1}")
})
public class TeamMember extends Auditable<String> {

    static public String DOC_TEXT = "TEAM_MEMBERS";

    static public String MEMBER_TEXT = "member";
    static public String TEAM_TEXT = "team";
    static public String STATUS_TEXT = "status";
    static public String IS_ARCHIVED_TEXT = "isArchived";

    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;

    @NotNull(message = "user must be not null !!")
    @DBRef
    private User member;

    @NotNull(message = "team must be not null !!")
    @DBRef
    private Team team;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;

    private String roles;
    private String authorities;

    public String getId() {
        return id;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public void setUser(User user) {
        this.member = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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


}
