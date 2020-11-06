package entity;

/**
 * Class for object Account
 * contains account's email, password and list of profiles
 */
public class History {
    private String historyDate;
    private String location;


    public History(String historyDate, String location)
    {

        this.historyDate = historyDate;
        this.location = location;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String name) {
        this.historyDate = historyDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {this.location = location; }


}