package marketplaces.backend.backendrestapi.restapi.src.system.user;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService extends GlobalService<User, UserRepository> {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepository userRepository;

    public Page<User> find(Filtering filtering){
        Pageable pageable = PageRequest.of(filtering.getPage(), filtering.getSize());

        Criteria criteria = new Criteria();
        criteria.orOperator( // NEED TO IMPLEMENT ES (ELASTICSEARCH).
                Criteria.where(User.USERNAME_TEXT).regex(filtering.getText()),
                Criteria.where(User.MAIL_TEXT).regex(filtering.getText()),
                Criteria.where(User.PHONE_TEXT).regex(filtering.getText()),
                Criteria.where(User.ROLES_TEXT).regex(filtering.getText()),
                Criteria.where(User.AUTHORITIES_TEXT).regex(filtering.getText())
        );

        if( Arrays.asList(0, 1).contains(filtering.getStatus()) )
        criteria.andOperator(Criteria.where(User.STATUS_TEXT).is(filtering.getStatus()));

        Query query = new Query(criteria).with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "count"));

        query.fields().include(User.USERNAME_TEXT);
        query.fields().include(User.MAIL_TEXT);
        query.fields().include(User.PHONE_TEXT);
        query.fields().include(User.ROLES_TEXT);
        query.fields().include(User.AUTHORITIES_TEXT);
        query.fields().include(User.STATUS_TEXT);


        List<User> list =  mongoTemplate.find(query, User.class);
        return PageableExecutionUtils.getPage(
                list,
                pageable,
                ()-> mongoTemplate.count(query, User.class)
        );

    }

    void insert(User user){
        try {
            user = this.encodePasseord(user);
            mongoTemplate.insert(user);
        } catch (Exception e) {
            this.CheckIfValidDoc(
                    User.DOC_TEXT,
                    user);
            this.CheckIfNewDoc(
                    User.DOC_TEXT,
                    user,
                    userRepository
                    );
            this.UnknownException(e.getMessage());
        }
    }


    void update(User user){
        // need to perform this function.
        user = this.encodePasseord(user);
        this.CheckIfValidDoc(
                User.DOC_TEXT,
                user,
                Arrays.asList(
                (user.getUsername() == null) ? "": User.USERNAME_TEXT,
                (user.getMail() == null) ? "": User.MAIL_TEXT,
                (user.getPassword() == null) ? "": User.PASSWORD_TEXT,
                (user.getPhone() == null) ? "": User.PHONE_TEXT
        ));
        this.CheckIfNewDoc(
                User.DOC_TEXT,
                user,
                userRepository
        );
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(user.getId()));
            Update update = new Update();
            if(user.getUsername() != null) update.set(User.USERNAME_TEXT,user.getUsername());
            if(user.getMail() != null) update.set(User.MAIL_TEXT,user.getMail());
            if(user.getPhone() != null) update.set(User.PHONE_TEXT,user.getPhone());
            if(user.getPassword() != null) update.set(User.PASSWORD_TEXT,user.getPassword());
            if(user.getRoles() != null) update.set(User.ROLES_TEXT,user.getRoles());
            if(user.getAuthorities() != null) update.set(User.AUTHORITIES_TEXT,user.getAuthorities());
            if(user.getStatus() == 1 || user.getStatus() == 0 ) update.set(User.STATUS_TEXT,user.getStatus());

            // TRY TO DO AUDITING SYSTEM ( THIS WORK NEED TO BE AUTOMATICALLY )

            update.set(User.LAST_MODIFIED_TEXT, new Date());
            update.set(User.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, User.class);

        }catch(Exception e){
            this.UnknownException(e.getMessage());
        }

    }

    void delete(String id){

        Optional<User> optionalUser = userRepository.findById(id);
        User user = Optional.empty().equals(optionalUser) ? null : optionalUser.get();

        if(user == null || user.getIsArchived() == 1)
            throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);

        try {


            Query query = new Query();

            query.addCriteria(Criteria.where("id").is(id));
            query.addCriteria(Criteria.where(User.IS_ARCHIVED_TEXT).is(0));

            Update update = new Update();

            update.set(User.STATUS_TEXT, 0);
            update.set(User.IS_ARCHIVED_TEXT, 1);
            String currentDate = (new Date()).toString();
            update.set(User.USERNAME_TEXT, user.getUsername()+ "_ARCHIVED_"+ currentDate);
            update.set(User.PHONE_TEXT, user.getPhone()+ "_ARCHIVED_"+ currentDate);
            update.set(User.MAIL_TEXT, user.getMail()+ "_ARCHIVED_"+ currentDate);

            update.set(User.LAST_MODIFIED_TEXT, new Date());
            update.set(User.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, User.class);
        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }


    public User encodePasseord(User user) {
        // a lot lines of codes to test if we need to encode the password
        // Need to minimize this by mongo hooks

        if (user.getPassword() == null && user.getId() == null) // new instance.
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);

        if (user.getPassword() == null && user.getId() != null) { // not need to update the password.
            return user;
        }
        else {

            Optional<User> oldUser = userRepository.findById(user.getId());
            String oldPassword = oldUser.equals(Optional.empty()) ? null : oldUser.get().getPassword();
            if (oldPassword == null) {

                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            } else if (!new BCryptPasswordEncoder().matches(user.getPassword(), oldUser.get().getPassword())) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }

        }

        return user;

    }
}
