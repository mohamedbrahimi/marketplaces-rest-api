package marketplaces.backend.backendrestapi.restapi.src.system.user;

import org.bson.types.ObjectId;
import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/sys/users")
public class UserController{

    @Autowired
    private UserRepository userRepository;


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
        try {
            userRepository.save(user);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            this.CheckIfValidUser(user);
            this.CheckIfNewUser(user);
            this.UnknownException();
        }

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userRepository.deleteById(id);
    }


    public void CheckIfValidUser(User user) {

        user = (User)user;

        if( user.getId() != null && !user.getId().matches("/^(?=[a-f\\d]{24}$)(\\d+[a-f]|[a-f]+\\d)/i"))
            throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
        if( user.getUsername() == null || user.getUsername().length() < 4)
            throw new ApiRequestException(ExceptionMessages.ERROR_USER_SMALL_THEN_4);
        if( user.getPassword() == null || user.getPassword().length() < 8)
            throw new ApiRequestException(ExceptionMessages.ERROR_PASS_SMALL_THEN_8);
        if( user.getMail() == null || user.getPhone() == null || user.getMail().equals("") || user.getPhone().equals("")  )
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
        if( !user.getMail().matches("^(.+)@(.+)$")  )
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_MAIL);
        if( !user.getPhone().matches("^\\+\\d{8,14}$")  )
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_PHONE_NUMBER);
    }


    public void CheckIfNewUser(User user) {

        User newUser = (User)user;
        Optional<User> optionalUser = user.getId() == null ? Optional.empty() : userRepository.findById(user.getId());


        if( !optionalUser.equals(Optional.empty()) ) {
            if(!optionalUser.get().getUsername().equals(newUser.getUsername()) && !userRepository.findByUsername(newUser.getUsername()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
            }

            if( !optionalUser.get().getMail().equals(newUser.getMail()) && !userRepository.findByMail(newUser.getMail()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
            }

            if(!optionalUser.get().getPhone().equals(newUser.getPhone()) && !userRepository.findByPhone(newUser.getPhone()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
            }
        }
        else if(user.getId() == null)  {

            if(!Optional.empty().equals(userRepository.findByUsername(newUser.getUsername()))   ) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
            }

            if(!userRepository.findByMail(newUser.getMail()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
            }


            if(!userRepository.findByPhone(newUser.getPhone()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
            }
        }
        else {
            throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
        }




    }



    public void UnknownException() {
        throw new ApiRequestUnknownException(ExceptionMessages.ERROR_UNKNOWN_EXCEPTION);
    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
