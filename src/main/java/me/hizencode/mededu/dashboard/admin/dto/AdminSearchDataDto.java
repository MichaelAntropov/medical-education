package me.hizencode.mededu.dashboard.admin.dto;

import java.util.List;

public class AdminSearchDataDto {

    private String searchText;

    private List<Integer> chosenSpecialities;

    public AdminSearchDataDto() {

    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<Integer> getChosenSpecialities() {
        return chosenSpecialities;
    }

    public void setChosenSpecialities(List<Integer> chosenSpecialities) {
        this.chosenSpecialities = chosenSpecialities;
    }
}
