package com.example.blogapi.repository;

import com.example.blogapi.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent,Long> {

    List<OutboxEvent> findTop100ByPublishedFalseOrderByCreatedAtAsc();
}
