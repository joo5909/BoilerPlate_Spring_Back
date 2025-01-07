package com.boilerplate.back.model.board;

import java.time.LocalDateTime;

import com.boilerplate.back.model.member.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "t_user_idx", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "favorite_count", nullable = false, columnDefinition = "int default 0")
    private int favoriteCount;

    @Column(name = "comment_count", nullable = false, columnDefinition = "int default 0")
    private int commentCount;

    @Column(name = "view_count", nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime regdate;
}