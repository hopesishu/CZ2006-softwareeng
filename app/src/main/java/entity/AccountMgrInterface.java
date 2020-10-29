package entity;

import android.app.Activity;
import android.view.View;

import control.AcctMgr;

public interface AccountMgrInterface {
    public void changePassword(String password, String uId);
    public void retrieveCurrentProfileName(final AcctMgr.MyCallbackString myCallback, final String Uid);
    public void retrieveEmailAdress(final AcctMgr.MyCallbackString myCallback, String Uid);
    public void retrieveSubprofileNameAndID(final AcctMgr.MyCallbackHashMap myCallback, final String Uid);
    public void createAccount(String email, String password, String firstName, String lastName, String dob, Activity activity);
    public void signIn(String email, String password, Activity activity);
    public void deleteAcc(String pw, View view, Activity activity, String uId);
}
