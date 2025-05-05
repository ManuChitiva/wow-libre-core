package com.register.wowlibre.infrastructure.entities;

import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.io.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "country")
    private String country;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "cell_phone")
    private String cellPhone;
    @Column(name = "password")
    private String password;
    private Boolean status;
    private Boolean verified;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "email")
    private String email;
    @JoinColumn(
            name = "rol_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RolEntity rolId;
    private String language;

    public UserModel mapToModelEntity() {
        return UserModel.builder()
                .id(id).avatar(avatarUrl).country(country)
                .firstName(firstName).rolModel(RolMapper.toModel(rolId))
                .dateOfBirth(dateOfBirth).email(email)
                .status(status).verified(verified)
                .lastName(lastName).cellPhone(cellPhone)
                .password(password).language(language).build();
    }


}
