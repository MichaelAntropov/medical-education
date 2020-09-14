package me.hizencode.mededu.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserEntityByEmail(String userName);

    Optional<UserEntity> findUserEntityByUsernameOrEmail(String username, String email);

}
