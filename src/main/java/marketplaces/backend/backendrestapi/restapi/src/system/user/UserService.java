package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepository userRepository;

    Page<User> find(Filtering filtering){
        Pageable pageable = PageRequest.of(filtering.getPage(), filtering.getSize());

        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where(User.USERNAME_TEXT).regex(filtering.getText()),
                Criteria.where(User.MAIL_TEXT).regex(filtering.getText()),
                Criteria.where(User.PHONE_TEXT).regex(filtering.getText()),
                Criteria.where(User.ROLES_TEXT).regex(filtering.getText()),
                Criteria.where(User.AUTHORITIES_TEXT).regex(filtering.getText())
        );

        query.addCriteria(Criteria.where(User.USERNAME_TEXT).regex(filtering.getText()));

        query.with(new Sort(Sort.Direction.DESC, "count"));
        query.fields().include(User.USERNAME_TEXT);
        query.fields().include(User.MAIL_TEXT);
        query.fields().include(User.MAIL_TEXT);
        query.fields().include(User.ROLES_TEXT);
        query.addCriteria(Criteria.where(User.STATUS_TEXT).is(1));

        List<User> list =  mongoTemplate.find(query, User.class);
        return PageableExecutionUtils.getPage(
                list,
                pageable,
                ()-> mongoTemplate.count(query, User.class)
        );

    }
}
