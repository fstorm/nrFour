package com.example.felix.nrfour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Felix on 07/04/2017.
 */

public class SignUp extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        // need to give list activity the user_id


        // need to create a new account and add it to the db if it is accepted
    }

    public void onSignupClicked(View view) {
    if (checkPassword()) {
        String username = ((EditText) findViewById(R.id.signupUsernameField)).getText().toString();
        String salt = BCrypt.gensalt();
        String password = ((EditText) findViewById(R.id.signupPassword1Field)).getText().toString();
        String passwordHashed = BCrypt.hashpw(password, salt);
        String IV = Encrypter.generateIV();
        Util.insertIntoUsers(new User(username, passwordHashed, salt, IV));

        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("userIDReference", username);
        intent.putExtra("passwordReference", password);
        startActivity(intent);
    }

    }

    private boolean checkPassword() {
        EditText username = (EditText) findViewById(R.id.signupUsernameField);
        EditText pass1 = (EditText) findViewById(R.id.signupPassword1Field);
        EditText pass2 = (EditText) findViewById(R.id.signupPassword2Field);

        boolean validUsername = false;
        boolean validPassLength = false;
        boolean validPasswordMatch = false;

        Util.connect();
        if (Util.validUsername(username.getText().toString())) {
            validUsername = true;
        } else {
            Toast.makeText(this, "That username has all ready been used", Toast.LENGTH_SHORT);
            return false;
        }

        if (pass1.getText().length() >= 10) {
            validPassLength = true;
        } else {
            Toast.makeText(this, "Please choose a password of minimum length 10 characters", Toast.LENGTH_SHORT);
            return false;
        }

        if (pass1.getText().equals(pass2.getText())) {
            validPasswordMatch = true;
        } else {
            Toast.makeText(this, "Make sure both passwords are the same", Toast.LENGTH_SHORT);
            return false;
        }
        if (validUsername && validPassLength && validPasswordMatch) {
            return true;
        }
        return false;
    }

}
