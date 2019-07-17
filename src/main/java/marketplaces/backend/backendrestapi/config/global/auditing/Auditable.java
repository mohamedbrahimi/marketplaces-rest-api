package marketplaces.backend.backendrestapi.config.global.auditing;


import org.springframework.data.annotation.*;


import java.util.Date;

public abstract class Auditable<U> {

    static public String VERSION_TEXT = "version";
    static public String CREATED_AT_TEXT = "createdAt";
    static public String LAST_MODIFIED_TEXT = "lastModified";
    static public String CREATED_BY_TEXT = "createdBy";
    static public String LAST_MODIFIED_USER_TEXT = "lastModifiedUser";

    @Version
    private Long version;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date lastModified;

    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedUser;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }
}

