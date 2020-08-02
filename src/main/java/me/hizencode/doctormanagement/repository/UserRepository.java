package me.hizencode.doctormanagement.repository;

import me.hizencode.doctormanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String userName);

}
