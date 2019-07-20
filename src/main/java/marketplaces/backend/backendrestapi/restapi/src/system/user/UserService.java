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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

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
            this.CheckIfValidUser(user);
            this.CheckIfNewUser(user);
            this.UnknownException(e.getMessage());
        }
    }


    void update(User user){
        // need to perform this function.
        user = this.encodePasseord(user);
        this.CheckIfValidUser(user, Arrays.asList(
                (user.getUsername() == null) ? "": User.USERNAME_TEXT,
                (user.getMail() == null) ? "": User.MAIL_TEXT,
                (user.getPassword() == null) ? "": User.PASSWORD_TEXT,
                (user.getPhone() == null) ? "": User.PHONE_TEXT
        ));
        this.CheckIfNewUser(user);
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



    /*
    do some illegal treatments !!
    these functions must be more organized
    */

    public void CheckIfValidUser(User user, List<String> forFields) {

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
    }

    public void CheckIfValidUser(User user){

        CheckIfValidUser(user, Arrays.asList(
                User.USERNAME_TEXT,
                User.MAIL_TEXT,
                User.PHONE_TEXT,
                User.PASSWORD_TEXT
        ));
    }


    public void CheckIfNewUser(User user) {

        User newUser = (User) user;
        Optional<User> optionalUser = user.getId() == null ? Optional.empty() : userRepository.findById(user.getId());

        if (!optionalUser.equals(Optional.empty())) {

            if (!optionalUser.get().getUsername().equals(newUser.getUsername()) && !userRepository.findByUsername(newUser.getUsername()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
            }
            if (!optionalUser.get().getMail().equals(newUser.getMail()) && !userRepository.findByMail(newUser.getMail()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
            }
            if (!optionalUser.get().getPhone().equals(newUser.getPhone()) && !userRepository.findByPhone(newUser.getPhone()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
            }
        } else if (user.getId() == null) {

            if (!Optional.empty().equals(userRepository.findByUsername(newUser.getUsername()))) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_USERNAME);
            }

            if (!userRepository.findByMail(newUser.getMail()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_MAIL);
            }


            if (!userRepository.findByPhone(newUser.getPhone()).equals(Optional.empty())) {
                throw new ApiRequestException(ExceptionMessages.ERROR_EXISTING_PHONE);
            }
        } else {
            throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
        }


    }


    public void UnknownException(String message) {

        ApiExceptionMessage e = ExceptionMessages.ERROR_UNKNOWN_EXCEPTION;
        e.setAdditionalMessage(message);
        throw new ApiRequestUnknownException(e);
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
