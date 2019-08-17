package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.GlobalService;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import marketplaces.backend.backendrestapi.restapi.src.system.team.TeamRepository;
import marketplaces.backend.backendrestapi.restapi.src.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TeamMemberService extends GlobalService<TeamMember, TeamMemberRepository> {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    TeamMemberRepository teamMemberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;

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
        this.CheckAdditionalCriteria(TeamMember.DOC_TEXT, teamMember, Arrays.asList(
                teamRepository,
                userRepository
        ));
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

    void update(TeamMember teamMember){
        this.CheckIfValidDoc(
                TeamMember.DOC_TEXT,
                teamMember,
                Arrays.asList(
                        (teamMember.getTeam() == null) ? "": TeamMember.TEAM_TEXT,
                        (teamMember.getMember() == null) ? "": TeamMember.MEMBER_TEXT
                )
        );
        this.CheckIfNewDoc(
                TeamMember.DOC_TEXT,
                teamMember,
                teamMemberRepository,
                Arrays.asList(
                        teamRepository,
                        userRepository
                )
        );
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(teamMember.getId()));
            Update update = new Update();

            if(teamMember.getTeam() != null) update.set(TeamMember.TEAM_TEXT, teamMember.getTeam());
            if(teamMember.getMember() != null) update.set(TeamMember.MEMBER_TEXT, teamMember.getMember());
            if(Arrays.asList(0, 1).contains(teamMember.getStatus())) update.set(TeamMember.STATUS_TEXT, teamMember.getStatus());

            // TRY TO DO AUDITING SYSTEM ( THIS WORK NEED TO BE AUTOMATICALLY )

            update.set(TeamMember.LAST_MODIFIED_TEXT, new Date());
            update.set(TeamMember.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, TeamMember.class);

        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }


    void delete(String id){

        try {
            teamMemberRepository.deleteById(id);
        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }




}
