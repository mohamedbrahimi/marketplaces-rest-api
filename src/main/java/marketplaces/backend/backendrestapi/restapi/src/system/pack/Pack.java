package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import marketplaces.backend.backendrestapi.config.global.ApiMessageBody;
import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import marketplaces.backend.backendrestapi.config.global.auditing.Auditable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;


@Document(collection = "packs")
public class Pack extends Auditable<String> {

    static public String CODE_TEXT = "code";
    static public String LABEL_TEXT = "label";
    static public String DESC_TEXT = "desc";
    static public String COST_TEXT = "cost";
    static public String STATUS_TEXT = "status";
    static public String IS_ARCHIVED_TEXT = "isArchived";

    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;
    @NotNull(message = "code must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String code;
    @NotNull(message = "label must be not null !!")
    private ApiMessageBody label;

    @NotNull(message = "desc must be not null !!")
    private ApiMessageBody desc;

    @NotNull(message = "cost must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING)
    private float cost;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;

    @Indexed(direction = IndexDirection.DESCENDING)
    private int isArchived = 0;


    public  Pack(){}

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public ApiMessageBody getLabel() {
        return label;
    }

    public void setLabel(ApiMessageBody label) {
        this.label = label;
    }

    public ApiMessageBody getDesc() {
        return desc;
    }

    public void setDesc(ApiMessageBody desc) {
        this.desc = desc;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
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
}
