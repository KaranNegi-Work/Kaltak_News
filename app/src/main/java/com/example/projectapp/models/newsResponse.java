package com.example.projectapp.models;

public class newsResponse {
    Integer id;
    private String image;
    private String discription;
    private String fullInformation;
    private int rate;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getFullInformation() {
        return fullInformation;
    }

    public void setFullInformation(String fullInformation) {
        this.fullInformation = fullInformation;
    }
}
