package entity;


import java.util.Date;

import control.ParkLogMgr;

public interface ParkLogMgrInterface {
    public void ParkLogEntry(final String userId, final String parkName);
    public void updateParkLogEntry(final String userId, final String parkName, final Date dateTaken, final Date dateDue, final String reminder);
    public void addParkLogEntry(final String userId,final Date date, final String parkName);
    public void retrieveParkLog(final ParkLogMgr.MyCallbackParkLog myCallback, final String Uid);

    public void retrieveUserPark(final ParkLogMgr.MyCallback myCallback, final String Uid);
    public void retrieveParks(final ParkLogMgr.MyCallback myCallback);
}