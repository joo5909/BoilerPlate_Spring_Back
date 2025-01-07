package com.boilerplate.back.repository.board;

import com.boilerplate.back.model.board.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Integer> {
}