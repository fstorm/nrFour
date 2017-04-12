package com.example.felix.nrfour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Felix on 07/04/2017.
 */

public class SignUp extends Activity {

    private static Context context;
    private static EditText username;
    private static EditText pass1;
    private static EditText pass2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = this;
    }
    public void onSignupClicked(View view) {
        new PasswordChecker().execute();
    }

    public void registerNewUser() {
        String username = ((EditText) findViewById(R.id.signupUsernameField)).getText().toString();
        String salt = BCrypt.gensalt();
        String password = ((EditText) findViewById(R.id.signupPassword1Field)).getText().toString();
        String passwordHashed = BCrypt.hashpw(password, salt);
        String IV = Encrypter.generateIV();
        User newUser = new User(username, passwordHashed, salt, IV);
        if(Util.isValidate(username) && Util.isValidate(password)) {
            new SignupTask().execute(newUser);

            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("passwordReference", password);
            startActivity(intent);
        }else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public class PasswordChecker extends AsyncTask<Void, Void, Void> {

        private String username = ((EditText) findViewById(R.id.signupUsernameField)).getText().toString();
        private String pass1 = ((EditText) findViewById(R.id.signupPassword1Field)).getText().toString();
        private String pass2 = ((EditText) findViewById(R.id.signupPassword2Field)).getText().toString();

        private boolean usernameValid = false;

        @Override
        protected Void doInBackground(Void... params) {

            Util.connect();
            if (Util.unusedUsername(username)) {
                usernameValid = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (!usernameValid) {
                Toast.makeText(context, "Invalid username", Toast.LENGTH_SHORT).show();
            } else if (!(pass1.equals(pass2))) {
                Toast.makeText(context, "Passwords are not the same", Toast.LENGTH_SHORT).show();
            } else if (pass1.length()<10) {
                Toast.makeText(context, "Please enter a password of length 10 or more", Toast.LENGTH_LONG).show();
            }
             else {
                registerNewUser();
            }
        }
    }
}
