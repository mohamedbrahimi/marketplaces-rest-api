package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;

import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/sys/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping
    public List<User> getUsers() {
       /* Query query = new Query();

        query.with(new Sort(Sort.Direction.DESC, "count"));
        query.fields().include("username");
        query.fields().include("mail");
        query.fields().include("phone");
        query.fields().include("roles");
        query.addCriteria(Criteria.where("status").is(1));

        return mongoTemplate.find(query, User.class);

        */

       return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id) {

        return userRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody User user) {

        try {
            user = this.encodePasseord(user);
            userRepository.insert(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.CheckIfValidUser(user);
            this.CheckIfNewUser(user);
            this.UnknownException();
        }
    }

    @PutMapping
    public void update(@RequestBody User user) {
        try {

            this.CheckIfValidUser(user);
            this.CheckIfNewUser(user);
            user = this.encodePasseord(user);

            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(user.getId()));
            Update update = new Update();
            if(user.getUsername() != null) update.set("username",user.getUsername());
            if(user.getMail() != null) update.set("mail",user.getMail());
            if(user.getPhone() != null) update.set("phone",user.getPhone());
            if(user.getPassword() != null) update.set("password",user.getPassword());
            if(user.getRoles() != null) update.set("roles",user.getRoles());
            if(user.getAuthorities() != null) update.set("authorities",user.getAuthorities());
            if(user.getStatus() == 1 && user.getStatus() == 0 ) update.set("status",user.getStatus());

            mongoTemplate.upsert(query, update, User.class);

           // userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            this.UnknownException();
        }

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userRepository.deleteById(id);
    }


    public void CheckIfValidUser(User user) {

        user = (User) user;

        if (user.getId() != null && !user.getId().matches(GlobalConstants.REGEXP_OBJECTID))
            throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
        if (user.getUsername() == null || user.getUsername().length() < 4)
            throw new ApiRequestException(ExceptionMessages.ERROR_USER_SMALL_THEN_4);
        if (user.getPassword() == null || user.getPassword().length() < 8)
            throw new ApiRequestException(ExceptionMessages.ERROR_PASS_SMALL_THEN_8);
        if (user.getMail() == null || user.getPhone() == null || user.getMail().equals("") || user.getPhone().equals(""))
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
        if (!user.getMail().matches(GlobalConstants.REGEXP_FOR_MAIL_VALDATION))
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_MAIL);
        if (!user.getPhone().matches(GlobalConstants.REGEXP_FOR_PHONE_NATIONAL_FORMAT))
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_PHONE_NUMBER);
    }


    public void CheckIfNewUser(User user) {

        User newUser = (User) user;
        Optional<User> optionalUser = user.getId() == null ? Optional.empty() : userRepository.findById(user.getId());


        if (!optionalUser.equals(Optional.empty())) {

            if (!optionalUser.get().getUsername().equals(newUser.getUsername()) && !userRepository.findByUsername(newUser.getUsername()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
            }

            if (!optionalUser.get().getMail().equals(newUser.getMail()) && !userRepository.findByMail(newUser.getMail()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
            }

            if (!optionalUser.get().getPhone().equals(newUser.getPhone()) && !userRepository.findByPhone(newUser.getPhone()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
            }
        } else if (user.getId() == null) {

            if (!Optional.empty().equals(userRepository.findByUsername(newUser.getUsername()))) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
            }

            if (!userRepository.findByMail(newUser.getMail()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
            }


            if (!userRepository.findByPhone(newUser.getPhone()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
            }
        } else {
            throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
        }


    }


    public void UnknownException() {
        throw new ApiRequestUnknownException(ExceptionMessages.ERROR_UNKNOWN_EXCEPTION);
    }


    public User encodePasseord(User user) {
        // a lot lines of codes to test if we need to encode the password
        // Need to minimize this by mongo hooks

        if (user.getPassword() == null)
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);

        if (user.getId() == null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        else {

            Optional<User> oldUser = userRepository.findById(user.getId());
            String oldPassword = oldUser.equals(Optional.empty()) ? null : oldUser.get().getPassword();
            if (oldPassword == null) {

                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            } else if (!new BCryptPasswordEncoder().matches(user.getPassword(), oldUser.get().getPassword())) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }

        }

        return user;

    }

}
