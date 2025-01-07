package com.boilerplate.back.repository.search;

import com.boilerplate.back.model.search.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchLogRepository extends JpaRepository<SearchLog, Integer> {
}