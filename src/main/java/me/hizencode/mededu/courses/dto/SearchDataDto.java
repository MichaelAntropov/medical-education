package me.hizencode.mededu.courses.dto;

import java.util.List;

public class SearchDataDto {

    private String searchText;

    private List<Integer> chosenSpecialities;

    public SearchDataDto() {

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
