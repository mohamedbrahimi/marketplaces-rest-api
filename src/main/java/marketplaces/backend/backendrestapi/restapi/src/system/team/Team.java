package marketplaces.backend.backendrestapi.restapi.src.system.team;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Document(collection = "teams")
public class Team {

    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;

    @NotNull(message = "code must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String code;

    @NotNull(message = "label must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING)
    private String label;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int isArchived = 0;

    @DBRef(db = "packs")
    private  String packs;


    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }

    public String getPacks() {
        return packs;
    }

    public void setPacks(String packs) {
        this.packs = packs;
    }
}

