package com.pilatesstudio.member.entity;
import com.pilatesstudio.common.entity.BaseEntity;
import com.pilatesstudio.identity.entity.Account;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity @Table(name="member", schema="pilates_studio") @Getter @Setter @NoArgsConstructor
public class Member extends BaseEntity {
 @Column(name="account_id", nullable=false, unique=true) private Long accountId;
 @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="account_id", insertable=false, updatable=false) private Account account;
 @Column(name="membership_start_date") private LocalDate membershipStartDate;
 @Column(name="membership_end_date") private LocalDate membershipEndDate;
 @Column(length=500) private String note;
}
