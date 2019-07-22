package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;
import marketplaces.backend.backendrestapi.config.exceptions.constants.ExceptionMessages;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import marketplaces.backend.backendrestapi.config.exceptions.unknown.ApiRequestUnknownException;
import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import marketplaces.backend.backendrestapi.restapi.src.system.user.User;
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
public class PackService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    PackRepository packRepository;

    public Page<Pack> find(Filtering filtering){
        Pageable pageable = PageRequest.of(filtering.getPage(), filtering.getSize());
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where(Pack.CODE_TEXT).regex(filtering.getText())
        );
        if( Arrays.asList(0, 1).contains(filtering.getStatus()) )
            criteria.andOperator(Criteria.where(User.STATUS_TEXT).is(filtering.getStatus()));
        Query query = new Query(criteria).with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "count"));

        query.fields().include(Pack.CODE_TEXT);
        query.fields().include(Pack.LABEL_TEXT);
        query.fields().include(Pack.DESC_TEXT);
        query.fields().include(Pack.STATUS_TEXT);

        List<Pack> list = mongoTemplate.find(query, Pack.class);
        return PageableExecutionUtils.getPage(
                list,
                pageable,
                ()-> mongoTemplate.count(query, Pack.class)
        );
    }

    void insert(Pack pack){
        try{
            mongoTemplate.insert(pack);
        }catch (Exception e){
            this.CheckIfValidPack(pack);
            this.CheckIfNewPack(pack);
            this.UnknownException(e.getMessage());
        }
    }

    void update(Pack pack){
        this.CheckIfValidPack(pack,
                Arrays.asList(
                        (pack.getCode() == null) ? "": Pack.CODE_TEXT,
                        (pack.getLabel() == null) ? "": Pack.LABEL_TEXT,
                        (pack.getDesc() == null) ? "": Pack.DESC_TEXT
                        )
        );
        this.CheckIfNewPack(pack);
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(pack.getId()));
            Update update = new Update();

            if(pack.getCode() != null) update.set(Pack.CODE_TEXT, pack.getCode());
            if(pack.getLabel() != null) update.set(Pack.LABEL_TEXT, pack.getLabel());
            if(pack.getDesc() != null) update.set(Pack.DESC_TEXT, pack.getDesc());
            if(Arrays.asList(0, 1).contains(pack.getStatus())) update.set(Pack.STATUS_TEXT, pack.getStatus());

            // TRY TO DO AUDITING SYSTEM ( THIS WORK NEED TO BE AUTOMATICALLY )

            update.set(User.LAST_MODIFIED_TEXT, new Date());
            update.set(User.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, Pack.class);

        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }

    void delete(String id){
        Optional<Pack> optionalPack = packRepository.findById(id);
        Pack pack = Optional.empty().equals(optionalPack) ? null : optionalPack.get();
        if (pack == null || pack.getIsArchived() == 1)
            throw new ApiRequestException(ExceptionMessages.ERROR_DOCUMENT_NOT_FOUND);
        try {
            Query query = new Query();
            String currentDate = (new Date()).toString();

            query.addCriteria(Criteria.where("id").is(id));
            query.addCriteria(Criteria.where(User.IS_ARCHIVED_TEXT).is(0));

            Update update = new Update();
            update.set(Pack.STATUS_TEXT, 0);
            update.set(Pack.IS_ARCHIVED_TEXT, 1);
            update.set(Pack.CODE_TEXT, pack.getCode()+"_ARCHIVED"+ currentDate);

            update.set(User.LAST_MODIFIED_TEXT, new Date());
            update.set(User.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, Pack.class);
        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }

    public void CheckIfValidPack(Pack pack, List<String> forFields){

        if(pack.getId() != null && !pack.getId().matches(GlobalConstants.REGEXP_OBJECTID))
            throw new ApiRequestException(ExceptionMessages.ERROR_OBJECT_ID_NOT_VALID);
        if(forFields.contains(Pack.CODE_TEXT) && pack.getCode() == null)
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
        if(forFields.contains(Pack.LABEL_TEXT) && pack.getLabel() == null)
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);
        if(forFields.contains(Pack.DESC_TEXT) && pack.getDesc() == null)
            throw new ApiRequestException(ExceptionMessages.ERROR_FIELD_NULL);

    }

    public void CheckIfValidPack(Pack pack){
        CheckIfValidPack(
                pack,
                Arrays.asList(
                   Pack.CODE_TEXT,
                   Pack.LABEL_TEXT,
                   Pack.DESC_TEXT
                )
        );
    }

    public void CheckIfNewPack(Pack pack){
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
    }

    public void UnknownException(String message) {

        ApiExceptionMessage e = ExceptionMessages.ERROR_UNKNOWN_EXCEPTION;
        e.setAdditionalMessage(message);
        throw new ApiRequestUnknownException(e);
    }

}
