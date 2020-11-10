package entity;


import control.ProfileMgr;

public interface ProfileMgrInterface {
    public void retrieveCurrentProfileName(final ProfileMgr.MyCallbackString myCallback, final String Uid);
    public void retrieveCurrentDOB(final ProfileMgr.MyCallbackString myCallback, final String Uid);
    public void editHistory(final String uId,String name);
    public void retrieveHistory(final ProfileMgr.MyCallbackString myCallback, final String Uid);
}