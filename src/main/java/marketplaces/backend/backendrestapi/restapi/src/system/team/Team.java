package marketplaces.backend.backendrestapi.restapi.src.system.team;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate = new Date();


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

    public Date getCreatedDate() {
        return createdDate;
    }
}

