package marketplaces.backend.backendrestapi.restapi.src.system.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    Optional<User> findByPhone(String phone);

    User save(User user);

    @Query(value = "{}", fields = "{username:1, mail:1, id:0}", sort = "{username:1}")
    List<User> findAll();

}
