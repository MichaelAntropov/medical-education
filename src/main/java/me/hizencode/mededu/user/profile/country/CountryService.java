package me.hizencode.mededu.user.profile.country;

import java.util.ArrayList;
import java.util.Optional;

public interface CountryService {

    ArrayList<CountryEntity> getAllCountries();

    Optional<CountryEntity> getCountryById(Integer id);
}
