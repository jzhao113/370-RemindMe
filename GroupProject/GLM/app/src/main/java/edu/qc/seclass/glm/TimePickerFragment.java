package edu.qc.seclass.glm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {
//    Calendar c = Calendar.getInstance();
//    int hour = c.get(Calendar.HOUR_OF_DAY);
//    int minute = c.get(Calendar.MINUTE);
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //2nd parameter is ontimesetlistener and its basically what class will listen to the time being changed.
        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),hour,minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
}
