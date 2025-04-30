package com.example.demo.repository;

import com.example.demo.entity.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {

    Optional<BlockedUser> findByUserId(Long userId);

    Optional<BlockedUser> findByUserEmail(String userEmail);

    void deleteByUserId(Long userId);
}