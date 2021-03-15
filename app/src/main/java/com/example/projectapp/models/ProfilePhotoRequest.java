package com.example.projectapp.models;

public class ProfilePhotoRequest {
    private String email;
    private String photo;

    public ProfilePhotoRequest(String email, String photo) {
        this.email = email;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
