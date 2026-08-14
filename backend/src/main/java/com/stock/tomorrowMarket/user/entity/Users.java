package com.stock.tomorrowMarket.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_ID")
    private Long usersId;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "BIRTHDATE")
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @Builder
    public Users(String email, String name, String password, LocalDate birthdate, Role role, Status status) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birthdate = birthdate;
        this.role = role;
        this.status = status;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateInfo(String name, LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
