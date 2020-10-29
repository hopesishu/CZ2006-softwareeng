package entity;


import java.util.ArrayList;
import java.util.List;

/**
 * Class for Object Profile
 * Contains profile's name , date of birth, list of park connector log entry and boolean var to indicate if this is current profile
 */
public class Profile {
    private String name;
    private String dateOfBirth;
    private List<ParkLogEntry> parkLogEntries;
    private boolean thisProfile;


    public Profile() {
    }

    public Profile(String name, String dateOfBirth, ArrayList<ParkLogEntry> parkLogEntries) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.parkLogEntries = parkLogEntries;
        this.thisProfile = true;
    }

    public Profile(String name, String dateOfBirth)
    {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        parkLogEntries = new ArrayList<>();
        thisProfile = true;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<ParkLogEntry> getParkLogEntries() {
        return parkLogEntries;
    }

    public void setParkLogEntries(List<ParkLogEntry> parkLogEntries) {
        this.parkLogEntries = parkLogEntries;
    }

    public boolean getThisProfile() {
        return thisProfile;
    }

    public void setThisProfile(boolean thisProfile) {
        this.thisProfile = thisProfile;
    }
}
