package com.example.easyappointment.GoogleApi;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.data.Models.Appointments;

import java.util.Date;

public class GoogleCalendar {

    public static final int MY_PERMISSION_READ_CALENDAR = 1;
    public static final int MY_PERMISSION_WRITE_CALENDAR = 2;

    private HomePageActivity host;

    public GoogleCalendar(HomePageActivity host) {
        this.host = host;
    }

    public void getCalendarInfo() {
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };
        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        // Run query
        Cursor cur = null;
        ContentResolver cr = host.getContentResolver();
        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            uri = CalendarContract.Calendars.CONTENT_URI;
        }

        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(host, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(host, new String[]{Manifest.permission.READ_CALENDAR}, MY_PERMISSION_READ_CALENDAR);
            Log.d("Calendar", "permission");
        }

        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        Log.d("Calendar", String.valueOf(cur.getCount()));
        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createClientCalendarEvent(Appointments appointment) {
        if (ActivityCompat.checkSelfPermission(host, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(host, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSION_WRITE_CALENDAR);
        }
        ContentResolver cr = host.getContentResolver();

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(new Date(appointment.start_time));

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(new Date(appointment.end_time));

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, appointment.provider_service.getTarget().service.getTarget().name);
        values.put(CalendarContract.Events.DESCRIPTION, appointment.provider_service.getTarget().provider.getTarget().account.getTarget().name);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Bucharest");
        values.put(CalendarContract.Events.EVENT_LOCATION, appointment.provider_service.getTarget().provider.getTarget().address);

        cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.d("Event", "succeded");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createProviderCalendarEvent(Appointments appointment) {
        Log.d("Event", "permission");
        ContentResolver cr = host.getContentResolver();

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(new Date(appointment.start_time));

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(new Date(appointment.end_time));

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, appointment.provider_service.getTarget().service.getTarget().name);
        values.put(CalendarContract.Events.DESCRIPTION, appointment.client.getTarget().account.getTarget().name);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Bucharest");
        values.put(CalendarContract.Events.EVENT_LOCATION, appointment.provider_service.getTarget().provider.getTarget().address);

        cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.d("Event", "succeded");
    }
}
