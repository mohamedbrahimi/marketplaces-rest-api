package marketplaces.backend.backendrestapi.restapi.src.system.user;

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

import java.util.List;

@Service
public class UserService {

    @Autowired
    MongoTemplate mongoTemplate;

    Page<User> find(Filtering filtering){
        Pageable pageable = PageRequest.of(filtering.getPage(), filtering.getSize());
        Query query = new Query().with(pageable);

        query.with(new Sort(Sort.Direction.DESC, "count"));
        query.fields().include("username");
        query.fields().include("mail");
        query.fields().include("phone");
        query.fields().include("roles");
        query.addCriteria(Criteria.where("status").is(1));

        List<User> list =  mongoTemplate.find(query, User.class);
        return PageableExecutionUtils.getPage(
                list,
                pageable,
                ()-> mongoTemplate.count(query, User.class)
        );

        //return mongoTemplate.find(query, User.class);

    }
}
