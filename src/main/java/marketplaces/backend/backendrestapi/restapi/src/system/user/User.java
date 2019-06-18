package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.exceptions.ApiException;
import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessageBody;
import marketplaces.backend.backendrestapi.config.exceptions.ApiRequestException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    @NotNull(message = "username must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String username;
    @NotNull(message = "mail must be not null !!")
    @Indexed(unique = true)
    @Pattern(message = "mail is incorrect !!", regexp = "^(.+)@(.+)$")
    private String mail;
    @NotNull(message = "phone must be not null !!")
    @Indexed(unique = true)
    @Pattern(message = "phone is incorrect !!", regexp = "^\\+\\d{8,14}$")
    private String phone;
    private String password;
    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;
    private String roles;
    private String authorities;

    protected  User(){}

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
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getAuthoritieList(){
        if(this.authorities.length() > 0){
            return Arrays.asList(this.authorities.split(","));
        }
        return new ArrayList<>();
    }

    public void CheckIfValidUser() {


        if( this.username.length() < 4)
            throw new ApiRequestException( new ApiExceptionMessage("USER_SMALL_THEN_4",
                    new ApiExceptionMessageBody(
                            "le nom doit être supérieur à 4 !!",
                            "",
                            "The name must be bigger then 4 caracters !!")));
        if( this.password.length() < 8)
            throw new ApiRequestException( new ApiExceptionMessage("PASS_SMALL_THEN_8",
                    new ApiExceptionMessageBody(
                            "le mot de passe doit être supérieur à 8 !!",
                            "",
                            "The password must be bigger then 8 caracters !!")));
        if( this.mail.equals("") || this.phone.equals("")  )
            throw new ApiRequestException( new ApiExceptionMessage("FIELD_NULL",
                    new ApiExceptionMessageBody(
                            "Veuillez envoyer tous les champs !!",
                            "",
                            "Try to send all fields !!")));
        if( this.mail.matches("^(.+)@(.+)$")  )
            throw new ApiRequestException( new ApiExceptionMessage("INVALID_MAIL",
                    new ApiExceptionMessageBody(
                            "Adresse email non valide !!",
                            "",
                            "Address email not valid !!")));
        if( this.phone.matches("^\\+\\d{8,14}$")  )
            throw new ApiRequestException( new ApiExceptionMessage("INVALID_PHONE_NUMBER",
                    new ApiExceptionMessageBody(
                            "Un numéro du télephone incorrect !!",
                            "",
                            "The number phone in incorrect !!")));


    }
}
