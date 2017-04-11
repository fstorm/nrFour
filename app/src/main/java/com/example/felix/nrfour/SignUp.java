package com.example.felix.nrfour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Felix on 07/04/2017.
 */

public class SignUp extends Activity {

    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = this;

        // need to give list activity the user_id


        // need to create a new account and add it to the db if it is accepted
    }
    public void onSignupClicked(View view) {
        System.out.println("Pressed");
//        if (checkPassword()) {

        String username = ((EditText) findViewById(R.id.signupUsernameField)).getText().toString();
        String salt = BCrypt.gensalt();
        String password = ((EditText) findViewById(R.id.signupPassword1Field)).getText().toString();
        String passwordHashed = BCrypt.hashpw(password, salt);
        String IV = Encrypter.generateIV();
        User newUser = new User(username, passwordHashed, salt, IV);

        new SignupTask().execute(newUser);

        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("userIDReference", username);
        intent.putExtra("passwordReference", password);
        startActivity(intent);


//            System.out.println("Passwordchecker passed");
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("Inserting...");
//                    String username = ((EditText) findViewById(R.id.signupUsernameField)).getText().toString();
//                    String salt = BCrypt.gensalt();
//                    String password = ((EditText) findViewById(R.id.signupPassword1Field)).getText().toString();
//                    String passwordHashed = BCrypt.hashpw(password, salt);
//                    String IV = Encrypter.generateIV();
//                    Util.insertIntoUsers(new User(username, passwordHashed, salt, IV));
//                    System.out.println("Inserted");
//
//                    Intent intent = new Intent(context, ListActivity.class);
//                    intent.putExtra("userIDReference", username);
//                    intent.putExtra("passwordReference", password);
//                    startActivity(intent);
//                }
//
//        });
//        thread.run();
//        }
    }

    private boolean checkPassword() {
        System.out.println("Checking Password");
        EditText username = (EditText) findViewById(R.id.signupUsernameField);
        EditText pass1 = (EditText) findViewById(R.id.signupPassword1Field);
        EditText pass2 = (EditText) findViewById(R.id.signupPassword2Field);

        final boolean[] validUsername = {false};
        boolean validPassLength = false;
        boolean validPasswordMatch = false;

        new Thread() {
            public void run() {
                // do check here
                System.out.println("Running first check");
                Util.connect();
                if (Util.unusedUsername(username.getText().toString())) {
                    validUsername[0] = true;
                    System.out.println("First check is true");
                } else {
                    SignUp.this.runOnUiThread(new Runnable() {
                        public void run() {
                            // print here
                            System.out.println("Username used");
                            Toast.makeText(context, "That username has all ready been used", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }.run();

        if (pass1.getText().length() >= 10) {
            validPassLength = true;
        } else {
            System.out.println("Invalid Password 1");
            Toast.makeText(this, "Please choose a password of minimum length 10 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (true) {
//            pass1.getText().equals(pass2.getText())
            validPasswordMatch = true;
        } else {
            System.out.println("Invalid Password 2");
            Toast.makeText(this, "Make sure both passwords are the same", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (validUsername[0] && validPassLength && validPasswordMatch) {
            System.out.println("Returning true");
            return true;
        }
        System.out.println("Returning false");
        return false;
    }

}
