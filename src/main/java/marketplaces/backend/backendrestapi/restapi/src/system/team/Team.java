package marketplaces.backend.backendrestapi.restapi.src.system.team;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.auditing.Auditable;
import marketplaces.backend.backendrestapi.restapi.src.system.pack.Pack;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Document(collection = "teams")
public class Team extends Auditable<String> {

    static public String DOC_TEXT = "TEAM";

    static public String CODE_TEXT = "code";
    static public String LABEL_TEXT = "label";
    static public String DESC_TEXT = "desc";
    static public String STATUS_TEXT = "status";
    static public String PACK_TEXT = "pack";
    static public String IS_ARCHIVED_TEXT = "isArchived";

    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;

    @NotNull(message = "code must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String code;

    @NotNull(message = "label must be not null !!")
    private String label;

    @NotNull(message = "desc must be not null !!")
    private String desc;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int isArchived = 0;

    @DBRef
    private Pack pack;


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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }
}

