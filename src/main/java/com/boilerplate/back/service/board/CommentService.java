package com.boilerplate.back.service.board;

import com.boilerplate.back.model.board.Comment;
import com.boilerplate.back.repository.board.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(int id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment updateComment(int id, Comment newCommentData) {
        Comment existingComment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment.setContent(newCommentData.getContent());
        return commentRepository.save(existingComment);
    }
}