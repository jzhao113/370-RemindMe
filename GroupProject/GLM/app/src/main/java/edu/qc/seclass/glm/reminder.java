package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class reminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText editTitle, editDesc, editType, editLong, editLat;
    private TextView mTextView;//for alarm
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //Bundle b is empty if its a new reminder, but is full if its a already set reminder
        //PREPEARING DATA
        Intent in = getIntent();
        final Bundle b = in.getExtras();
        String tempTitle = "",tempType = "",tempLong="",tempLat="",tempCheckBox="";
        if (b != null) {
            tempTitle = b.getString("value").trim().toLowerCase();
            tempType = b.getString("value2").trim().toLowerCase();
            tempLong = b.getString("value3");
            tempLat = b.getString("value4");
            tempCheckBox = b.getString("value5");
            pasteData(tempTitle, tempType,tempLong,tempLat,tempCheckBox);
        }
        mTextView = (TextView) findViewById(R.id.alarmTextView);
        final String finalTitle = tempTitle;
        final String finalType = tempType;
        final String finalLong = tempLong;
        final String finalLat =tempLat;
        final String finalCheckBox = tempCheckBox;

        Button pickTime;
        pickTime = (Button) findViewById(R.id.chooseTime);
        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        Button chooseDate;
        chooseDate = (Button) findViewById(R.id.chooseDay);
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        //THE MAIN ADD BUTTON FUNCTION
        //CONTAINS ALL IF ELSE IF AND ELSES FOR EDGE CASES
        Button btnMenu;
        btnMenu = (Button) findViewById(R.id.backToMenu);
        Button btnDelete;
        editType = (EditText) findViewById(R.id.type);
        editDesc = (EditText) findViewById(R.id.desc);
        editLong = (EditText) findViewById(R.id.longitude);
        editLat = (EditText)  findViewById(R.id.latitude);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PREPEARING DATA FOR USAGE
                String type = editType.getText().toString().trim().toLowerCase();
                String desc = editDesc.getText().toString().trim().toLowerCase();
                String longg = editLong.getText().toString().trim().toLowerCase();
                String lat = editLat.getText().toString().trim().toLowerCase();
                boolean test =checkIfNumber(longg)&&checkIfNumber(lat);
                CheckBox check = (CheckBox) findViewById(R.id.checkBox);
                String checkStat="";
                if(check.isChecked())
                    checkStat="TRUE";
                else
                    checkStat="FALSE";

                //checking if only lat or long has a value in it and the other doesn't
                if(desc.equals("")||type.equals(""))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "NEED DESC AND TYPE TO BE FILLED";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(((longg.equals("")&&!lat.equals(""))||(!longg.equals("")&&lat.equals("")))||
                        (!test&&!longg.equals("")&&!lat.equals(""))||
                        (test&&((Float.parseFloat(longg)>180||Float.parseFloat(longg)<-180)||
                                (Float.parseFloat(lat)>90 || Float.parseFloat(lat)<-90))))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "INVALID COORD COMBINATION";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(b == null) {
                    boolean good = false;

                    if(longg.equals("")&&longg.equals(""))
                    {
                        longg="200";
                        lat="200";
                    }

                    if (!user.db.checkAlreadyExist(type)) {
                        user.db.insertList(type);
                        user.db.insertReminder(desc, type,Float.parseFloat(longg),Float.parseFloat(lat),checkStat);
                        good = true;
                    } else if (!user.db.checkReminderAlreadyExist(type, desc)) {
                        user.db.insertReminder(desc, type,Float.parseFloat(longg),Float.parseFloat(lat),checkStat);
                        good = true;
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "IT ALREADY EXISTS";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    if (good)
                        goMenu();
                }
                else
                {
                    boolean good = false;
                    int id=-1;

                    //prepping data
                    if(longg.equals("")&&longg.equals(""))
                    {
                        longg="200";
                        lat="200";
                    }
                    Cursor resII = user.db.getAllReminderData();
                    while (resII.moveToNext()) {
                        if (resII.getString(2).equals(finalType) && resII.getString(1).equals(finalTitle)) {
                            id = resII.getInt(0);
                            break;
                        }
                    }

                    //checking if everything is the same
                    if (type.equals(finalType) && desc.equals(finalTitle) &&longg.equals(finalLong) &&lat.equals(finalLat)) {
                        good = true;
                        user.db.updateList(id, desc,longg,lat,checkStat);
                    }
                    else if(type.equals(finalType) && desc.equals(finalTitle)&&( ! (longg.equals(finalLong)) || ! (lat.equals(finalLat)) ))
                    {
                        user.db.updateList(id, desc,longg,lat,checkStat);
                        good = true;
                    }
                    //checking if only the desc changed but type is same
                    else if (type.equals(finalType) && !user.db.checkReminderAlreadyExist(type, desc)) {
                        user.db.updateList(id, desc,longg,lat,checkStat);
                        good = true;
                    }
                    //switching types for a desc
                    else if (user.db.checkAlreadyExist(type) && !user.db.checkReminderAlreadyExist(type, desc)) {
                        //this is to update the list
                        user.db.updateListAndType(desc, type,longg,lat, checkStat,id);

                        //checking if its the last value inside the list, if so delete list
                        Cursor res = user.db.getAllReminderData();
                        int counter = 0;
                        while (res.moveToNext()) {
                            if (res.getString(2).equals(finalType)) {
                                counter++;
                            }
                        }

                        if (counter == 0)
                            user.db.deleteFromList(finalType);

                        good = true;
                    }
                    else if (!user.db.checkAlreadyExist(type) && !user.db.checkReminderAlreadyExist(type, desc))
                    {

                        user.db.insertList(type);
                        user.db.updateListAndType(desc, type,longg,lat,checkStat,id);

                        Cursor res = user.db.getAllReminderData();
                        int counter = 0;
                        while (res.moveToNext()) {
                            if (res.getString(2).equals(finalType)) {
                                counter++;
                            }
                        }

                        if (counter == 0)
                            user.db.deleteFromList(finalType);

                        good = true;
                    }
                    else {
                        Context context = getApplicationContext();
                        CharSequence text = "IT ALREADY EXISTS";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    if (good)
                        goMenu();
                }
            }
        });

    }

    //used to head back to user
    private void goMenu() {
        Intent intent = new Intent(this, user.class);
        startActivity(intent);
    }

    //used to clear all data
    private void clear() {
        TextView mTextView = (TextView) findViewById(R.id.desc);
        TextView nTextView = (TextView) findViewById(R.id.type);
        TextView oTextView = (TextView) findViewById(R.id.longitude);
        TextView pTextView = (TextView) findViewById(R.id.latitude);
        mTextView.setText("");
        nTextView.setText("");
        oTextView.setText("");
        pTextView.setText("");
    }

    //used to put data into textviews if bundle b is full
    private void pasteData(String desc, String type, String longg, String lat,String cb) {
        TextView mTextView = (TextView) findViewById(R.id.desc);
        TextView nTextView = (TextView) findViewById(R.id.type);
        TextView oTextView = (TextView) findViewById(R.id.longitude);
        TextView pTextView = (TextView) findViewById(R.id.latitude);
        CheckBox check = (CheckBox) findViewById(R.id.checkBox);
        mTextView.setText(desc);
        nTextView.setText(type);
        if(Float.parseFloat(longg)==200||Float.parseFloat(lat)==200)
        {
            oTextView.setText("");
            pTextView.setText("");
        }
        else
        {
            oTextView.setText(longg);
            pTextView.setText(lat);
        }
        if(cb.equals("TRUE"))
            check.setChecked(true);
        else
            check.setChecked(false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        user.db.addAlarm(editDesc.getText().toString(), editType.getText().toString(), hour, minute);
        updateTimeText(c);

        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTextView.setText(timeText);
    }
    private void startAlarm(Calendar c) { // reminderID is the primary key of reminders table
        String type = editType.getText().toString().trim().toLowerCase();
        String desc = editDesc.getText().toString().trim().toLowerCase();

        int reqCode = createUniqueRequestCode(type, desc);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("desc", desc);
        intent.putExtra("type", type);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reqCode, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);/*
        rtc wake up wakes up the alarm, c.getTimeInMs gets the time the user choose in miliseconds, and then
         the pending intent which will become an actual event as an alarm.*/
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Toast.makeText(this, "" + month + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
    }

    //used to check of a long or lat is a number which can be used
    private boolean checkIfNumber(String x)
    {
        try {
            double d = Float.parseFloat(x);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private int createUniqueRequestCode(String a, String b) {
        /* TODO: choose a better algorithm. does not work for all cases. */
        String str = a+b;
        int code = 0;
        for(int i=0; i<str.length(); i++) {
            code+= (int) str.charAt(i);
        }
        return code;
    }

    private void goMenu(View view) {
        Intent intent = new Intent(this, user.class);
        startActivity(intent);
    }
}