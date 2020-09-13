package me.hizencode.mededu.user.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {

    Optional<UserProfileEntity> findByUserId(Integer userId);

}
