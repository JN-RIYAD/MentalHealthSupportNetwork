package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(String role);

    Optional<User> findByEmailAndIdNot(String email, Long id);


    Optional<User> findByNidNoAndIdNot(String nidNo, Long id);

    Optional<User> findByMobileNoAndIdNot(String mobileNo, Long id);
}