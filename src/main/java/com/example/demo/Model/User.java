package com.example.demo.Model;

public class User {
    private String fullname;
    private String email;
    private String password;

    public User() {} // Needed for form binding

    public User(String username, String password) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    public String getFullname() {return fullname; }
    public void setFullname(String fullname) {this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
