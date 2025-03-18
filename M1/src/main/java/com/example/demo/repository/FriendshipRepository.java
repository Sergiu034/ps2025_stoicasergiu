package com.example.demo.repository;

import com.example.demo.entity.Friendship;
import com.example.demo.entity.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findFriendshipById(Long id);
    Optional<Friendship> findFriendshipByUser1AndUser2(User user1, User user2);

    List<Friendship> findByUser1OrUser2(User user, User user1);
}
