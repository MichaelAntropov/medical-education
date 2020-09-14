package me.hizencode.mededu.specialities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SpecialityRepository extends JpaRepository<SpecialityEntity, Integer> {

    ArrayList<SpecialityEntity> findAllByOrderByName();

}
