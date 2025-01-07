package com.boilerplate.back.service.search;

import com.boilerplate.back.model.search.SearchLog;
import com.boilerplate.back.repository.search.SearchLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchLogService {

    @Autowired
    private SearchLogRepository searchLogRepository;

    @Transactional
    public SearchLog saveSearchLog(SearchLog searchLog) {
        return searchLogRepository.save(searchLog);
    }
}