package me.hizencode.mededu.user.profile.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    /*Fields*/
    /*================================================================================================================*/

    private CountryRepository countryRepository;

    /*Setters*/
    /*================================================================================================================*/

    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public ArrayList<CountryEntity> getAllCountries() {
        return countryRepository.findAllByOrderByCountryName();
    }

    @Override
    public Optional<CountryEntity> getCountryById(Integer id) {
        return countryRepository.findById(id);
    }
}
