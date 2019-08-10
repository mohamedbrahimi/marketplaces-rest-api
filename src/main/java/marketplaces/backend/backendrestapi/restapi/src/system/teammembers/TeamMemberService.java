package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import marketplaces.backend.backendrestapi.config.global.GlobalService;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TeamMemberService extends GlobalService<TeamMember, TeamMemberRepository> {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    TeamMemberRepository teamMemberRepository;

    public Page<TeamMember> find(Filtering filtering){
        Pageable pageable = PageRequest.of(filtering.getPage(), filtering.getSize());
        Criteria criteria = new Criteria();

        if( Arrays.asList(0, 1).contains(filtering.getStatus()) )
            criteria.and(TeamMember.STATUS_TEXT).is(filtering.getStatus());

        Query query = new Query(criteria).with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "count"));

        query.fields().include(TeamMember.TEAM_TEXT);
        query.fields().include(TeamMember.MEMBER_TEXT);
        query.fields().include(TeamMember.STATUS_TEXT);

        List<TeamMember> list = mongoTemplate.find(query, TeamMember.class);
        return PageableExecutionUtils.getPage(
                list,
                pageable,
                ()-> mongoTemplate.count(query, TeamMember.class)
        );
    }

    void insert(TeamMember teamMember){
        try{
            mongoTemplate.insert(teamMember);
        }catch (Exception e){

            this.CheckIfValidDoc(
                    TeamMember.DOC_TEXT,
                    teamMember
            );
            this.CheckIfNewDoc(
                    TeamMember.DOC_TEXT,
                    teamMember,
                    teamMemberRepository
            );
            this.UnknownException(e.getMessage());
        }
    }


}
