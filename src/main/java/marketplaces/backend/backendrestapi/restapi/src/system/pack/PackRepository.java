package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PackRepository extends MongoRepository<Pack, String> {

    @Query(fields = "{code: 1, label: 1, desc: 1, status: 1, isArchived: 1}")
    Optional<Pack> findById(String id);

    @Query(fields = "{code: 1, label: 1, desc: 1, status: 1, isArchived: 1}")
    Optional<Pack> findByCode(String code);
}
