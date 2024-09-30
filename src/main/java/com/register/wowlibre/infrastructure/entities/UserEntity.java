package com.register.wowlibre.infrastructure.entities;

import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.UserModel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
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

    @Column(name = "email")
    private String email;

    private Boolean status;
    private Boolean verified;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String language;

    @JoinColumn(
            name = "rol_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RolEntity rolId;


    public UserModel mapToModelEntity() {
        return UserModel.builder().id(id).country(country).firstName(firstName).rolModel(RolMapper.toModel(rolId))
                .dateOfBirth(dateOfBirth).email(email).status(status).verified(verified)
                .lastName(lastName).cellPhone(cellPhone).password(password).language(language).build();
    }


}
