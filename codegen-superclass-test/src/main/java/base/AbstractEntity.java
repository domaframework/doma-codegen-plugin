package base;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.seasar.doma.Column;

/**
 * Base entity class with common audit fields.
 * This class demonstrates superclass functionality in Doma CodeGen.
 */
public abstract class AbstractEntity implements Serializable {

    /** Created by user ID */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /** Created timestamp */
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    /** Last updated by user ID */
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    /** Last updated timestamp */
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    /** Logical delete flag */
    @Column(name = "DELETED")
    private Boolean deleted;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
