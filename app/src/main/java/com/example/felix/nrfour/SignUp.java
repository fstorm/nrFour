package com.example.felix.nrfour;

import android.content.Context;
import android.os.AsyncTask;
import android.telecom.Connection;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Felix on 20/03/2017.
 */

public class SignUp extends AsyncTask<String, Void, Connection> {

    private static ArrayList<User> users;
    private static Statement myStm;
    private final static String getAllUserInfo = "SELECT user_id, username, salt, password FROM users";
    private Context context;
    private String username;
    private String password;
    private static String insertNewUser;
    private static ArrayList<String> existingUsernames;
    private final static String getUserNames = "SELECT username FROM users";
    private static String URL;

    public SignUp(Context context, String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
    }
    @Override
    protected Connection doInBackground(String... params) {
        System.out.println("Tester 1");
        // first check whether the username allready exists
        existingUsernames = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Tester 2");
        java.sql.Connection myConn = null;
        try {
            myConn = DriverManager.getConnection(params[0]);
            URL = params[0];
            myStm = myConn.createStatement();
            ResultSet usersResSet = myStm.executeQuery(getUserNames);

            while (usersResSet.next()) {
                existingUsernames.add(usersResSet.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onPostExecute(Connection connection) {
        super.onPostExecute(connection);

        System.out.println("Tester 3");
        boolean exists = false;
        for(String s: existingUsernames) {
            if(s.equals(username) || ! (s == null)) {
                Toast.makeText(context, "Username allready used", Toast.LENGTH_SHORT);
                exists = true;
            }
        }

        System.out.println("Tester 4");
        if(!exists) {
            // then check password strength

            // if all this works, create a new profile using the username and password enterd
            //          and sign in
            insertNewUser = "INSERT INTO users (user_id, password, salt, email, username) VALUES (?,?,?,?,?)";
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                java.sql.Connection newConn = DriverManager.getConnection(URL);
                System.out.println("Tester 4.1");
                PreparedStatement prepStat = newConn.prepareCall(insertNewUser);
                System.out.println("Tester 4.2");
                String salt = BCrypt.gensalt();
                System.out.println("Tester 5");
                prepStat.setString(2, BCrypt.hashpw(password, salt));
                prepStat.setString(3, salt);
                prepStat.setString(5, username);
                prepStat.execute();
                System.out.println("Tester 6");
//                myConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }


    }
}
