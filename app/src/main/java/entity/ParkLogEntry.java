package entity;

import java.util.Date;

/**
 * Class for Object VaccineLogEntry
 * Contains vaccine taken, data taken for this vaccine, date for the next dose, boolean var to indicate if reminder is requested by user
 */
public class ParkLogEntry {
    private Date dateTaken;
    private Park park;

    public ParkLogEntry(Date dateTaken, Park park) {
        this.dateTaken = dateTaken;
        this.park = park;
    }

    public ParkLogEntry() {
    }


    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }
}