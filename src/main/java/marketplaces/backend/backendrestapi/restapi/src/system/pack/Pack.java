package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import marketplaces.backend.backendrestapi.config.global.GlobalConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;


@Document(collection = "packs")
public class Pack {
    @Id
    @Pattern(message = "Id not valid", regexp = GlobalConstants.REGEXP_OBJECTID)
    private String id;
    @NotNull(message = "code must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String code;
    @NotNull(message = "username must be not null !!")
    private String label;

    @NotNull(message = "cost must be not null !!")
    @Indexed(direction = IndexDirection.ASCENDING)
    private float cost;

    @Indexed(direction = IndexDirection.ASCENDING)
    private int status = 1;


    public  Pack(){}

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


}
