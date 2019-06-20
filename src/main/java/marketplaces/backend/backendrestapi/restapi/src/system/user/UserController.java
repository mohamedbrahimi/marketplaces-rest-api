package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessageBody;
import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            user = (User)user;
            user.setPassword(this.passwordEncoder().encode(user.getPassword()));
            userRepository.insert(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
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
            throw new ApiRequestException(ExceptionMessages.ERROR_USER_SMALL_THEN_4);
        if( user.getPassword().length() < 8)
            throw new ApiRequestException(ExceptionMessages.ERROR_PASS_SMALL_THEN_8);
        if( user.getMail().equals("") || user.getPhone().equals("")  )
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
        if( !user.getMail().matches("^(.+)@(.+)$")  )
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_MAIL);
        if( !user.getPhone().matches("^\\+\\d{8,14}$")  )
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_PHONE_NUMBER);
    }


    public void CheckIfNewUser(User user) {

        user = (User)user;

        if(!userRepository.findByUsername(user.getUsername()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
        }

        if(!userRepository.findByMail(user.getMail()).equals(Optional.empty())) {
            throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
        }

        if(!userRepository.findByPhone(user.getPhone()).equals(Optional.empty())) {
            throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
        }
    }

    public void UnknownException() {
        throw new ApiRequestUnknownException(ExceptionMessages.ERROR_UNKNOWN_EXCEPTION);
    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
