package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}