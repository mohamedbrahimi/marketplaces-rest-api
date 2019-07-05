package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;

import marketplaces.backend.backendrestapi.config.global.auditing.Auditable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

@Document(collection = "users")
public class User extends Auditable<String> {

    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;
    @NotNull(message = "username must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String username;
    @NotNull(message = "mail must be not null !!")
    @Indexed(unique = true)
    @Pattern(message = "mail is incorrect !!", regexp = GlobalConstants.REGEXP_FOR_MAIL_VALDATION)
    private String mail;
    @NotNull(message = "phone must be not null !!")
    @Indexed(unique = true)
    @Pattern(message = "phone is incorrect !!", regexp = GlobalConstants.REGEXP_FOR_PHONE_NATIONAL_FORMAT)
    private String phone;
    private String password;
    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;
    private String roles;
    private String authorities;


    public  User(){}

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public int getStatus() {
        return status;
    }


    public String getRoles() {
        return roles;
    }

    public String getAuthorities() {
        return authorities;
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


    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(int status) {
        this.status = status;
    }
 /*
    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }


  */
}
