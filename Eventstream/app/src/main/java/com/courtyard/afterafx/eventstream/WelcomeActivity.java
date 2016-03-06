package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/** Activity that displays our Welcome screen, gives users an option to sign in or sign up!*/

public class WelcomeActivity extends Activity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedPreferences.contains("username") && sharedPreferences.contains("password")){

            String username = sharedPreferences.getString("username", null);
            String password = sharedPreferences.getString("password", null);

            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        // Show the error message
                        Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        // Start an intent for the dispatch activity
                        Intent intent = new Intent(WelcomeActivity.this, DispatchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
        }

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