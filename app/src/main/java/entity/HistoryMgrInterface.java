package entity;

import control.HistoryMgr;

public interface HistoryMgrInterface {
    public void addHistory(final String historyLocation, final String Uid);
    public void getHistory(final HistoryMgr.MyCallbackHistory myCallback, final String Uid);
}
