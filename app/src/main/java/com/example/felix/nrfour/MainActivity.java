package com.example.felix.nrfour;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
    }

    public void login(View view){

//        new Login(this, ((EditText) findViewById(R.id.usernameField)).getText().toString(),
//                ((EditText) findViewById(R.id.passwordField)).getText().toString()).execute(URL);

Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        Util.connect();
        String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
        User userLoggingIn = null;
        if (Util.validUsername(username)) {
            userLoggingIn = Util.getUser(username);
            String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
            if (BCrypt.hashpw(password, userLoggingIn.getSalt()).equals(userLoggingIn.getPassword())) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("userIDReference", userLoggingIn.getUserID());
                startActivity(intent);
            }
        }
    }
});

        thread.start();
    }

    public void signup(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Util.connect();
                Intent intent = new Intent(context, SignUp.class);
                startActivity(intent);
            }
        });
        thread.start();
    }
}
