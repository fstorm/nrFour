package com.example.felix.nrfour;
import android.database.CursorJoiner;
import android.text.Editable;

import java.sql.*;

/**
 * Created by Felix on 03/04/2017.
 */
public class Util {

    private final static String URL = "jdbc:mysql://sql8.freesqldatabase.com:3306/" +
            "sql8166895?user=sql8166895&password=LQ8WkkssPb&autoReconnect=true&useSSL=false";
    private static Connection conn;


    public static void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(URL);
            boolean reached = conn.isValid(10);
            System.out.println(reached);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void dropTable(String tableName) {
        Statement stm = null;
        try {
            stm = conn.createStatement();
            stm.executeUpdate("DROP TABLE "+tableName);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Called when creating a new account
     * @param newAccount
     * @param userid
     */
    public static boolean insertIntoAccounts(Account newAccount, String userid) {
        try {
            Statement getCurrentAccounts = conn.createStatement();
            String statmentToCall = "SELECT user_id AND account_name FROM accounts";
            ResultSet resultSet = getCurrentAccounts.executeQuery(statmentToCall);
            if(resultSet.getString("user_id").equals(userid)
                    && resultSet.getString("account_name").equals(newAccount.getAccountName())) {
                return false; // this set all ready exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement prepStm = null;
        String statment = "INSERT INTO accounts (user_id, username, password, note, account_name)" +
                " VALUES (?,?,?,?,?)";
        try {
            prepStm = conn.prepareCall(statment);
            prepStm.setString(1, userid); //userid
            prepStm.setString(2, newAccount.getUsername()); //username
            prepStm.setString(3,newAccount.getPassword()); //password
            prepStm.setString(4,newAccount.getNote()); //note
            prepStm.setString(5, newAccount.getAccountName()); //accountname
            prepStm.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true; // everything is fine
    }

    /**
     * To be called when a account is saved. This can only be pressed after the account all ready exists.
     * @param account
     * @param userid
     */
    public static void updateAccounts(Account account, String userid) {
        Statement stm = null;
        String statment = "UPDATE accounts SET username = "+account.getUsername()+
                ", password = "+account.getPassword()+
                ", note = "+account.getNote()+
                "WHERE user_id = "+userid+" AND account_name = "+account.getAccountName();
        try {
            stm = conn.createStatement();
            stm.executeUpdate(statment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoUsers(User newUser) {



        PreparedStatement prepStm = null;
        String statment = "INSERT INTO users (password, salt, iv, username)" +
                " VALUES ('somepassword', 'somesalt', 'someiv', 'someusername')";
        try {
//            prepStm = conn.prepareCall(statment);
//            prepStm.setString(2,"password");
//            prepStm.setString(3, "somesalt");
//            prepStm.setString(4, "someIV");
//            prepStm.setString(5, "someusername");
//            prepStm.execute();
            Statement stm = conn.createStatement();
            stm.executeUpdate(statment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // rejects " ' | ;
    public static boolean isValidate(String input) {
        boolean toReturn = true;
        for (int i = 0; i<input.length(); i++) {
            switch(input.charAt(i)) {
                case '\"' : toReturn = false;
                    break;
                case '\'': toReturn = false;
                    break;
                case '|': toReturn = false;
                    break;
                case ';': toReturn = false;
                    break;
                default: toReturn = true;
            }
        }
        return toReturn;
    }

    public static boolean deleteAccount(Account account, String userid) {
        String deleteStatment = "DELETE FROM accounts WHERE user_id = "+userid+
                " AND account_name = "+account.getAccountName();
        Statement stm = null;
        try {
            stm = conn.createStatement();
            stm.executeUpdate(deleteStatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean validUsername(String username) {
        String getUsernames = "SELECT username FROM users";
        try {
            Statement stm = conn.createStatement();
            ResultSet results = stm.executeQuery(getUsernames);
            while(results.next()) {
                if(results.equals(null)) {return true;}
                if (results.getString("username").equals(username)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
