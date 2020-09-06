package me.hizencode.doctormanagement.user.profile.country;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryValidator implements ConstraintValidator<ValidCountry, CountryEntity> {
    public void initialize(ValidCountry constraint) {
    }

    public boolean isValid(CountryEntity country, ConstraintValidatorContext context) {
        return country!=null;
    }
}
