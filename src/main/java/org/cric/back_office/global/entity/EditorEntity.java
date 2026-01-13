package org.cric.back_office.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class EditorEntity extends DateEntity{

    @CreatedBy
    @Column(updatable = false, name = "create_by")
    private String createBy;

    @LastModifiedBy
    @Column(name = "update_by")
    private String updateBy;

    // 회원가입 시 직접 설정할 수 있도록 setter 제공
    protected void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    protected void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @PrePersist
    public void prePersistName() {
        LocalDateTime now = LocalDateTime.now();
        super.setDateOfCreate(now);
        super.setDateOfUpdate(now);
        if (createBy == null) {
            createBy = getCurrentUserName();
        }
        if (updateBy == null) {
            updateBy = createBy;
        }
    }

    @PreUpdate
    public void preUpdateName() {
        super.setDateOfUpdate(LocalDateTime.now());
        String currentUserName = getCurrentUserName();
        if (currentUserName != null) {
            updateBy = currentUserName;
        }
    }

    /**
     * SecurityContext에서 현재 로그인한 사용자의 이름을 가져옴
     * JwtAuthenticationFilter에서 name을 principal로 설정
     */
    private String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            // JwtAuthenticationFilter에서 name을 principal로 설정했으므로
            if (principal instanceof String && !"anonymousUser".equals(principal)) {
                return (String) principal;
            }
        }
        // 인증 정보가 없을 경우 (회원가입 시) null 반환
        return null;
    }

}
