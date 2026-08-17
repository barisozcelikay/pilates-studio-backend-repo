package com.pilatesstudio.instructor.entity;
import com.pilatesstudio.common.entity.BaseEntity;
import com.pilatesstudio.identity.entity.Account;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="instructor", schema="pilates_studio") @Getter @Setter @NoArgsConstructor
public class Instructor extends BaseEntity {
 @Column(name="account_id", nullable=false, unique=true) private Long accountId;
 @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="account_id", insertable=false, updatable=false) private Account account;
 @Column(length=255) private String specialty;
 @Column(length=1000) private String biography;
}
