package com.mbarca.vete.domain;

public class Images {
    byte [] fullImage;
    byte [] thumbnail;

    public Images() {
    }

    public Images(byte[] fullImage, byte[] thumbnail) {
        this.fullImage = fullImage;
        this.thumbnail = thumbnail;
    }

    public byte[] getFullImage() {
        return fullImage;
    }

    public void setFullImage(byte[] fullImage) {
        this.fullImage = fullImage;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
