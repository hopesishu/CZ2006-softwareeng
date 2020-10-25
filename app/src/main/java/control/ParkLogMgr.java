package control;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import entity.Park;
import entity.ParkLogEntry;
import entity.ParkLogMgrInterface;


/**
 * implements ParkLogMgrInterface
 * allows the app to interact with firebase database, acting as an control class for ParkLogEntry (entity class)
 */
public class ParkLogMgr implements ParkLogMgrInterface {

    private FirebaseDatabase database;
    private DatabaseReference parksRef;
    private DatabaseReference userRef;

    /**
     * delete parkLogEntry
     * @param userId
     * @param parkName
     */
    @Override
    public void deleteParkLogEntry(String userId, String parkName) {
        database = FirebaseDatabase.getInstance();
        parkssRef = database.getReference("Parks");
        userRef = database.getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileId="";
                //get profile id
                if (dataSnapshot.child(userId).child("profiles").getChildrenCount()!=1)
                {
                    for (DataSnapshot data: dataSnapshot.child(userId).child("profiles").getChildren())
                    {
                        System.out.println("finding profile....." + data.child("thisProfile").getValue(boolean.class));

                        //then error here how
                        if (data.child("thisProfile").getValue(boolean.class))
                        {
                            profileId = data.getKey();
                            System.out.println("profile id: "+profileId);
                            break;

                        }
                    }
                }
                else
                    profileId = "0";

                System.out.println("vaccine log entry count" + dataSnapshot.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").getChildrenCount());
                for (DataSnapshot data : dataSnapshot.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").getChildren()) {
                    System.out.println(data.getValue(VaccineLogEntry.class).getVaccine().getName() + "vaccineee nam eeeee");
                    if (data.getValue(VaccineLogEntry.class).getVaccine().getName() == vaccineName) {
                        userRef.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").child(data.getKey()).removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * update vaccine log entry
     * @param userId
     * @param vaccineName
     * @param dateTaken
     * @param dateDue
     * @param reminder
     */
    @Override
    public void updateVaccineLogEntry(String userId, String vaccineName, Date dateTaken, Date dateDue, String reminder) {
        database = FirebaseDatabase.getInstance();
        vaccinesRef = database.getReference("Vaccines");
        userRef = database.getReference("users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileId="";
                //get profile id
                if (dataSnapshot.child(userId).child("profiles").getChildrenCount()!=1)
                {
                    for (DataSnapshot data: dataSnapshot.child(userId).child("profiles").getChildren())
                    {
                        System.out.println("finding profile....." + data.child("thisProfile").getValue(boolean.class));

                        //then error here how
                        if (data.child("thisProfile").getValue(boolean.class))
                        {
                            profileId = data.getKey();
                            System.out.println("profile id: "+profileId);
                            break;

                        }
                    }
                }
                else
                    profileId = "0";

                String key="";
                System.out.println("vaccine log entry count"+ dataSnapshot.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").getChildrenCount());
                for (DataSnapshot data: dataSnapshot.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").getChildren())
                {
                    System.out.println(data.getValue(VaccineLogEntry.class).getVaccine().getName() + "vaccineee nam eeeee");
                    if (data.getValue(VaccineLogEntry.class).getVaccine().getName()==vaccineName)
                    {
                        key =data.getKey();
                    }
                }
                System.out.println("keyy for vaccine entry log: "+key);
                Vaccine vaccine = dataSnapshot.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").child(key).getValue(VaccineLogEntry.class).getVaccine();
                userRef.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").child(key).removeValue();
                VaccineLogEntry vaccineLogEntry;
                if (reminder.compareTo("true")==0)
                    vaccineLogEntry = new VaccineLogEntry(dateTaken, vaccine, dateDue, true);
                else
                    vaccineLogEntry = new VaccineLogEntry(dateTaken, vaccine, dateDue, false);


                final String key1 = userRef.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").push().getKey();
                //hash.put(key, vaccineLogEntry);
                System.out.println(key + "KEYYYY");
                userRef.child(userId).child("profiles").child(profileId).child("vaccineLogEntries").child(key1).setValue(vaccineLogEntry);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * add vaccineLogEntry
     * @param userId
     * @param date
     * @param vaccineName
     */
    @Override
    public void addVaccineLogEntry(String userId, Date date, String vaccineName) {
        database = FirebaseDatabase.getInstance();
        vaccinesRef = database.getReference("Vaccines");
        userRef = database.getReference("users");


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileId="0";
                //get profile id
                if (dataSnapshot.child(userId).child("profiles").getChildrenCount()!=1)
                {
                    for (DataSnapshot data: dataSnapshot.child(userId).child("profiles").getChildren())
                    {
                        System.out.println("finding profile....." + data.child("thisProfile").getValue(boolean.class));

                        //then error here how
                        if (data.child("thisProfile").getValue(boolean.class))
                        {
                            profileId = data.getKey();
                            System.out.println("profile id: "+profileId);
                            break;

                        }
                    }
                }
                final String pId = profileId;
                vaccinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("final profile id to be added" + pId);
                        Vaccine vaccine = dataSnapshot.child(vaccineName).getValue(Vaccine.class);
                        if (dataSnapshot.child(vaccineName).child("oneTime").getValue(boolean.class))
                        {
                            System.out.println("no got reminder");
                            final VaccineLogEntry vaccineLogEntry = new VaccineLogEntry(date, vaccine);
                            System.out.println(userId + "user IDDDDDD");
                            final String key = userRef.child(userId).child("profiles").child(pId).child("vaccineLogEntries").push().getKey();
                            userRef.child(userId).child("profiles").child(pId).child("vaccineLogEntries").child(key).setValue(vaccineLogEntry);
                        }
                        else
                        {
                            System.out.println("got reminder");
                            Date nextDue;
                            if (date.getMonth()+vaccine.getNumMonths()<=12)
                            {
                                int mth = date.getMonth()+vaccine.getNumMonths();
                                nextDue = new Date(date.getYear(), mth, date.getDate());
                            }
                            else
                            {
                                int mth = date.getMonth()+vaccine.getNumMonths()-12;
                                int year = date.getYear()+1;
                                nextDue = new Date(year, mth , date.getDate());
                            }

                            final VaccineLogEntry vaccineLogEntry= new VaccineLogEntry(date, vaccine, nextDue,true);
                            System.out.println(userId + "user IDDDDDD");
                            final String key = userRef.child(userId).child("profiles").child(pId).child("vaccineLogEntries").push().getKey();
                            userRef.child(userId).child("profiles").child(pId).child("vaccineLogEntries").child(key).setValue(vaccineLogEntry);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * retrieve vaccineLogEntries that require reminder
     * @param myCallback
     * @param Uid
     * @return ArrayList<VaccineLogEntry> through MyCallbackVaccineLog interface
     */
    @Override
    public void retrieveVaccineLogWithReminder(MyCallbackVaccineLog myCallback, String Uid) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileId="";
                //get profile id
                if (dataSnapshot.child(Uid).child("profiles").getChildrenCount()!=1)
                {
                    for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").getChildren())
                    {
                        System.out.println("finding profile....." + data.child("thisProfile").getValue(boolean.class));

                        //then error here how
                        if (data.child("thisProfile").getValue(boolean.class))
                        {
                            profileId = data.getKey();
                            System.out.println("profile id: "+profileId);
                            break;

                        }
                    }
                }
                else
                    profileId = "0";

                ArrayList<VaccineLogEntry> vaccineLogArrayList = new ArrayList<>();
                System.out.println("user id in firebase manager: " + Uid);
                for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").child(profileId).child("vaccineLogEntries").getChildren())
                {
                    if (data.getValue(VaccineLogEntry.class).getReminder())
                    {
                        vaccineLogArrayList.add(data.getValue(VaccineLogEntry.class));
                        System.out.println(data.getValue(VaccineLogEntry.class).getVaccine().getName() + "usre vaccine log");
                    }

                }
                myCallback.onCallback(vaccineLogArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * retrieve vaccineLogEntries
     * @param myCallback
     * @param Uid
     * @return ArrayList<VaccineLogEntry> through MyCallbackVaccineLog interface
     */
    @Override
    public void retrieveVaccineLog(MyCallbackVaccineLog myCallback, String Uid) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileId="";
                //get profile id
                if (dataSnapshot.child(Uid).child("profiles").getChildrenCount()!=1)
                {
                    for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").getChildren())
                    {
                        System.out.println("finding profile....." + data.child("thisProfile").getValue(boolean.class));

                        //then error here how
                        if (data.child("thisProfile").getValue(boolean.class))
                        {
                            profileId = data.getKey();
                            System.out.println("profile id: "+profileId);
                            break;

                        }
                    }
                }
                else
                    profileId = "0";

                ArrayList<VaccineLogEntry> vaccineLogArrayList = new ArrayList<>();
                System.out.println("user id in firebase manager: " + Uid);
                for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").child(profileId).child("vaccineLogEntries").getChildren())
                {
                    vaccineLogArrayList.add(data.getValue(VaccineLogEntry.class));
                    System.out.println(data.getValue(VaccineLogEntry.class).getVaccine().getName() + "usre vaccine log");
                }
                myCallback.onCallback(vaccineLogArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * retrieve vaccines in user VaccineLogEntries
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveUserVaccine(MyCallback myCallback, String Uid) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        System.out.println("retrieve user vaccine firebase manager");

        //get user vaccine
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Vaccine> vaccineArrayList = new ArrayList<>();
                String profileId="";
                System.out.println("user id in firebase manager: " + Uid);

                //find profile
                System.out.println("number of profiles" +dataSnapshot.child(Uid).child("profiles").getChildrenCount() );

                if (dataSnapshot.child(Uid).child("profiles").getChildrenCount()!=1)
                {
                    for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").getChildren())
                    {
                        System.out.println("finding profile....." + data.child("thisProfile").getValue(boolean.class));

                        //then error here how
                        if (data.child("thisProfile").getValue(boolean.class))
                        {
                            profileId = data.getKey();
                            System.out.println("profile id: "+profileId);
                            break;

                        }
                    }
                }
                else
                    profileId = "0";


                for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").child(profileId).child("vaccineLogEntries").getChildren())
                {
                    vaccineArrayList.add(data.getValue(VaccineLogEntry.class).getVaccine());
                    System.out.println(data.getValue(VaccineLogEntry.class).getVaccine().getName() + "user vaccine");
                }
                myCallback.onCallback(vaccineArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * retrieve all vaccines in the database
     * @param myCallback
     */
    @Override
    public void retrieveVaccines(MyCallback myCallback) {
        database = FirebaseDatabase.getInstance();
        vaccinesRef = database.getReference("Vaccines");
        vaccinesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Vaccine> vaccineArrayList = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    vaccineArrayList.add(data.getValue(Vaccine.class));
                }
                myCallback.onCallback(vaccineArrayList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(ArrayList<Vaccine> value);
    }

    public interface MyCallbackVaccineLog {
        void onCallback(ArrayList<VaccineLogEntry> value);
    }
}