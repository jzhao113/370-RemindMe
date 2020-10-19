package edu.qc.seclass.glm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        nb.setContentTitle(extras.getCharSequence("type"));
        nb.setContentText(extras.getCharSequence("desc"));
        notificationHelper.getManager().notify(1, nb.build());
    }
}
