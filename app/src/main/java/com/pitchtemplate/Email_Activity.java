package com.pitchtemplate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class Email_Activity extends AppCompatActivity {

    String emailAdd, str_subject, str_message;
    EditText et_subject, et_message;
    TextView tv_emailAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    void init() {
        setContentView(R.layout.feedback);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et_subject = (EditText)findViewById(R.id.et_feedback_subject);
        et_message = (EditText)findViewById(R.id.et_feedback_message);
        tv_emailAddress = (TextView)findViewById(R.id.tv_email_emailAddress);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String type = extras.getString("KEY");
        emailAdd = extras.getString("email");
        tv_emailAddress.setText(emailAdd);

        if (type.equals("feedback")) {
            getSupportActionBar().setTitle("Feedback");
            et_subject.setText("Feedback for event");
        }
        else if (type.equals("query")) {
            getSupportActionBar().setTitle("Query");
            et_subject.setText("Query for event");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_openener, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_send) {
            String EmailAddresses[] = {emailAdd};
            str_subject = et_subject.getText().toString();
            str_message = et_message.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, EmailAddresses);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, str_subject);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_TEXT, str_message);
            startActivity(emailIntent);

            return true;
        }

        if (id == android.R.id.home) {
            Intent i = new Intent(Email_Activity.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

