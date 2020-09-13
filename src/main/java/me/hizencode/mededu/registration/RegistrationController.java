package me.hizencode.mededu.registration;

import me.hizencode.mededu.user.UserAlreadyExistException;
import me.hizencode.mededu.user.UserService;
import me.hizencode.mededu.user.profile.UserProfileAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class RegistrationController {

    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {

        model.addAttribute("user", new UserDto());

        return "registration/registration-page";
    }

    @PostMapping("/registration-process")
    public String registerUserAccount(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult theBindingResult,
            Model model) {

        String userName = userDto.getUsername();
        logger.info("Processing registration form for: " + userName);

        if (theBindingResult.hasErrors()) {
            logger.info(theBindingResult.getAllErrors().toString());
            return "registration/registration-page";
        }

        try {
            userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException exc) {
            logger.warning("User name/email already exists.");
            model.addAttribute("message", "An account with this username/email already exists.");
            model.addAttribute("user", new UserDto());
            return "registration/registration-page";
        } catch (UserProfileAlreadyExistsException e) {
            logger.warning(e.getMessage());
            model.addAttribute("message", "Can not register the user");
            model.addAttribute("user", new UserDto());
        }

        return "registration/registration-success";
    }
}
