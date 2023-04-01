package com.example.neticket.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.neticket.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findByEmail(String email);
  Optional<User> findByNickname(String nickname);
}
