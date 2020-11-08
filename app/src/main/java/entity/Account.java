package entity;

import java.util.ArrayList;


/**
 * Class for object Account
 * contains account's email, password and list of profiles
 */
public class Account {
    private String email;
    private String password;
    private ArrayList<Profile> profiles;
    private ArrayList<History> history;

    public Account(String email, String password, ArrayList<Profile> profiles) {
        this.email = email;
        this.password = password;
        this.profiles = profiles;
    }

    public Account(String email, String password, String firstName, String lastName, String dob)
    {
        this.email = email;
        this.password = password;
        profiles = new ArrayList<>();
        profiles.add(new Profile(firstName+" "+lastName, dob));

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

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }


}