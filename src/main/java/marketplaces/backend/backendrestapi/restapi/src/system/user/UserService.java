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

        System.out.println(filtering.getStatus());
        System.out.println(Arrays.asList(0, 1).contains(filtering.getStatus()));


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

    User update(User user){
        // need to perform this function.
        user = this.encodePasseord(user);
        this.CheckIfValidUser(user);
        this.CheckIfNewUser(user);
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(user.getId()));
            Update update = new Update();
            if(user.getUsername() != null) update.set("username",user.getUsername());
            if(user.getMail() != null) update.set("mail",user.getMail());
            if(user.getPhone() != null) update.set("phone",user.getPhone());
            if(user.getPassword() != null) update.set("password",user.getPassword());
            if(user.getRoles() != null) update.set("roles",user.getRoles());
            if(user.getAuthorities() != null) update.set("authorities",user.getAuthorities());
            if(user.getStatus() == 1 || user.getStatus() == 0 ) update.set("status",user.getStatus());

            try {
                mongoTemplate.findAndModify(query, update, User.class);
                //  userRepository.save(user);
            } catch (Exception e) {

                this.UnknownException(e.getMessage());
            }

        }catch(Exception e){
            this.UnknownException(e.getMessage());
        }

        return null;
    }



    /*
    do some illegal treatments !!
    these functions must be more organized
    */

    public void CheckIfValidUser(User user) {

        user = (User) user;

        if (user.getId() != null && !user.getId().matches(GlobalConstants.REGEXP_OBJECTID))
            throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
        if (user.getUsername() == null || user.getUsername().length() < 4)
            throw new ApiRequestException(ExceptionMessages.ERROR_USER_SMALL_THEN_4);
        if (user.getPassword() == null || user.getPassword().length() < 8)
            throw new ApiRequestException(ExceptionMessages.ERROR_PASS_SMALL_THEN_8);
        if (user.getMail() == null || user.getPhone() == null || user.getMail().equals("") || user.getPhone().equals(""))
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
        if (!user.getMail().matches(GlobalConstants.REGEXP_FOR_MAIL_VALDATION))
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_MAIL);
        if (!user.getPhone().matches(GlobalConstants.REGEXP_FOR_PHONE_NATIONAL_FORMAT))
            throw new ApiRequestException(ExceptionMessages.ERROR_INVALID_PHONE_NUMBER);
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

        if (user.getPassword() == null)
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);

        if (user.getId() == null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
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
