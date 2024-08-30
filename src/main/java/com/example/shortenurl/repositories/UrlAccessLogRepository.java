package com.example.shortenurl.repositories;

import com.example.shortenurl.models.UrlAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlAccessLogRepository extends JpaRepository<UrlAccessLog, Integer> {
}
