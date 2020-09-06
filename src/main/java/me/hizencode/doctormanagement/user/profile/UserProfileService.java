package me.hizencode.doctormanagement.user.profile;

import java.util.Optional;

public interface UserProfileService {

    Optional<UserProfileEntity> getProfileByUserId(Integer userId);

    Optional<UserProfileEntity> createUserProfileForUserWithId(Integer userId);

    void saveProfile(UserProfileEntity userProfileEntity);
}
