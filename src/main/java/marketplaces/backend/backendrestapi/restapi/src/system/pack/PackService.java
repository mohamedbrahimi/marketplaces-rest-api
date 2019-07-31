package marketplaces.backend.backendrestapi.restapi.src.system.pack;

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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PackService extends GlobalService<Pack, PackRepository> {

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
            criteria.and(Pack.STATUS_TEXT).is(filtering.getStatus());

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

            this.CheckIfValidDoc(
                            Pack.DOC_TEXT,
                            pack
            );
            this.CheckIfNewDoc(
                    Pack.DOC_TEXT,
                    pack,
                    packRepository
            );
            this.UnknownException(e.getMessage());
        }
    }

    void update(Pack pack){
        this.CheckIfValidDoc(
                Pack.DOC_TEXT,
                pack,
                Arrays.asList(
                        (pack.getCode() == null) ? "": Pack.CODE_TEXT,
                        (pack.getLabel() == null) ? "": Pack.LABEL_TEXT,
                        (pack.getDesc() == null) ? "": Pack.DESC_TEXT
                        )
        );
        this.CheckIfNewDoc(
                Pack.DOC_TEXT,
                pack,
                packRepository
        );
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(pack.getId()));
            Update update = new Update();

            if(pack.getCode() != null) update.set(Pack.CODE_TEXT, pack.getCode());
            if(pack.getLabel() != null) update.set(Pack.LABEL_TEXT, pack.getLabel());
            if(pack.getDesc() != null) update.set(Pack.DESC_TEXT, pack.getDesc());
            if(Arrays.asList(0, 1).contains(pack.getStatus())) update.set(Pack.STATUS_TEXT, pack.getStatus());

            // TRY TO DO AUDITING SYSTEM ( THIS WORK NEED TO BE AUTOMATICALLY )

            update.set(Pack.LAST_MODIFIED_TEXT, new Date());
            update.set(Pack.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

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
            query.addCriteria(Criteria.where(Pack.IS_ARCHIVED_TEXT).is(0));

            Update update = new Update();
            update.set(Pack.STATUS_TEXT, 0);
            update.set(Pack.IS_ARCHIVED_TEXT, 1);
            update.set(Pack.CODE_TEXT, pack.getCode()+GlobalConstants.DEFAULT_SUFFIX_OF_ARCHIVED_DOC+ currentDate);

            update.set(Pack.LAST_MODIFIED_TEXT, new Date());
            update.set(Pack.LAST_MODIFIED_USER_TEXT, SecurityContextHolder.getContext().getAuthentication().getName());

            mongoTemplate.findAndModify(query, update, Pack.class);
        }catch (Exception e){
            this.UnknownException(e.getMessage());
        }
    }







}
