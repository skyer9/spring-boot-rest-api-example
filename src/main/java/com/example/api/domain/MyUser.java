package com.example.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "user")
public class MyUser {

    @Id
    @Column(length = 128, unique = true)
    private String username;

    @JsonIgnore
    @Column(length = 256)
    private String password;

    @Column(length = 128)
    private String nickname;

    @Column(length = 19)
    private String lastLoginDate;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "username", referencedColumnName = "username")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
