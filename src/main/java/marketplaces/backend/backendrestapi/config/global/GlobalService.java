package marketplaces.backend.backendrestapi.config.global;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import marketplaces.backend.backendrestapi.restapi.src.system.pack.Pack;
import marketplaces.backend.backendrestapi.restapi.src.system.pack.PackRepository;
import marketplaces.backend.backendrestapi.restapi.src.system.team.Team;
import marketplaces.backend.backendrestapi.restapi.src.system.team.TeamRepository;
import marketplaces.backend.backendrestapi.restapi.src.system.teammembers.TeamMember;
import marketplaces.backend.backendrestapi.restapi.src.system.teammembers.TeamMemberRepository;
import marketplaces.backend.backendrestapi.restapi.src.system.user.User;
import marketplaces.backend.backendrestapi.restapi.src.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GlobalService<T, R> {

    @Autowired
    MongoTemplate mongoTemplate;
    public void CheckIfValidDoc(String document, T doc, List<String> forFields){
        switch (document) {
            case "USER": {
                User user = (User) doc;

                if (user.getId() != null && !user.getId().matches(GlobalConstants.REGEXP_OBJECTID))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
                if (forFields.contains(user.USERNAME_TEXT) && ( user.getUsername() == null || user.getUsername().length() < 4))
                    throw new ApiRequestException(ExceptionMessages.ERROR_USER_SMALL_THEN_4);
                if (forFields.contains(user.PASSWORD_TEXT) && (user.getPassword() == null || user.getPassword().length() < 8))
                    throw new ApiRequestException(ExceptionMessages.ERROR_PASS_SMALL_THEN_8);
                if (forFields.contains(user.MAIL_TEXT) && (user.getMail() == null || user.getPhone() == null || user.getMail().equals("") || user.getPhone().equals("")))
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if (forFields.contains(user.MAIL_TEXT) && (!user.getMail().matches(GlobalConstants.REGEXP_FOR_MAIL_VALDATION)))
                    throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_MAIL);
                if (forFields.contains(user.PHONE_TEXT) && (!user.getPhone().matches(GlobalConstants.REGEXP_FOR_PHONE_NATIONAL_FORMAT)))
                    throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_PHONE_NUMBER);

            }break;
            case "PACK": {
                Pack pack = (Pack) doc;

                if(pack.getId() != null && !pack.getId().matches(GlobalConstants.REGEXP_OBJECTID))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
                if(forFields.contains(Pack.CODE_TEXT) && pack.getCode() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(Pack.LABEL_TEXT) && pack.getLabel() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(Pack.DESC_TEXT) && pack.getDesc() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);

            }break;
            case "TEAM": {
                Team team = (Team) doc;

                if(team.getId() != null && !team.getId().matches(GlobalConstants.REGEXP_OBJECTID))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
                if(forFields.contains(Team.CODE_TEXT) && team.getCode() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(Team.LABEL_TEXT) && team.getLabel() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(Team.DESC_TEXT) && team.getDesc() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(Team.PACK_TEXT) && team.getPack() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(Team.PACK_TEXT) && team.getPack() != null && (team.getPack().getId() == null || (team.getPack().getId() != null && !team.getPack().getId().matches(GlobalConstants.REGEXP_OBJECTID))))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);

            }break;
            case "TEAM_MEMBERS": {
                TeamMember teamMember = (TeamMember) doc;
                if(teamMember.getId() != null && !teamMember.getId().matches(GlobalConstants.REGEXP_OBJECTID))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
                if(forFields.contains(TeamMember.TEAM_TEXT) && teamMember.getTeam() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(TeamMember.TEAM_TEXT) && teamMember.getTeam() != null && (teamMember.getTeam().getId() == null || (teamMember.getTeam().getId() != null && !teamMember.getTeam().getId().matches(GlobalConstants.REGEXP_OBJECTID))))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
                if(forFields.contains(TeamMember.MEMBER_TEXT) && teamMember.getMember() == null)
                    throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
                if(forFields.contains(TeamMember.MEMBER_TEXT) && teamMember.getMember() != null && (teamMember.getMember().getId() == null || (teamMember.getMember().getId() != null && !teamMember.getMember().getId().matches(GlobalConstants.REGEXP_OBJECTID))))
                    throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
            }break;
            default: break;
        }
    }

    public void CheckIfValidDoc(String document,T doc){
        switch (document) {
            case "USER": {
                CheckIfValidDoc(
                        document,
                        doc,
                        Arrays.asList(
                                User.USERNAME_TEXT,
                                User.MAIL_TEXT,
                                User.PHONE_TEXT,
                                User.PASSWORD_TEXT
                        )
                );
            }break;
            case "PACK": {
                CheckIfValidDoc(
                        document,
                        doc,
                        Arrays.asList(
                                Pack.CODE_TEXT,
                                Pack.LABEL_TEXT,
                                Pack.DESC_TEXT
                        )

                 );
            }break;
            case "TEAM": {
                CheckIfValidDoc(
                        document,
                        doc,
                        Arrays.asList(
                                Team.CODE_TEXT,
                                Team.LABEL_TEXT,
                                Team.DESC_TEXT,
                                Team.PACK_TEXT
                        )
                );
            }break;
            case "TEAM_MEMBERS": {
                CheckIfValidDoc(
                        document,
                        doc,
                        Arrays.asList(
                                TeamMember.TEAM_TEXT,
                                TeamMember.MEMBER_TEXT
                        )
                );
            }
            default: break;
        }
    }
    public void CheckIfNewDoc(String document,T doc, R repository, List repositories){

        switch (document) {
            case "USER": {
                User user = (User) doc;
                UserRepository userRepository = (UserRepository)repository;
                Optional<User> optionalUser = user.getId() == null ? Optional.empty() : userRepository.findById(user.getId());

                if (!optionalUser.equals(Optional.empty())) {

                    if (!optionalUser.get().getUsername().equals(user.getUsername()) && !userRepository.findByUsername(user.getUsername()).equals(Optional.empty())) {
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
                    }
                    if (!optionalUser.get().getMail().equals(user.getMail()) && !userRepository.findByMail(user.getMail()).equals(Optional.empty())) {
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
                    }
                    if (!optionalUser.get().getPhone().equals(user.getPhone()) && !userRepository.findByPhone(user.getPhone()).equals(Optional.empty())) {
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
                    }
                } else if (user.getId() == null) {

                    if (!Optional.empty().equals(userRepository.findByUsername(user.getUsername()))) {
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
                    }

                    if (!userRepository.findByMail(user.getMail()).equals(Optional.empty())) {
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
                    }

                    if (!userRepository.findByPhone(user.getPhone()).equals(Optional.empty())) {
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
                    }
                } else {
                    throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
                }

            }break;
            case "PACK": {
                Pack pack = (Pack) doc;
                PackRepository packRepository = (PackRepository)repository;
                Optional<Pack> optionalPack = pack.getId() == null ? Optional.empty() : packRepository.findById(pack.getId());
                if(!optionalPack.equals(Optional.empty())){

                    if(!optionalPack.get().getCode().equals(pack.getCode()) && !packRepository.findByCode(pack.getCode()).equals(Optional.empty()))
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PACK_CODE);
                }else if(pack.getId() == null){
                    if(!Optional.empty().equals(packRepository.findByCode(pack.getCode())))
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PACK_CODE);
                }else{
                    throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
                }
            }break;
            case "TEAM": {
                /**
                 * for referenced fields we need to check them from there collection
                 * and check if
                 * status: 1 and not archived, ...
                 * referenced field: 'pack'
                 */
                Team team = (Team) doc;
                TeamRepository teamRepository = (TeamRepository)repository;

                Optional<Team> optionalTeam = team.getId() == null ? Optional.empty() : teamRepository.findById(team.getId());

                if(!optionalTeam.equals(Optional.empty())){

                    if(!optionalTeam.get().getCode().equals(team.getCode()) && !teamRepository.findByCode(team.getCode()).equals(Optional.empty()))
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_TEAM_CODE);
                    // ****
                    this.CheckAdditionalCriteria(document, doc, repositories);

                }else if(team.getId() == null){
                    if(!Optional.empty().equals(teamRepository.findByCode(team.getCode())))
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_TEAM_CODE);
                }else{
                    throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
                }
            }break;
            case "TEAM_MEMBERS" : {
                TeamMember teamMember = (TeamMember) doc;
                TeamMemberRepository teamMemberRepository = (TeamMemberRepository) repository;

                Criteria criteria = new Criteria();
                criteria.andOperator(
                        Criteria.where(TeamMember.TEAM_TEXT+"."+GlobalConstants.DEFAULT_DOC_ID).is(teamMember.getTeam().getId()),
                        Criteria.where(TeamMember.MEMBER_TEXT+"."+GlobalConstants.DEFAULT_DOC_ID).is(teamMember.getMember().getId())
                );
                Query query = new Query(criteria);


                Optional<TeamMember> optionalTeamMember = teamMember.getId() == null ? Optional.empty() : teamMemberRepository.findById(teamMember.getId());
                if (!optionalTeamMember.equals(Optional.empty())){
                    if (mongoTemplate.findOne(query, TeamMember.class) != null)
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MEMBER_IN_TEAM);
                    // ***
                    this.CheckAdditionalCriteria(document, doc, repositories);
                }else if (teamMember.getId() == null){

                    if (mongoTemplate.findOne(query, TeamMember.class) != null){
                        throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MEMBER_IN_TEAM);
                    }
                }else{
                    throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
                }

            }break;
            default: break;
        }

    }

    public void CheckIfNewDoc(String document,T doc, R repository){
        CheckIfNewDoc(document, doc, repository, null);
    }

    public void CheckAdditionalCriteria(String document,T doc,
                                        List repositories){
        switch (document){
            case "TEAM": {
                PackRepository packRepository = (PackRepository) repositories.get(0);
                Team team = (Team) doc;
                Optional<Pack> pack = (team == null || team.getPack() == null || team.getPack().getId() == null ) ? Optional.empty() : packRepository.findById(team.getPack().getId());

                if (Optional.empty().equals(pack) || pack.get().getStatus() == 0 || pack.get().getIsArchived() == 1)
                    throw new ApiRequestException(ExceptionMessages.ERROR_PACK_TO_TEAM);
            }break;
            case "TEAM_MEMBERS": {
                System.out.println(repositories);
                if (repositories == null || repositories.size() < 2)
                    this.UnknownException("request not allowed!");

                TeamRepository teamRepository = (TeamRepository) repositories.get(0);
                UserRepository userRepository = (UserRepository) repositories.get(1);
                TeamMember teamMember = (TeamMember) doc;

                Optional<Team> team = (teamMember == null || teamMember.getTeam() == null || teamMember.getTeam().getId() == null ) ? Optional.empty() : teamRepository.findById(teamMember.getTeam().getId());

                if (Optional.empty().equals(team) || team.get().getStatus() == 0 || team.get().getIsArchived() == 1)
                    throw new ApiRequestException(ExceptionMessages.ERROR_USER_TO_TEAM);

                Optional<User> member = (teamMember == null || teamMember.getMember() == null || teamMember.getMember().getId() == null ) ? Optional.empty() : userRepository.findById(teamMember.getMember().getId());

                if (Optional.empty().equals(member) || member.get().getStatus() == 0 || member.get().getIsArchived() == 1)
                    throw new ApiRequestException(ExceptionMessages.ERROR_USER_TO_TEAM);
            }break;
        }
    }
    public void UnknownException(String message) {
        ApiExceptionMessage e = ExceptionMessages.ERROR_UNKNOWN_EXCEPTION;
        e.setAdditionalMessage(message);
        throw new ApiRequestUnknownException(e);
    }
}
