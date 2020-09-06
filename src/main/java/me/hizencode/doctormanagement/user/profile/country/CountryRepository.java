package me.hizencode.doctormanagement.user.profile.country;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {

    Optional<CountryEntity> findById(Integer id);

    ArrayList<CountryEntity> findAllByOrderByCountryName();
}
