package com.boilerplate.back.service.board;

import com.boilerplate.back.model.board.Favorite;
import com.boilerplate.back.repository.board.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Transactional
    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    @Transactional
    public void removeFavorite(int id) {
        favoriteRepository.deleteById(id);
    }
}
