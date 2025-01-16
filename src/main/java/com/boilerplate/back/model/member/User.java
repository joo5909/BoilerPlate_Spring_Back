package com.boilerplate.back.model.member;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String telNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private String addressDetail;

    @Column
    private String profileImage;

    @Column(nullable = false)
    private String agreed_personal;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()", insertable = false, updatable = false)
    private LocalDateTime regdate;
}