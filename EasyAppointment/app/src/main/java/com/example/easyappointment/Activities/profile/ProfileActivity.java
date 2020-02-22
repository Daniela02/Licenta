package com.example.easyappointment.Activities.profile;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.easyappointment.R;

public class ProfileActivity extends AppCompatActivity {

    public static final int MY_PERMISSION_READ_CALENDAR = 1;
    public static final int MY_PERMISSION_WRITE_CALENDAR = 2;
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    private TextView profileName, profileEmail;
    private ImageView profileImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileName = findViewById(R.id.profile_text);
        profileEmail = findViewById(R.id.profile_email);
        profileImage = findViewById(R.id.profile_image);

        Intent profileIntent = getIntent();
        setDataOnView(profileIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDataOnView(Intent profileIntent){
        profileName.setText(profileIntent.getStringExtra(NAME));
        profileEmail.setText(profileIntent.getStringExtra(EMAIL));
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                createCalendarEvent();
        }*/


    }

    ////////////////////////////////////////

    private void getCalendarInfo()  {
        // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
        final String[] EVENT_PROJECTION = new String[]{
                Calendars._ID,                           // 0
                Calendars.ACCOUNT_NAME,                  // 1
                Calendars.CALENDAR_DISPLAY_NAME,         // 2
                Calendars.OWNER_ACCOUNT                  // 3
        };
// The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            uri = Calendars.CONTENT_URI;
        }

// Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( this, new String[]{permission.READ_CALENDAR},MY_PERMISSION_READ_CALENDAR);
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
    private void createCalendarEvent(){
        if (ActivityCompat.checkSelfPermission(this, permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission.WRITE_CALENDAR}, MY_PERMISSION_WRITE_CALENDAR);
        }
        Log.d("Event", "permission");
        ContentResolver cr = getContentResolver();

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2019, 0, 3, 9, 30);

        Calendar endTime = Calendar.getInstance();
        endTime.set(2019, 0, 3, 10, 35);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, "Tech Stores");
        values.put(CalendarContract.Events.DESCRIPTION, "Successful Startups");
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Bucharest");
        values.put(CalendarContract.Events.EVENT_LOCATION, "London");

        cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.d("Event", "succeded");
    }

}
