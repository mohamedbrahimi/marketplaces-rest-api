package marketplaces.backend.backendrestapi.restapi.src.system.team;

import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.GlobalService;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import marketplaces.backend.backendrestapi.restapi.src.system.pack.PackRepository;
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
public class TeamService extends GlobalService<Team, TeamRepository> {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PackRepository packRepository;

    public Page<Team> find(Filtering filtering){
        Pageable pageable = PageRequest.of(filtering.getPage(), filtering.getSize());

        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where(Team.CODE_TEXT).regex(filtering.getText()),
                Criteria.where(Team.LABEL_TEXT).regex(filtering.getText()),
                Criteria.where(Team.DESC_TEXT).regex(filtering.getText())

        );
        if(Arrays.asList(0, 1).contains(filtering.getStatus()))
            criteria.and(Team.STATUS_TEXT).is(filtering.getStatus());

        Query query = new Query(criteria).with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "count"));

        query.fields().include(Team.CODE_TEXT);
        query.fields().include(Team.LABEL_TEXT);
        query.fields().include(Team.DESC_TEXT);
        query.fields().include(Team.PACK_TEXT).include("desc");
        query.fields().include(Team.STATUS_TEXT);

        List<Team> list = mongoTemplate.find(query, Team.class);
        return PageableExecutionUtils.getPage(
                list,
                pageable,
                ()-> mongoTemplate.count(query, Team.class)
        );
    }


    void insert(Team team){
        this.CheckAdditionalCriteria(Team.DOC_TEXT, team, Arrays.asList(packRepository));

        try{
            mongoTemplate.insert(team);
        }catch (Exception e){
            this.CheckIfValidDoc(
                    Team.DOC_TEXT,
                    team
            );

            this.CheckIfNewDoc(
                    Team.DOC_TEXT,
                    team,
                    teamRepository
            );

            this.UnknownException(e.getMessage());

        }
    }

    void update(Team team){
        this.CheckIfValidDoc(
                Team.DOC_TEXT,
                team,
                Arrays.asList(
                        (team.getCode() == null) ? "": Team.CODE_TEXT,
                        (team.getLabel() == null) ? "": Team.LABEL_TEXT,
                        (team.getDesc() == null) ? "": Team.DESC_TEXT,
                        (team.getPack() == null) ? "": Team.PACK_TEXT
                )
        );

        this.CheckIfNewDoc(
                Team.DOC_TEXT,
                team,
                teamRepository,
                Arrays.asList(
                        packRepository
                )
        );

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(team.getId()));
            Update update = new Update();

            if (team.getCode() != null) update.set(Team.CODE_TEXT, team.getCode());
            if (team.getLabel() != null) update.set(Team.LABEL_TEXT, team.getLabel());
            if (team.getDesc() != null) update.set(Team.DESC_TEXT, team.getDesc());
            if (team.getPack() != null) update.set(Team.PACK_TEXT, team.getPack());
            if (Arrays.asList(0, 1).contains(team.getStatus())) update.set(Team.STATUS_TEXT, team.getStatus());

            // TRY TO DO AUDITING SYSTEM ( THIS WORK NEED TO BE AUTOMATICALLY )

            update.set(Team.LAST_MODIFIED_TEXT, new Date());
            update.set(Team.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, Team.class);
        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }

    void delete(String id){
        Optional<Team> optionalTeam = teamRepository.findById(id);
        Team team = Optional.empty().equals(optionalTeam) ? null : optionalTeam.get();
        if(team == null || team.getIsArchived() == 1)
            throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
        try {
            Query query = new Query();
            String currentDate = (new Date()).toString();

            query.addCriteria(Criteria.where("id").is(id));
            query.addCriteria(Criteria.where(Team.IS_ARCHIVED_TEXT).is(0));

            Update update = new Update();
            update.set(Team.STATUS_TEXT, 0);
            update.set(Team.IS_ARCHIVED_TEXT, 1);
            update.set(Team.CODE_TEXT, team.getCode()+GlobalConstants.DEFAULT_SUFFIX_OF_ARCHIVED_DOC+ currentDate);

            update.set(Team.LAST_MODIFIED_TEXT, new Date());
            update.set(Team.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, Team.class);

        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }

}
