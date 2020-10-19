package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="reminderAPP.db";
    public static final String REMINDER_TABLE = "reminder_table";
    public static final String LIST_TABLE = "list_table";

    public static final String COL1 ="ID";
    public static final String COL2 = "description";
    public static final String COL3 = "type";
    public static final String COL4 = "checkBoxStatus";
    public static final String COL5 = "hour";
    public static final String COL6 = "minute";
    public static final String COL7 = "longitude";
    public static final String COL8 = "latitude";

    public static final String COLB1 = "type";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table "+REMINDER_TABLE+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,description TEXT, type TEXT,checkBoxStatus TEXT,hour INTEGER, minute INTEGER,longitude FLOAT,latitude FLOAT)");
        db.execSQL("create table "+LIST_TABLE+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, FOREIGN KEY (ID) REFERENCES reminder_table(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+REMINDER_TABLE);
        onCreate(db);
    }


    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        Cursor res = getAllListData();

        //grabbing all reminderLists
        while (res.moveToNext()) {
            values.add(res.getString(1));
        }
        return values;
    }
            //user.db.addAlarm(editDesc.getText().toString(),editType.getText().toString(),hour,minute);
            //todo:if user deletes a reminder, we have to delete the alarm for that reminder somehow
    public void addAlarm(String desc, String type, int hour, int minute){
        if(checkReminderAlreadyExist(desc,type)){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + "hour" + " = "+ hour + " WHERE " + COL2+ " =" + desc +" AND " + COL3 + " = " + minute);
            db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + "minute" + " = "+ minute + " WHERE " + COL2+ " =" + desc +" AND " + COL3 + " = " + minute);
        }
    }
    public HashMap<String, ArrayList<String>> getInternals() {
        HashMap<String, ArrayList<String>> internals = new HashMap<>();
        Cursor res2 = getAllReminderData();

        while (res2.moveToNext()) {
            String temp = res2.getString(2);
            if (internals.get(temp) == null) {
                ArrayList<String> putArray = new ArrayList<>();
                putArray.add(res2.getString(1));
                internals.put(temp, putArray);
            } else {
                ArrayList putArray = internals.get(temp);
                putArray.add(res2.getString(1));
                internals.put(temp, putArray);
            }
        }
        return internals;
    }
    public boolean insertReminder(String description, String type,float longitude, float latitude,String checkBox)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if(!description.equals("")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, description);
            contentValues.put(COL3, type);
            contentValues.put(COL4,checkBox);
            contentValues.put(COL7,longitude);
            contentValues.put(COL8,latitude);
            long result = db.insert(REMINDER_TABLE, null, contentValues);
            if (result == -1)
                return false;

            return true;
        }

        return false;

    }
    public void updateList(int ID, String newVal, String longitude, String latitude,String cb){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL2 + " = '"+ newVal + "' WHERE " + COL1 + " IN ('" + ID +"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL7 + " = '"+ longitude + "' WHERE " + COL1 + " IN ('" + ID +"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL8 + " = '"+ latitude + "' WHERE " + COL1 + " IN ('" + ID +"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL4 + " = '"+ cb + "' WHERE " + COL1 + " IN ('" + ID +"')");
        //db.execSQL("UPDATE " + LIST_TABLE + " SET " + COL2 + " = '"+ newVal + "' WHERE " + COL2 + " = '" + currentVal +"'");
    }
    public void updateListAndType(String newVal,String newType, String longitude, String latitude, String cb, int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL2 + " = '"+ newVal + "' WHERE " + COL1 + " IN ('" + ID+"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL3 + " = '"+ newType + "' WHERE " + COL1 + " IN ('" + ID+"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL7 + " = '"+ longitude + "' WHERE " + COL1 + " IN ('" + ID +"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL8 + " = '"+ latitude + "' WHERE " + COL1 + " IN ('" + ID +"')");
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL4 + " = '"+ cb + "' WHERE " + COL1 + " IN ('" + ID +"')");
    }
    public void deleteFromList(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REMINDER_TABLE + " WHERE " + COL3 + " IN ('" + type + "')");
        db.execSQL("DELETE FROM " + LIST_TABLE + " WHERE " + COL3 + "= '" + type + "'");
    }
    //there is a bug: you have to navigate back to parent list and back to the list to see the deletion
    public void delete(String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REMINDER_TABLE + " WHERE " + COL2 + "= '" + desc + "'");
    }

    public void updateStatus(int id, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + COL4 + " = '"+ status + "' WHERE " + COL1 + " IN ('" + id +"')");
    }

    public boolean insertList(String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLB1,type);
        long result = db.insert(LIST_TABLE, null, contentValues);
        if(result == -1)
            return false;

        return true;
    }

    public Cursor getAllReminderData()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+REMINDER_TABLE,null);
        return res;
    }

    public Cursor getAllListData()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+LIST_TABLE,null);
        return res;
    }

    public boolean checkAlreadyExist(String type)
    {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + LIST_TABLE + " WHERE " + COLB1 + " =?";

        Cursor cursor = db.rawQuery(selectString, new String[] {type});

        if(cursor.getCount()>0){
            return true;
        }

        return false;
    }

    public boolean checkReminderAlreadyExist(String type, String desc)
    {

        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + REMINDER_TABLE + " WHERE " + COL1 + " =?";
        Cursor res = db.rawQuery("select * from "+REMINDER_TABLE,null);
        while(res.moveToNext())
        {
            if (res.getString(1).equals(desc) && res.getString(2).equals(type))
            {
                return true;
            }
        }

        return false;
    }
    //All reminders inside of a list should be unique as well
    public boolean checkAlreadyExist(String description, String type)
    {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + REMINDER_TABLE + " WHERE " + COL2 + " = '" + description + "' AND " + COL3 + " = '" + type + "'";
        Cursor cursor = db.rawQuery(selectString,null);
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }
}
