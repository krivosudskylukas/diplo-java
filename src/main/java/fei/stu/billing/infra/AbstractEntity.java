package fei.stu.billing.infra;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime modifiedAt;

    protected AbstractEntity() {
    }

    @PrePersist
    public void prePersist() {
        this.modifiedAt = null;
    }

    public LocalDateTime getCreatedTime() {
        return createdAt;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdAt = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedAt;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedAt = modifiedTime;
    }
}
