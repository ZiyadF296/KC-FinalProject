package com.example.astrospace;

public class MarsGallery {
    public final String cameraShortName;
    public final String cameraName;
    public final String imageUrl;
    public final String roverName;
    public final String roverStatus;
    public final String landingDate;

    public MarsGallery(String cameraShortName, String cameraName, String imageUrl, String roverName, String roverStatus, String landingDate) {
        this.cameraShortName = cameraShortName;
        this.cameraName = cameraName;
        this.imageUrl = imageUrl;
        this.roverName = roverName;
        this.roverStatus = roverStatus;
        this.landingDate = landingDate;
    }
}
