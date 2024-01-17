package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Blog;
import com.mhsn.riyad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}