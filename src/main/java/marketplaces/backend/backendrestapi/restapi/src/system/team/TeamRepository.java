package marketplaces.backend.backendrestapi.restapi.src.system.team;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {

}
