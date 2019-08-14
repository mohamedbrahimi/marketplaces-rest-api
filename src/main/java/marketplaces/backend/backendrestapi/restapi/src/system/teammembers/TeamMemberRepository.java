package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import marketplaces.backend.backendrestapi.restapi.src.system.team.Team;
import marketplaces.backend.backendrestapi.restapi.src.system.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TeamMemberRepository extends MongoRepository<TeamMember, String> {

    @Query("{'team.id': ?0, 'member.id': ?0}")
    Optional<TeamMember> findByTeamIdAndMemberId(String teamId, String memberId);
}
