package com.boilerplate.back.model.board;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "t_board_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "t_board_idx", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime regdate;
}