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
}
