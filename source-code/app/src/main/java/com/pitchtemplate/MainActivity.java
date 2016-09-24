package com.pitchtemplate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    TextView tv_eventName, tv_eventType, tv_dateTime, tv_location, tv_headingDescription, tv_description,
            tv_openClosedStatus, tv_headingTargetAudience, tv_targetAudience, tv_headingDuration,
            tv_duration, tv_website, tv_sponsors, tv_headingOrg, tv_organizers, tv_headingContactDetails;
    Button bt_locateOnMap, bt_email, bt_phone, bt_setReminder, bt_feedback, bt_query, bt_openWebsite;
    String url;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        setFonts();

        setDataFromConfigFile();

        bt_setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder();
            }
        });

        bt_locateOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = getDataFromId(4);
                url = "http://maps.google.com/?q=" + address.substring(address.indexOf("Venue: ") + 7);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        bt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailAddresses[] = {bt_email.getText().toString()};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, EmailAddresses);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }
        });

        bt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallWrapper();
            }
        });

        bt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Email_Activity.class);
                i.putExtra("KEY", "feedback");
                i.putExtra("email", bt_email.getText().toString());
                startActivity(i);
            }
        });

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Email_Activity.class);
                i.putExtra("KEY", "query");
                i.putExtra("email", bt_email.getText().toString());
                startActivity(i);
            }
        });

        bt_openWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebsiteOpener.class);
                i.putExtra("KEY", getDataFromId(3));
                startActivity(i);
            }
        });
    }

    void makeCall() {
        String phoneNumber = getDataFromId(11);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    void makeCallWrapper() {
        int hasPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            hasPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            makeCall();
        }
        else
        {
            makeCall();
        }
    }

    void setReminder() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR,getYear());
        cal.set(Calendar.MONTH, getMonth());//Don't use exact numeric value of the month, use one minus.Ex: April=>as 3
        cal.set(Calendar.DATE, getDay());
        cal.set(Calendar.HOUR_OF_DAY, getHour());
        cal.set(Calendar.MINUTE, getMinute());
        cal.set(Calendar.SECOND, 0);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", tv_eventName.getText().toString());
        intent.putExtra("description", tv_description.getText().toString());
        intent.putExtra("eventLocation", getVenueLocation());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    makeCall();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "You denied calling permission!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void setDataFromConfigFile() {
        /*  0 - Event Name      1 - Event Type
            2 - timeDate        3 - websiteURL
            4 - Address         5 - Description
            6 - openClosed      7 - targetAudience
            8 - duration        9 - organizersName
            10 - orgEmail        11 - orgContact
            12 - sponsor         13 - appCredits        */
        tv_eventName.setText(getDataFromId(0));
        tv_eventType.setText(getDataFromId(1));
        tv_dateTime.setText(getDataFromId(2));
        //tv_website.setText(getDataFromId(3));
        tv_location.setText(getDataFromId(4));
        tv_description.setText(getDataFromId(5));
        tv_openClosedStatus.setText(getDataFromId(6));
        tv_targetAudience.setText(getDataFromId(7));
        tv_duration.setText(getDataFromId(8));
        tv_organizers.setText(getDataFromId(9));
        bt_email.setText(getDataFromId(10));
        bt_phone.setText(getDataFromId(11));
        tv_sponsors.setText(getDataFromId(12));
    }

    String getDataFromId(int idOfData) {
        List<Data> datas = null;
        String valueToBeReturned = null;
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            InputStream is=getAssets().open("config.xml");
            datas = parser.parse(is);
            String temp = datas.get(idOfData).toString();
            valueToBeReturned =  temp.substring(temp.indexOf("Name=") + 5);
        } catch (IOException e) {e.printStackTrace();}
        return valueToBeReturned;
    }

    void init() {
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tv_eventName = (TextView)findViewById(R.id.tv_preview_eventName);
        tv_eventType = (TextView)findViewById(R.id.tv_preview_eventType);
        tv_dateTime = (TextView)findViewById(R.id.tv_preview_dateTime);
        bt_setReminder = (Button)findViewById(R.id.bt_preview_setReminder);
        //tv_website = (TextView)findViewById(R.id.tv_preview_website);

        tv_location = (TextView)findViewById(R.id.tv_preview_location);
        bt_locateOnMap = (Button)findViewById(R.id.bt_preview_locateOnMap);

        tv_headingDescription = (TextView)findViewById(R.id.tv_preview_descriptionHeadingText);
        tv_description = (TextView)findViewById(R.id.tv_preview_description);
        tv_openClosedStatus = (TextView)findViewById(R.id.tv_preview_openCloseStatus);

        tv_headingTargetAudience = (TextView)findViewById(R.id.tv_preview_headingTargetAudience);
        tv_targetAudience = (TextView)findViewById(R.id.tv_preview_targetAudience);
        tv_headingDuration = (TextView)findViewById(R.id.tv_preview_headingEventDuration);
        tv_duration = (TextView)findViewById(R.id.tv_preview_eventDuration);

        tv_headingOrg = (TextView)findViewById(R.id.tv_preview_headingOrganizers);
        tv_organizers = (TextView)findViewById(R.id.tv_preview_organizers);
        tv_headingContactDetails = (TextView)findViewById(R.id.tv_preview_headingContactDetails);
        bt_email = (Button)findViewById(R.id.bt_preview_email);
        bt_phone = (Button)findViewById(R.id.bt_preview_phone);

        tv_sponsors = (TextView)findViewById(R.id.tv_preview_sponsors);

        bt_feedback = (Button)findViewById(R.id.bt_preview_feedback);
        bt_query = (Button)findViewById(R.id.bt_preview_query);
        bt_openWebsite = (Button)findViewById(R.id.bt_preview_openWebsite);
    }

    void setFonts() {
        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");
        tv_eventName.setTypeface(MontBold);
        tv_eventType.setTypeface(MontBold);
        tv_dateTime.setTypeface(MontBold);
        bt_setReminder.setTypeface(MontReg);
        //tv_website.setTypeface(MontReg);
        tv_location.setTypeface(MontBold);
        tv_headingDescription.setTypeface(MontBold);
        tv_description.setTypeface(MontReg);
        tv_openClosedStatus.setTypeface(MontBold);
        bt_locateOnMap.setTypeface(MontBold);
        tv_headingTargetAudience.setTypeface(MontBold);
        tv_targetAudience.setTypeface(MontReg);
        tv_headingDuration.setTypeface(MontBold);
        tv_duration.setTypeface(MontReg);
        tv_sponsors.setTypeface(MontBold);
        tv_headingOrg.setTypeface(MontBold);
        tv_organizers.setTypeface(MontReg);
        tv_headingContactDetails.setTypeface(MontBold);
        bt_email.setTypeface(MontBold);
        bt_phone.setTypeface(MontBold);
        bt_setReminder.setTypeface(MontReg);
        bt_feedback.setTypeface(MontReg);
        bt_query.setTypeface(MontReg);
        bt_openWebsite.setTypeface(MontReg);
    }

    String getVenueLocation() {
        String base = tv_location.getText().toString();
        String location = base.substring(base.indexOf("Venue: ") + 7);
        return location;
    }
    int getDay() {
        String base = tv_dateTime.getText().toString();
        String date = base.substring((base.indexOf("on ") + 3));
        date = date.substring(0, date.indexOf("/"));
        int day = Integer.parseInt(date);
        return day;
    }
    int getMonth() {
        String base = tv_dateTime.getText().toString();
        String date = base.substring((base.indexOf("on ") + 3));
        date = date.substring(3, 5);
        int month = Integer.parseInt(date) - 1;
        return month;
    }
    int getYear() {
        String base = tv_dateTime.getText().toString();
        String date = base.substring((base.indexOf("on ") + 3));
        date = date.substring(6);
        int year = Integer.parseInt(date) + 2000;
        return year;
    }
    int getHour() {
        String base = tv_dateTime.getText().toString();
        String time = base.substring((base.indexOf("at ") + 3), base.indexOf(":"));
        int hour = Integer.parseInt(time);
        if (base.contains("PM")){
            hour = hour + 12;
        }
        return hour;
    }
    int getMinute() {
        String base = tv_dateTime.getText().toString();
        String time = "";
        if (base.contains("PM")){
            time = base.substring((base.indexOf(":") + 1), base.indexOf(" PM"));
        }
        else if (base.contains("AM")) {
            time = base.substring((base.indexOf(":") + 1), base.indexOf(" AM"));
        }
        int minute = Integer.parseInt(time);
        return minute;
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}
