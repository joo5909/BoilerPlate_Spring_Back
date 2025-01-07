package com.boilerplate.back.service.board;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.back.model.board.Board;
import com.boilerplate.back.repository.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Optional<Board> getBoardById(int id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public void deleteBoard(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public Board updateBoard(int id, Board newBoardData) {
        Board existingBoard = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("Board not found"));
        existingBoard.setTitle(newBoardData.getTitle());
        existingBoard.setContent(newBoardData.getContent());
        return boardRepository.save(existingBoard);
    }
}