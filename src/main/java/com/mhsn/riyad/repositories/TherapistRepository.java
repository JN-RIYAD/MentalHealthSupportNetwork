package com.mhsn.riyad.repositories;


import com.mhsn.riyad.entities.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {
    List<Therapist> findByRole(String therapist);
}
