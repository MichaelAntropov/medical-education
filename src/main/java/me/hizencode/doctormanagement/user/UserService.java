package me.hizencode.doctormanagement.user;

import me.hizencode.doctormanagement.registration.UserDto;
import me.hizencode.doctormanagement.user.role.RoleEntity;
import me.hizencode.doctormanagement.user.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private Logger logger = Logger.getLogger(getClass().getName());

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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findUserByUsername(username);

        if(user.isPresent()) {
            return getUserPrincipal(user.get());
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }

    }

    @Transactional
    public void registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
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
    }

    public UserPrincipal getUserPrincipal(UserEntity userEntity) {
        return new UserPrincipal(userEntity.getUsername(),
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
}
