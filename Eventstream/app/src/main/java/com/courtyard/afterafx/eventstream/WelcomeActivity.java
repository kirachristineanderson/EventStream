package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/** Activity that displays our Welcome screen, gives users an option to sign in or sign up!*/

//only pushing the app folder test


public class WelcomeActivity extends Activity {
    static int testloadonlyappfolder;
    static int testloadonlyappfolder2;
    static int testloadonlyappfolder3;
    static int testloadonlyappfolder4;
    static int testloadonlyappfolder5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Log in button click handler
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Starts an intent of the log in activity
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

            }
        });

        // Sign up button click handler
        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Starts an intent for the sign up activity
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));

            }
        });
    }
}