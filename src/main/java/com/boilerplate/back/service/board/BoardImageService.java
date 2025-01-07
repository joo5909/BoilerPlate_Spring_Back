package com.boilerplate.back.service.board;

import com.boilerplate.back.model.board.BoardImage;
import com.boilerplate.back.repository.board.BoardImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardImageService {

    @Autowired
    private BoardImageRepository boardImageRepository;

    public List<BoardImage> getImagesByBoardId(int boardIdx) {
        return boardImageRepository.findAll();  // 필요 시, Board Id로 필터링할 수 있습니다.
    }

    public BoardImage addImage(BoardImage boardImage) {
        return boardImageRepository.save(boardImage);
    }
}