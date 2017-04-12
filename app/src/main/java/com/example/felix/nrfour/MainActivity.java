package com.example.felix.nrfour;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private static String username;
    private static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
    }

    public void login(View view){

        username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
        password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
        if (Util.isValidate(username) && Util.isValidate(password)) {
            new LoginTask(this).execute(new String[]{"username", "password"});
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public void signup(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, SignUp.class);
                startActivity(intent);
            }
        });
        thread.start();
    }


    private class LoginTask extends AsyncTask<String, Void, Void> {
        private Context context;
        private User userLoggingIn;
//        private String username;
//        private String password;
        private boolean cont = false;

        public LoginTask(Context context) {
            this.context = context;
        }
        @Override
        protected Void doInBackground(String... userinfo) {
            userLoggingIn = null;
            if (!Util.unusedUsername(username)) {
                userLoggingIn = Util.getUser(username);
                cont = true;
            } else {
                System.out.println("MAINACT: Unknown username");
                cont = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            if (cont == false) {
                Toast.makeText(context, "The username is incorrect", Toast.LENGTH_SHORT).show();}
            else if (BCrypt.hashpw(password, userLoggingIn.getSalt()).equals(userLoggingIn.getPassword())) {
                System.out.println("Password Valid");
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("username", userLoggingIn.getUsername());
                System.out.println("Sending"+userLoggingIn.getUserID());
                intent.putExtra("passwordReference", password);
                context.startActivity(intent);
            }
        }
    }
}
