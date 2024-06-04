package com.example.mobilestatusapp;

public class User {
    private String Name;
    private String Role;
    private String Status;
    private String Location;
    private String Image;

    public User() {
        // Empty Constructor required by Firebase
    }

    public User(String Name, String Role, String Status, String Location, String Image) {
        this.Name = Name;
        this.Role = Role;
        this.Status = Status;
        this.Location = Location;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        this.Role = role;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}
