package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseInstallation;

//Activity which displays a login screen to the user, offering registration as well
/**--------------------------------------------------------*/

public class LoginActivity extends Activity {
    // UI references.
    private EditText usernameTextEdit;
    private EditText passwordTextEdit;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    /**--------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Set up the login form.
        usernameTextEdit = (EditText) findViewById(R.id.username);
        passwordTextEdit = (EditText) findViewById(R.id.password);
        passwordTextEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.edittext_action_login ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    login(null, null);
                    return true;
                }
                return false;
            }
        });

        if(sharedpreferences.contains("username") && sharedpreferences.contains("password")){
            login(sharedpreferences.getString("username", null), sharedpreferences.getString("password", null));
        }

        // Set up the submit button click handler
        Button signInButton = (Button) findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                login(null, null);

            }
        });
    }

    /**--------------------------------------------------------*/

    private void login(String username, String password) {

        username = "afterafx";
        password = "hacker";

        if(username == null || password == null) {
            username = usernameTextEdit.getText().toString().trim();
            password = passwordTextEdit.getText().toString().trim();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();
        }

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_field_required));
        if (username.length() == 0) {
            validationError = true;
            //validationErrorMessage.append(getString(R.string.error_invalid_email));
        }
        if (password.length() == 0) {
            if (validationError) {
                //validationErrorMessage.append(getString(R.string.error_incorrect_password));
            }
            validationError = true;
            //validationErrorMessage.append(getString(R.string.error_invalid_password));
        }
        //validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        //dialog.setMessage(getString(R.string.));
        dialog.show();
        // Call the Parse login method
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    /**--------------------------------------------------------*/
}
