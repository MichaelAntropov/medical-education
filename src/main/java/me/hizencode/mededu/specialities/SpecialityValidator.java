package me.hizencode.mededu.specialities;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpecialityValidator implements ConstraintValidator<ValidSpeciality, SpecialityEntity> {
    public void initialize(ValidSpeciality constraint) {
    }

    public boolean isValid(SpecialityEntity speciality, ConstraintValidatorContext context) {
        return speciality != null;
    }
}
