package me.hizencode.mededu.dashboard.admin.dto;

import me.hizencode.mededu.specialities.SpecialityEntity;

import javax.validation.constraints.Size;
import java.util.List;

public class AdminSearchCoursesDto {

    @Size(max = 128, message = "Max 512 characters")
    private String searchText;

    private List<SpecialityEntity> chosenSpecialities;

    public AdminSearchCoursesDto() {

    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<SpecialityEntity> getChosenSpecialities() {
        return chosenSpecialities;
    }

    public void setChosenSpecialities(List<SpecialityEntity> chosenSpecialities) {
        this.chosenSpecialities = chosenSpecialities;
    }
}
