package me.hizencode.mededu.user;

import me.hizencode.mededu.registration.UserDto;
import me.hizencode.mededu.user.profile.UserProfileAlreadyExistsException;
import me.hizencode.mededu.user.profile.UserProfileService;
import me.hizencode.mededu.user.role.RoleEntity;
import me.hizencode.mededu.user.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    /*Fields*/
    /*================================================================================================================*/

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private UserProfileService userProfileService;

    private PasswordEncoder passwordEncoder;

    private Logger logger = Logger.getLogger(getClass().getName());

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findUserEntityByEmail(email);

        if (user.isPresent()) {
            return getUserPrincipal(user.get());
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }

    }

    @Transactional
    public void registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException, UserProfileAlreadyExistsException {
        if (usernameOrEmailExists(userDto.getUsername(), userDto.getEmail())) {

            throw new UserAlreadyExistException("There is an account with that username/email address: "
                    + userDto.getUsername() + "/" + userDto.getEmail());

        }

        logger.info("Registering user: " + userDto.getUsername());

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEmail(userDto.getEmail());
        userEntity.setActive(true);

        Optional<RoleEntity> roleEntity = roleRepository.findRoleByName("ROLE_USER");

        logger.info("With role: " + roleEntity.toString());

        roleEntity.ifPresent(role ->
                userEntity.setRoles(Collections.singletonList(role)));

        userRepository.save(userEntity);

        userProfileService.createUserProfileForUserWithId(userEntity.getId());
    }

    public UserPrincipal getUserPrincipal(UserEntity userEntity) {
        return new UserPrincipal(userEntity.getId(), userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.isActive(),
                true,
                true,
                true,
                userEntity.getAuthorities());
    }

    private boolean usernameOrEmailExists(String username, String email) {
        return userRepository.findUserEntityByUsernameOrEmail(username, email).isPresent();
    }

    public boolean userHasRole(Authentication authentication, String role) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
