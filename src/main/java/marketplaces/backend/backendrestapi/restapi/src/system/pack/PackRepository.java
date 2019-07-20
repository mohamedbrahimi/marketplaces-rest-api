package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PackRepository extends MongoRepository<Pack, String> {
    Optional<Pack> findById(String id);
    Optional<Pack> findByCode(String code);
}
