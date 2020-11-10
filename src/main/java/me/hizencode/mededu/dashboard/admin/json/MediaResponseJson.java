package me.hizencode.mededu.dashboard.admin.json;

import java.util.List;

public class MediaResponseJson {

    private String message;

    private List<MediaItemJson> mediaItems;

    public MediaResponseJson() {
    }

    public MediaResponseJson(String message) {
        this.message = message;
    }

    public MediaResponseJson(List<MediaItemJson> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public MediaResponseJson(String message, List<MediaItemJson> mediaItems) {
        this.message = message;
        this.mediaItems = mediaItems;
    }

    public List<MediaItemJson> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItemJson> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
