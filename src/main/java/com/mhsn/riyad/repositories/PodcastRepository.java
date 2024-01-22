package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PodcastRepository extends JpaRepository<Podcast, Long>  {
}
