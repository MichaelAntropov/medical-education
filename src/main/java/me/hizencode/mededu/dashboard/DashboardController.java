package me.hizencode.mededu.dashboard;

import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.SpecialityService;
import me.hizencode.mededu.user.UserPrincipal;
import me.hizencode.mededu.user.profile.UserProfileEntity;
import me.hizencode.mededu.user.profile.UserProfileService;
import me.hizencode.mededu.user.profile.country.CountryEntity;
import me.hizencode.mededu.user.profile.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class DashboardController {

    /*Fields*/
    /*================================================================================================================*/

    private UserProfileService userProfileService;

    private CountryService countryService;

    private SpecialityService specialityService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Autowired
    public void setSpecialityService(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    /*Methods*/
    /*================================================================================================================*/

    @GetMapping("/user-dashboard/profile")
    private String showDashboard(Model model, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserProfileEntity userProfileEntity =
                userProfileService.getProfileByUserId(userPrincipal.getId()).orElseThrow();

        //Get countries and specialities from db and add to the model
        ArrayList<CountryEntity> countries = countryService.getAllCountries();
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();


        model.addAttribute("countries", countries);
        model.addAttribute("specialities", specialities);

        //Translate profile to dto object
        UserProfileDto userProfileDto = new UserProfileDto(userProfileEntity);

        model.addAttribute("profile", userProfileDto);

        return "user-dashboard/user-profile";
    }

    @PostMapping("/user-dashboard/profile/save")
    private String saveProfile(@ModelAttribute(name = "profile") @Valid UserProfileDto userProfileDto,
                               BindingResult bindingResult, Authentication authentication, Model model) {

        if (bindingResult.hasErrors()) {
            //Get countries and specialities from db and add to the model
            ArrayList<CountryEntity> countries = countryService.getAllCountries();
            ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();


            model.addAttribute("countries", countries);
            model.addAttribute("specialities", specialities);
            return "user-dashboard/user-profile";
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserProfileEntity userProfileEntity =
                userProfileService.getProfileByUserId(userPrincipal.getId()).orElseThrow();

        userProfileEntity.setFirstName(userProfileDto.getFirstName());
        userProfileEntity.setLastName(userProfileDto.getLastName());
        userProfileEntity.setMiddleName(userProfileDto.getMiddleName());

        userProfileEntity.setPhone(userProfileDto.getPhone());
        userProfileEntity.setBirthDate(userProfileDto.getBirthDate());

        userProfileEntity.setCountry(userProfileDto.getCountry());
        userProfileEntity.setRegionState(userProfileDto.getRegionState());
        userProfileEntity.setCity(userProfileDto.getCity());

        userProfileEntity.setSpeciality(userProfileDto.getSpeciality());
        userProfileEntity.setWorkplace(userProfileDto.getWorkplace());
        userProfileEntity.setPosition(userProfileDto.getPosition());

        //Set profile as filled
        userProfileEntity.setFilled(1);

        userProfileService.saveProfile(userProfileEntity);

        return "redirect:/user-dashboard/profile";
    }
}
