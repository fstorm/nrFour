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

    private static ArrayList<User> users;

    private final static String getAllUserInfo = "SELECT user_id, username, salt, password FROM users";
    private static Statement myStm;
    private final static String URL = "jdbc:mysql://localhost:3306/passwordmng?user=ryan&password=&autoReconnect=true&useSSL=false";
    private final static String URL2 =" http://10.0.2.2:8080//passwordmng?user=ryan&password=&autoReconnect=true&useSSL=false";
    private final static String getUserNames = "SELECT username FROM users";
    private static String insertNewUser;
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
        try {

            EditText et = (EditText) findViewById(R.id.usernameField);
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Connection myConn = null;
            try {
                myConn = DriverManager.getConnection(URL);
                myStm = myConn.createStatement();
                ResultSet usersResSet = myStm.executeQuery(getAllUserInfo);

                while (usersResSet.next()) {
                    users.add(new User(usersResSet.getString("username"), usersResSet.getString("password"),
                            usersResSet.getString("salt"), usersResSet.getString("user_id")));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
            if (users != null || users.isEmpty() || users.size() == 0) {
                Toast.makeText(context, "No users in DB", Toast.LENGTH_SHORT);
            }
            else{
            for (User u : users) { // if users is empty, this wount work...
                if (u.getUsername().equals(username)) {
                    String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
                    String salt = u.getSalt();
                    if (u.getPassword().equals(BCrypt.hashpw(password, salt))) {
                        Intent intent = new Intent(context, ListActivity.class);

                        // this sends username to the next intent.
                        // you probs need to send the username through, so that you know who you are dealing
                        // with.

                        intent.putExtra(u.getUserID(), "userIDReference");
                        intent.putExtra(password, "passwordReference");

//                    try {
//                        myConn.close();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }

                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
});

        thread.start();
//        EditText et = (EditText) findViewById(R.id.usernameField);
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//        Connection myConn = null;
//        try {
//            myConn = DriverManager.getConnection(URL);
//            myStm = myConn.createStatement();
//            ResultSet usersResSet = myStm.executeQuery(getAllUserInfo);
//
//            while (usersResSet.next()) {
//                users.add(new User(usersResSet.getString("username"), usersResSet.getString("password"),
//                        usersResSet.getString("salt"), usersResSet.getString("user_id")));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
//        for(User u:users) {
//            if(u.getUsername().equals(username)) {
//                String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
//                String salt = u.getSalt();
//                if(u.getPassword().equals(BCrypt.hashpw(password, salt))) {
//                    Intent intent = new Intent(this, ListActivity.class);
//
//                    // this sends username to the next intent.
//                    // you probs need to send the username through, so that you know who you are dealing
//                    // with.
//
//                    intent.putExtra(u.getUserID(), "userIDReference");
//                    intent.putExtra(password, "passwordReference");
//
////                    try {
////                        myConn.close();
////                    } catch (SQLException e) {
////                        e.printStackTrace();
////                    }
//
//                    startActivity(intent);
//                }
//                else {
//                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

    }

    public void signup(View view) {
//        new SignUp(this, ((EditText) findViewById(R.id.usernameField)).getText().toString(),
//                ((EditText) findViewById(R.id.passwordField)).getText().toString()).execute(URL);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    System.out.println("Tester 1");
        // first check whether the username allready exists
        ArrayList<String> existingUsernames = new ArrayList<String>();
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
        Connection myConn = null;
        try {
            myConn = DriverManager.getConnection(URL2);
            myStm = myConn.createStatement();
            ResultSet usersResSet = myStm.executeQuery(getUserNames);

            while (usersResSet.next()) {
                existingUsernames.add(usersResSet.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Tester 3");
        boolean exists = false;
        for(String s: existingUsernames) {
            if(s.equals(((EditText) findViewById(R.id.usernameField)).getText().toString()) || ! (s == null)) {
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
                Connection newConn = DriverManager.getConnection(URL2);
                System.out.println("Tester 4.1");
                PreparedStatement prepStat = newConn.prepareCall(insertNewUser);
                System.out.println("Tester 4.2");
                String salt = BCrypt.gensalt();
                System.out.println("Tester 5");
                prepStat.setString(2, BCrypt.hashpw(((EditText) findViewById(R.id.passwordField)).getText().toString(),
                        salt));
                prepStat.setString(3, salt);
                prepStat.setString(5, ((EditText) findViewById(R.id.usernameField)).getText().toString());
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
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
//        System.out.println("Tester 1");
//        // first check whether the username allready exists
//        ArrayList<String> existingUsernames = new ArrayList<String>();
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Tester 2");
//        Connection myConn = null;
//        try {
//            myConn = DriverManager.getConnection(URL);
//            myStm = myConn.createStatement();
//            ResultSet usersResSet = myStm.executeQuery(getUserNames);
//
//            while (usersResSet.next()) {
//                existingUsernames.add(usersResSet.getString("username"));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Tester 3");
//        boolean exists = false;
//        for(String s: existingUsernames) {
//            if(s.equals(((EditText) findViewById(R.id.usernameField)).getText().toString()) || ! (s == null)) {
//                Toast.makeText(this, "Username allready used", Toast.LENGTH_SHORT);
//                exists = true;
//            }
//        }
//
//        System.out.println("Tester 4");
//        if(!exists) {
//            // then check password strength
//
//            // if all this works, create a new profile using the username and password enterd
//            //          and sign in
//            insertNewUser = "INSERT INTO users (user_id, password, salt, email, username) VALUES (?,?,?,?,?)";
//            try {
//                Class.forName("com.mysql.jdbc.Driver").newInstance();
//                Connection newConn = DriverManager.getConnection(URL);
//                System.out.println("Tester 4.1");
//                PreparedStatement prepStat = newConn.prepareCall(insertNewUser);
//                System.out.println("Tester 4.2");
//                String salt = BCrypt.gensalt();
//                System.out.println("Tester 5");
//                prepStat.setString(2, BCrypt.hashpw(((EditText) findViewById(R.id.passwordField)).getText().toString(),
//                        salt));
//                prepStat.setString(3, salt);
//                prepStat.setString(5, ((EditText) findViewById(R.id.usernameField)).getText().toString());
//                prepStat.execute();
//                System.out.println("Tester 6");
////                myConn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//

    }
}
