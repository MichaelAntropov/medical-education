package me.hizencode.mededu.rest.photos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoSource {

    String large;

    public PhotoSource() {
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    @Override
    public String toString() {
        return "PhotoSource{" +
                "large='" + large + '\'' +
                '}';
    }
}
