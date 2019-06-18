package marketplaces.backend.backendrestapi.restapi.src.system.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findById(String id);
}
