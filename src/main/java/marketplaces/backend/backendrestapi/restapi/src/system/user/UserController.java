package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessageBody;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/users")
public class UserController{

    private UserRepository userRepository;


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("all")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        return userRepository.findById(id);
    }

    @PutMapping
    public void insert(@RequestBody User user) {
        try{

            userRepository.insert(user);
        }catch (Exception e){

            this.CheckIfValidUser(user);
            this.CheckIfNewUser(user);
            this.UnknownException();
        }
    }

    @PostMapping
    public  void update(@RequestBody User user){
            userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userRepository.deleteById(id);
    }


    public void CheckIfValidUser(User user) {

        user = (User)user;
        if( user.getUsername().length() < 4)
            throw new ApiRequestException( new ApiExceptionMessage("USER_SMALL_THEN_4",
                    new ApiExceptionMessageBody(
                            "le nom doit être supérieur à 4 !!",
                            "",
                            "The name must be bigger then 4 caracters !!")));
        if( user.getPassword().length() < 8)
            throw new ApiRequestException( new ApiExceptionMessage("PASS_SMALL_THEN_8",
                    new ApiExceptionMessageBody(
                            "le mot de passe doit être supérieur à 8 !!",
                            "",
                            "The password must be bigger then 8 caracters !!")));
        if( user.getMail().equals("") || user.getPhone().equals("")  )
            throw new ApiRequestException( new ApiExceptionMessage("FIELD_NULL",
                    new ApiExceptionMessageBody(
                            "Veuillez remplir tous les champs !!",
                            "",
                            "Try to send all fields !!")));
        if( !user.getMail().matches("^(.+)@(.+)$")  )
            throw new ApiRequestException( new ApiExceptionMessage("INVALID_MAIL",
                    new ApiExceptionMessageBody(
                            "Adresse email non valide !!",
                            "",
                            "Address email not valid !!")));
        if( !user.getPhone().matches("^\\+\\d{8,14}$")  )
            throw new ApiRequestException( new ApiExceptionMessage("INVALID_PHONE_NUMBER",
                    new ApiExceptionMessageBody(
                            "Un numéro du télephone incorrect !!",
                            "",
                            "The number phone in incorrect !!")));
    }


    public void CheckIfNewUser(User user) {

        user = (User)user;

        if(!userRepository.findByUsername(user.getUsername()).equals(Optional.empty())) {
                throw new ApiRequestException( new ApiExceptionMessage("EXISTING_USERNAME",
                        new ApiExceptionMessageBody(
                                "Veuillez changer ce nom d'utilisatuer !!",
                                "",
                                "You need to choose another username!!")));
        }

        if(!userRepository.findByMail(user.getMail()).equals(Optional.empty())) {
            throw new ApiRequestException( new ApiExceptionMessage("EXISTING_MAIL",
                    new ApiExceptionMessageBody(
                            "Veuillez changer cette adresse mail!!",
                            "",
                            "You need to pass another address mail!!")));
        }

        if(!userRepository.findByPhone(user.getPhone()).equals(Optional.empty())) {
            throw new ApiRequestException( new ApiExceptionMessage("EXISTING_PHONE",
                    new ApiExceptionMessageBody(
                            "Veuillez changer ce numero du telephone!!",
                            "",
                            "You need to pass another phone number l!!")));
        }
    }

    public void UnknownException() {
        throw new ApiRequestUnknownException( new ApiExceptionMessage("UNKNOWN_EXCEPTION",
                new ApiExceptionMessageBody(
                        "Essaies de passer des donnees correct !!",
                        "",
                        "Try to pass a correct data !!")));
    }
}
