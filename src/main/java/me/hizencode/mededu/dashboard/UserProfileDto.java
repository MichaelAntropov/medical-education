package me.hizencode.mededu.dashboard;

import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.ValidSpeciality;
import me.hizencode.mededu.user.profile.UserProfileEntity;
import me.hizencode.mededu.user.profile.country.CountryEntity;
import me.hizencode.mededu.user.profile.country.ValidCountry;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserProfileDto {

    @NotNull(message = "Should not be empty")
    @NotEmpty(message = "Should not be empty")
    @Size(max = 128, message = "Max 128 characters long")
    private String firstName;

    @NotNull(message = "Should not be empty")
    @NotEmpty(message = "Should not be empty")
    @Size(max = 128, message = "Max 128 characters long")
    private String lastName;

    @Size(max = 128, message = "Max 128 characters long")
    private String middleName;

    @NotNull(message = "Should not be empty")
    @NotEmpty(message = "Should not be empty")
    @Size(max = 20, message = "Max 20 numbers long")
    private String phone;

    @ValidDate(message = "Should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @ValidCountry
    private CountryEntity country;

    @Size(max = 128, message = "Max 128 characters long")
    private String regionState;

    @NotNull(message = "Should not be empty")
    @NotEmpty(message = "Should not be empty")
    @Size(max = 128, message = "Max 128 characters long")
    private String city;

    @ValidSpeciality
    private SpecialityEntity speciality;

    @NotNull(message = "Should not be empty")
    @NotEmpty(message = "Should not be empty")
    @Size(max = 128, message = "Max 128 characters long")
    private String workplace;

    @NotNull(message = "Should not be empty")
    @NotEmpty(message = "Should not be empty")
    @Size(max = 128, message = "Max 128 characters long")
    private String position;

    //Used by spring
    public UserProfileDto() {

    }

    public UserProfileDto(UserProfileEntity userProfileEntity) {
        this.firstName = userProfileEntity.getFirstName();
        this.lastName = userProfileEntity.getLastName();
        this.middleName = userProfileEntity.getMiddleName();
        this.phone = userProfileEntity.getPhone();
        this.birthDate = userProfileEntity.getBirthDate();
        this.country = userProfileEntity.getCountry();
        this.regionState = userProfileEntity.getRegionState();
        this.city = userProfileEntity.getCity();
        this.speciality = userProfileEntity.getSpeciality();
        this.workplace = userProfileEntity.getWorkplace();
        this.position = userProfileEntity.getPosition();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public String getRegionState() {
        return regionState;
    }

    public void setRegionState(String regionState) {
        this.regionState = regionState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SpecialityEntity getSpeciality() {
        return speciality;
    }

    public void setSpeciality(SpecialityEntity speciality) {
        this.speciality = speciality;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate=" + birthDate +
                ", country=" + country +
                ", regionState='" + regionState + '\'' +
                ", city='" + city + '\'' +
                ", speciality=" + speciality +
                ", workplace='" + workplace + '\'' +
                ", position='" + position +
                "'}";
    }
}
