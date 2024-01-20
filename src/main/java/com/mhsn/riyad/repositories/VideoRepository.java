package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}