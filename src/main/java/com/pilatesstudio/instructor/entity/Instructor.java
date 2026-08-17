package com.pilatesstudio.instructor.entity;
import com.pilatesstudio.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "instructor", schema = "pilates_studio")
@Getter
@Setter
@NoArgsConstructor
public class Instructor extends BaseEntity {
 @Column(name="first_name", nullable=false, length=100)
 private String firstName;

 @Column(name="last_name", nullable=false, length=100)
 private String lastName;

 @Column(nullable=false, unique=true, length=20)
 private String phone;

 @Column(unique=true, length=255)
 private String email;

 @Column(nullable=false)
 private boolean active=true;

}
