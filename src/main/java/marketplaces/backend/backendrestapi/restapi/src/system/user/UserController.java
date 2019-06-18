package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessageBody;
import marketplaces.backend.backendrestapi.config.exceptions.ApiRequestException;
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
        }catch (ApiRequestException e){

        }catch (Exception e){
            user.CheckIfValidUser();

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
}
