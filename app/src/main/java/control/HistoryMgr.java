package control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import entity.History;
import entity.HistoryMgrInterface;

public class HistoryMgr implements HistoryMgrInterface {
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String uId;
    private ArrayList<History> history;

    public void addHistory(final String historyLocation, final String Uid)
    {
        history = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.child(Uid).child("history").getChildren()) {
//                    String pastDate = data.child("historyDate").getValue(String.class);
//                    String pastLocation = data.child("location").getValue(String.class);
//                    Log.d("error", "onDataChange: ");
//                    history.add(new History(pastDate, pastLocation));
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//
//        });

        history.add(new History(historyLocation,strDate));
        userRef.child(Uid).child("history").push().setValue(history);
    }

    public void getHistory(final MyCallbackHistory myCallback, final String Uid) {
        database = FirebaseDatabase.getInstance();
        history = new ArrayList<>();
        userRef = database.getReference("users").child(Uid).child("history");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String pastDate = data.child("historyDate").getValue().toString();
                    String pastLocation = data.child("location").getValue().toString();
                    Log.d("error", "onDataChange: ");
                    history.add(new History(pastDate, pastLocation));
                }
                myCallback.onCallback(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }



    public interface MyCallbackHistory{
        void onCallback(ArrayList<History> history);
    }

    public interface MyCallbackString {
        void onCallback(String value);
    }

    public interface MyCallbackHashMap{
        void onCallback(HashMap<String,String> value);
    }
}
