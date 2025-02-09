package com.boilerplate.back.repository.board;

import com.boilerplate.back.model.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}