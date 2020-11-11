package entity;


/**
 * Class for Object Profile
 * Contains profile's name , date of birth, list of park connector log entry and boolean var to indicate if this is current profile
 */
public class Profile {
    private String name;
    private String dateOfBirth;
    private boolean thisProfile;
    private String history;


    public Profile() {
    }

    public Profile(String name, String dateOfBirth)
    {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        thisProfile = true;
        history = "";
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

    public boolean getThisProfile() {
        return thisProfile;
    }

    public void setThisProfile(boolean thisProfile) {
        this.thisProfile = thisProfile;
    }

    public String getHistory() {return history;}

    public void setHistory(String history) {this.history = history;}
}
