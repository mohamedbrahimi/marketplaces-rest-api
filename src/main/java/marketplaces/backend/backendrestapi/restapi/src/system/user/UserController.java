package marketplaces.backend.backendrestapi.restapi.src.system.user;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/sys/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


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
       userService.insert(user);
    }

    @PutMapping
    public void update(@RequestBody User user) {
       userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }




}
