package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;

import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;





    @GetMapping
    public Page<User> getUsers(@RequestHeader int size, @RequestHeader int page, @RequestHeader String text,@RequestHeader int status ) {
        return userService.find(new Filtering(size, page, text, status));
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id) {

        return userRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody User user) {

        try {
            user = userService.encodePasseord(user);
            userRepository.insert(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            userService.CheckIfValidUser(user);
            userService.CheckIfNewUser(user);
            userService.UnknownException(e.getMessage());
        }
    }

    @PutMapping
    public void update(@RequestBody User user) {

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userRepository.deleteById(id);
    }




}
