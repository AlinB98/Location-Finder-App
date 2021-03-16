package org.scd.model.dto;

import java.util.ArrayList;

public class UserRegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ArrayList<String> role = new ArrayList<>();

    public UserRegisterDTO(){}

    public UserRegisterDTO(String firstName, String lastName, String email, String password){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.password=password;


    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList getRole() {
        return role;
    }

    public void setRole(ArrayList role) {
        this.role = role;
    }




}
