package com.example.blogapi.repository;

import com.example.blogapi.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long> {

    boolean existsByPostIdAndAction(
            Long postId,
            String action
    );
}
