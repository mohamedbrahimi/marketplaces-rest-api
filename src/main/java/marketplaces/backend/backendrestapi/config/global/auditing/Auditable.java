package marketplaces.backend.backendrestapi.config.global.auditing;


import org.springframework.data.annotation.*;


import java.util.Date;

public abstract class Auditable<U> {

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

    public String getUser() {
        return createdBy;
    }

    public void setUser(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }
}

