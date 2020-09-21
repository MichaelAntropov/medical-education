package me.hizencode.mededu.rest.photos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

    PhotoSource src;

    public Photo() {
    }

    public PhotoSource getSrc() {
        return src;
    }

    public void setSrc(PhotoSource src) {
        this.src = src;
    }
}
