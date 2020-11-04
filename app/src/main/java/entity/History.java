package entity;

import java.util.Date;


/**
 * Class for object Account
 * contains account's email, password and list of profiles
 */
public class History {
    private Date historyDate;
    private String location;


    public History(Date historyDate, String location)
    {

        this.historyDate = historyDate;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
    public Date getHistoryDate() {return historyDate;}
}