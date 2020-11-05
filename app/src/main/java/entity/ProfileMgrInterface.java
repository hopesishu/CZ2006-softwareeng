package entity;


import control.ProfileMgr;

public interface ProfileMgrInterface {
    public void retrieveSubprofileNameAndID(final ProfileMgr.MyCallbackHashMap myCallback, final String Uid);
    public void retrieveCurrentProfileName(final ProfileMgr.MyCallbackString myCallback, final String Uid);
    public void retrieveCurrentDOB(final ProfileMgr.MyCallbackString myCallback, final String Uid);
    public void retrieveProfile(ProfileMgr.MyCallbackProfile myCallback, String uId, String pId);
    public void editProfile(final String uId,String name);
    public void editProfile(final String uId,String pId ,String name, String dob);
    public void setDefaultProfile(final String Uid);
    public void addProfile(final String Uid, String name, String dob);
    public void switchProfile(final String Uid, final String profileId);
    public void deleteProfile(String Uid, String profileId);
}