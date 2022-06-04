package com.abhishek.graphmugsdemo.classes;

public class UserHelperClass {

    String name, username, email, phoneNo;

    //Empty Constructor
    public UserHelperClass() {

    }

    public UserHelperClass(String name, String username, String email, String phoneNo) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}
