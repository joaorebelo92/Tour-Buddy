package com.example.joorebelo.finalproject.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPlaces implements Serializable {
    private  int placeId;
    private String placeName;
    private String locationLink;
    private String country;
    private String city;
    private String imageLink;
    private String webLink;
    private int rank;
    private String description;
    private boolean favorite;
    private String videoLink;
    private int countRate;

    public RankedPlaces(int placeId, String placeName, String locationLink, String country, String city, String imageLink, String webLink, int rank, String description, boolean favorite, String videoLink, int countRate) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.locationLink = locationLink;
        this.country = country;
        this.city = city;
        this.imageLink = imageLink;
        this.webLink = webLink;
        this.rank = rank;
        this.description = description;
        this.favorite = favorite;
        this.videoLink = videoLink;
        this.countRate = countRate;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocationLink() {
        return locationLink;
    }

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public int getCountRate() {
        return countRate;
    }

    public void setCountRate(int countRate) {
        this.countRate = countRate;
    }
}
