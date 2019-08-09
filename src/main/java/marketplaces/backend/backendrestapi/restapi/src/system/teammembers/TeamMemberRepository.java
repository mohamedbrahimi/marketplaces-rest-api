package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamMemberRepository extends MongoRepository<TeamMember, String> {
}
