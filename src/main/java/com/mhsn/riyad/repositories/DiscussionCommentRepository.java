package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.DiscussionComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionCommentRepository extends JpaRepository<DiscussionComment, Long> {
}