package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.*;
import android.view.*;
import android.content.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.*;

public class user extends AppCompatActivity {

    //static database used for all classes
    private NotificationManagerCompat notificationManager;
    static DatabaseHelper db;
    private final ArrayList<Integer> IDs = new ArrayList<>();
    private final HashMap<String, Integer> valueToId = new HashMap<>();
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //static database for the entire app
        db = new DatabaseHelper(this);

        //Manager needed to be created to send notifictions
        notificationManager =NotificationManagerCompat.from(this);
        //displaying list and starting location tracking
        displayList();
        location();

        //search bar
        final SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 search(query);
                 searchBar.clearFocus();
                 return true;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 return false;
             }
         });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //add reminder button
        FloatingActionButton btnAdd;
        btnAdd = (FloatingActionButton)findViewById(R.id.addReminder);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addReminder();
            }
        });
    }

    private void addReminder()
    {
        //start the reminder class
        Intent intent = new Intent(this, reminder.class);
        startActivity(intent);
    }

    private void displayList()
    {
        //Preparing data for displaying list
        final ArrayList<String> values = db.getValues();
        final HashMap<String, ArrayList<String>> internals=  db.getInternals();

        //declaring ExpandableList and its adapter CREATES ACTUAL DISPLAY.
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.ReminderLists);
        ExpandableListAdapter listAdapter = new MyExListAdapter(this, values,internals);
        lv.setAdapter(listAdapter);
    }

    //searches for database for the reminder name
    private void search(String temp)
    {
        temp = temp.trim().toLowerCase();
        Cursor res = user.db.getAllReminderData();
        boolean flag=true;
        Bundle b = new Bundle();
        int i =0;
        while(res.moveToNext())
        {
            if(res.getString(1).equals(temp))
            {
                ArrayList<String> tempArray =  new ArrayList<String>();
                tempArray.add(res.getString(1));
                tempArray.add(res.getString(2));
                tempArray.add(res.getString(6));
                tempArray.add(res.getString(7));
                tempArray.add(res.getString(3));

                b.putStringArrayList(Integer.toString(i),tempArray);
                i++;
                flag=false;
            }
        }

        if(flag)
        {
            Context context = getApplicationContext();
            CharSequence text = "Nothing Found";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0,200);
            toast.show();
        }
        else
        {
            b.putInt("total",i);
            Intent in = new Intent(getApplicationContext(), search.class);
            in.putExtras(b);
            startActivity(in);
        }


    }

    //used to actively track location and any reminders needed
    private void location() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            //constantly checking updated location to any reminders which may be close to that location
            @Override
            public void onLocationChanged(Location location) {
                Context context = getApplicationContext();
                CharSequence text = location.getLongitude()+"\n"+location.getLatitude();

                //toast just used for debugging purposes
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Cursor res = user.db.getAllReminderData();
                while (res.moveToNext()) {
                    if (Math.floor(res.getFloat(6))==Math.floor(location.getLongitude())&&Math.floor(res.getFloat(7))==Math.floor(location.getLatitude())) {
                        sendOnChannel(res.getString(1));
                        db.updateList(res.getInt(0),res.getString(1),"200","200",res.getString(3));
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[]x = new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET};

                ActivityCompat.requestPermissions(this,x,10);
                return;
            }
        }
        else
        {
            try
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            }
            catch(SecurityException se)
            {
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    try
                    {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
                    }
                    catch(SecurityException se)
                    {
                    }
                }
                return;
        }
    }

    //used to send notifications
    private void sendOnChannel(String desc)
    {
        android.app.Notification notification = new NotificationCompat.Builder(this, Notification.CHANNEL_ID)
                .setContentTitle("LOCATION ALERT")
                .setContentText("Reminder: "+desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }
}
