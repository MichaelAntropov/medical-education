package me.hizencode.doctormanagement.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService{

    /*Fields*/
    /*================================================================================================================*/
    private UserProfileRepository userProfileRepository;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /*Methods*/
    /*================================================================================================================*/

    @Override
    @Transactional
    public Optional<UserProfileEntity> getProfileByUserId(Integer userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Optional<UserProfileEntity> createUserProfileForUserWithId(Integer userId) {

        if(userId == null) {
            throw new NullPointerException("Can not create profile for user with id - " + userId);
        }

        UserProfileEntity userProfile = new UserProfileEntity();

        userProfile.setUserId(userId);
        userProfile.setFilled(0);

        return Optional.of(userProfileRepository.save(userProfile));
    }

    @Override
    public void saveProfile(UserProfileEntity userProfileEntity) {
        userProfileRepository.save(userProfileEntity);
    }
}
