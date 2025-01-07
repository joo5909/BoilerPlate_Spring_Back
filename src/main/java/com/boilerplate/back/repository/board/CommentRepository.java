package com.boilerplate.back.repository.board;

import com.boilerplate.back.model.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}