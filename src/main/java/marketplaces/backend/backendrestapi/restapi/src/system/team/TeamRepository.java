package marketplaces.backend.backendrestapi.restapi.src.system.team;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, String> {
    Optional<Team> findByCode(String code);
}
