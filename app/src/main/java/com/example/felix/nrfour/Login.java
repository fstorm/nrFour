package com.example.felix.nrfour;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telecom.Connection;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Felix on 20/03/2017.
 */

public class Login extends AsyncTask<String, Void, Connection> {

    private static ArrayList<User> users;
    private static Statement myStm;
    private final static String getAllUserInfo = "SELECT user_id, username, salt, password FROM users";
    private Context context;
    private String username;
    private String password;

    public Login(Context context, String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
    }

    // get the stuff
    @Override
    protected Connection doInBackground(String... params) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        java.sql.Connection myConn = null;
        try {
            myConn = DriverManager.getConnection(params[0]);
            myStm = myConn.createStatement();
            ResultSet usersResSet = myStm.executeQuery(getAllUserInfo);

            while (usersResSet.next()) {
                users.add(new User(usersResSet.getString("username"), usersResSet.getString("password"),
                        usersResSet.getString("salt"), usersResSet.getString("user_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return null;
    }

    // do the stuff
    @Override
    protected void onPostExecute(Connection connection) {
        super.onPostExecute(connection);

        for(User u:users) {
            if(u.getUsername().equals(username)) {
                String salt = u.getSalt();
                if(u.getPassword().equals(BCrypt.hashpw(password, salt))) {
                    Intent intent = new Intent(context, ListActivity.class);

                    // this sends username to the next intent.
                    // you probs need to send the username through, so that you know who you are dealing
                    // with.

                    intent.putExtra(u.getUserID(), "userIDReference");
                    intent.putExtra(password, "passwordReference");

                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

