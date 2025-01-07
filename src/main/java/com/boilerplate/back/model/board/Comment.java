package com.boilerplate.back.model.board;

import java.time.LocalDateTime;

import com.boilerplate.back.model.member.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "t_user_idx", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "t_board_idx", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime regdate;
}