package me.hizencode.mededu.specialities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    /*Fields*/
    /*================================================================================================================*/
    private SpecialityRepository specialityRepository;

    /*Autowire*/
    /*================================================================================================================*/
    @Autowired
    public void setSpecialityRepository(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public ArrayList<SpecialityEntity> getAllSpecialities() {
        return specialityRepository.findAllByOrderByName();
    }
}
