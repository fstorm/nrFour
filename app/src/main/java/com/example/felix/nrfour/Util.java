package com.example.felix.nrfour;
import android.database.CursorJoiner;
import android.provider.Settings;
import android.text.Editable;

import java.security.spec.ECField;
import java.sql.*;
import java.util.ArrayList;

/**
 * The Util class handles all database connections that the application uses, as well
 * as validation of user input.
 */
public class Util {

    private final static String URL = "jdbc:mysql://188.166.152.51:3306/passwordmanager?autoReconnect=true&useSSL=false";
    private static Connection conn;

    private static final String username = "root";
    private static final String password = "isitsecret";

    /**
     * Checks the connection to the database.
     * @throws Exception
     */
    public static void checkConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection(URL);
        boolean reached = conn.isValid(10);
    }


    /**
     * Connects the user to the database.
     */
    public static void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(URL, username, password);
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

    /**
     * Closes the connection to the database
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drops a table from the database.
     * @param tableName
     */
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
            Util.connect();
            Statement getCurrentAccounts = conn.createStatement();
            String statmentToCall = "SELECT user_id AND account_name FROM accounts";
            ResultSet resultSet = getCurrentAccounts.executeQuery(statmentToCall);
            while(resultSet.next()) {
                if (resultSet.getString("user_id").equals(userid)
                        && resultSet.getString("account_name").equals(newAccount.getAccountName())) {
                    return false; // this set all ready exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement prepStm = null;
        String statment = "INSERT INTO accounts (account_name, user_id, username, password, note)" +
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
     * @param username
     */
    public static void updateAccounts(Account account, String username, String oldPassword, String key) {
        Statement stm = null;
        String userID = getUserID(username);
        String IV = Util.getUserIVFromID(userID);
        String statment = "";
        if (oldPassword.equals("---")) {
            statment = "INSERT INTO accounts (account_name, user_id, username, password, note) " +
                    "VALUES ('"+account.getAccountName()+"', '"+userID+"','"
                    +Encrypter.encrypter(key, account.getUsername(), IV)
                    +"', '"+Encrypter.encrypter(key, account.getPassword(), IV)+"', '"+
                    Encrypter.encrypter(key, account.getNote(), IV)+"')";
        } else {
            statment = "UPDATE accounts SET username = '" + Encrypter.encrypter(key, account.getUsername(), IV) +
                    "', password = '" + Encrypter.encrypter(key, account.getPassword(), IV) +
                    "', note = '" + Encrypter.encrypter(key, account.getNote(), IV) +
                    "' WHERE user_id =" + userID + " AND account_name = '" + account.getAccountName() + "'";
        }
        try {
            Util.connect();
            stm = conn.createStatement();
            stm.executeUpdate(statment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the users IV based on the user ID.
     * @param userid
     * @return IV
     */
    private static String getUserIVFromID(String userid) {
        String statment = "SELECT iv FROM users WHERE user_id="+userid;
        String IV = "";
        try {
            Util.connect();
            Statement stm = conn.createStatement();
            ResultSet rstSet = stm.executeQuery(statment);
            if (rstSet.next()) {
                IV = rstSet.getString("iv");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return IV;
    }

    /**
     * Inserts a user into the database.
     * @param newUser
     */
    public static void insertIntoUsers(User newUser) {

        PreparedStatement prepStm = null;
        String statment = "INSERT INTO users (password, salt, iv, username)" +
                " VALUES ('"+newUser.getPassword()+"', '"+newUser.getSalt()+"', '"+newUser.getIV()+
                "', '"+newUser.getUsername()+"')";
        try {
            Util.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate(statment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // rejects " ' | ;
    /**
     * Returns true if the input is valid, and false if not.
     * If the input is null or empty, it returns true, as this is permitted.
     * @param input
     * @return boolean true
     */
    public static boolean isValidate(String input) {
        boolean toReturn = true;
        if(input == null) {return true;}
        if (input.equals("")) {return true;}
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

    /**
     * Deletes an account from the accounts database based on the users username and accountname
     * @param accountName
     * @param username
     * @return boolean
     */
    public static boolean deleteAccount(String accountName, String username) {
        String userID = getUserID(username);
        String deleteStatment = "DELETE FROM accounts WHERE user_id = "+userID+
                " AND account_name = '"+accountName+"'";
        Statement stm = null;
        try {
            Util.connect();
            stm = conn.createStatement();
            stm.executeUpdate(deleteStatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks whether a username is currently in use or not.
     * Returns true of it is unused.
     * @param username
     * @return boolean
     */
    public static boolean unusedUsername(String username) {

        String getUsernames = "SELECT username FROM users";
        try {
            Util.connect();
            Statement stm = conn.createStatement();
            ResultSet results = stm.executeQuery(getUsernames);
            while(results.next()) {
                if (results.getString("username").equals(username)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Returns a user from the database.
     * @param username
     * @return user
     */
    public static User getUser(String username) {
        String getUser = "SELECT * FROM users WHERE username = '"+username+"'";
        Statement stm = null;
        User toReturn = null;
        try {
            Util.connect();
            stm = conn.createStatement();
            ResultSet resultSet = stm.executeQuery(getUser);
            if (resultSet.next()) {
                toReturn = new User(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("salt"), resultSet.getInt("user_id") + "", resultSet.getString("iv"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * Returns a user from the database
     * @param userID
     * @return user
     */
    public static User getUserFromID(String userID){
        String getUser = "SELECT * FROM users WHERE username = "+userID;
        Statement stm = null;
        User toReturn = null;
        try {
            Util.connect();
            stm = conn.createStatement();
            ResultSet resultSet = stm.executeQuery(getUser);
            if (resultSet.next()) {
                toReturn = new User(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("salt"), resultSet.getInt("user_id") + "", resultSet.getString("iv"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }


    /**
     * Returns a userID
     * @param username
     * @return userID
     */
    public static String getUserID (String username) {

        String statment = "SELECT user_id FROM users WHERE username = '"+username+"'";
        String userID = null;
        try {
            Util.connect();
            Statement stm = conn.createStatement();
            ResultSet resultSet = stm.executeQuery(statment);
            if (resultSet.next()) {
                userID = resultSet.getString("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    /**
     * Returns all accounts associated with a user in an ArrayList<Account>
     * @param username
     * @param key
     * @return ArrayList<Account> listToReturn
     */
    public static ArrayList<Account> getUserAccounts(String username, String key) {
        ArrayList<Account> listToReturn = new ArrayList<Account>();
        String userID = getUserID(username);
        String statement = "SELECT * FROM accounts WHERE user_id="+userID;
        String IV = Util.getUserIVFromID(userID);
        Account account = null;
        try {
            Util.connect();
            Statement stm = conn.createStatement();
            ResultSet resultSet = stm.executeQuery(statement);
            while (resultSet.next()) {
                if (Encrypter.decrypter(key, resultSet.getString("note"), IV).equals("")) {
                    account = new Account(resultSet.getString("account_name"),
                            Encrypter.decrypter(key, resultSet.getString("username"), IV),
                            Encrypter.decrypter(key, resultSet.getString("password"), IV)
                            );
                } else {
                    account = new Account(resultSet.getString("account_name"),
                            Encrypter.decrypter(key, resultSet.getString("username"), IV),
                            Encrypter.decrypter(key, resultSet.getString("password"), IV)
                            , Encrypter.decrypter(key, resultSet.getString("note"), IV)
                    );

                }
                listToReturn.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listToReturn;
    }
}
