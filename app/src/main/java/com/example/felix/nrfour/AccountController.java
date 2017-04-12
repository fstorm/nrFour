package com.example.felix.nrfour;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  The controller of the Account tied together the view and the model. This holds most
 *  of the functionality required.
 */

public class AccountController {

    private static Account account;
    private static Account accountToAdd;
    private static AccountView accountView;
    private static String key;

    private static String[] charsToUse = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z"};

    public AccountController(Account account, AccountView view, String key) {
        this.account = account;
        this.accountView = view;
        this.key = key;
    }

    /**
     * Sets a password in the view that the user can chose to keep as their account password.
     * @param view
     */
    public void createPassword(View view) {
        boolean go = true;
        String password = "";
        while (go) {
            password = this.createPass(10);
            if(!checkPass(password)) {
                password = "";
            }
            go = !checkPass(password);
        }
        accountView.setNewPasswordField(password);
    }

    /**
     * Returns a string containting a pseudorandom combination of permitted characters.
     * @param length (length of password required)
     * @return
     */
    public static String createPass(int length) {
        String toReturn = "";
        Random rnd = new Random();
        for(int i = 0; i<length; i++){
            int toUse = rnd.nextInt(charsToUse.length);
            toReturn += charsToUse[toUse];
        }
        return toReturn;
    }

    /**
     * Checks if the inputted password passed the complexity criteria set.
     * @param pass
     * @return
     */
    public static boolean checkPass(String pass) {
        boolean toReturn = false;
        Pattern p = Pattern.compile("[A-Z]");
        Pattern q = Pattern.compile("[a-z]");
        Pattern r = Pattern.compile("[0-9]");
        Matcher match = p.matcher(pass);
        Matcher match2 = q.matcher(pass);
        Matcher match3 = r.matcher(pass);

        if (match.find() && match2.find() && match3.find()) {
            toReturn = true;
        }
        return toReturn;
    }

    /**
     * Sets the account name.
     */
    public void setAccountName(String name) {
        account.setName(name);
    }

    /**
     * Returns the account name.
     * @return name
     */
    public String getAccountName() {
        return account.getAccountName();
    }

    /**
     * Sets the accounts username.
     * @param name
     */
    public void setUsername(String name) {
        account.setUsername(name);
    }

    /**
     * Returns the accounts username.
     * @return username
     */
    public String getUsername() {
        return account.getUsername();
    }

    /**
     * Sets the accounts password.
     * @param password
     */
    public void setPassword(String password) {
        account.setPassword(password);
    }

    /**
     * Returns the accounts password
     * @return password
     */
    public String getPassword() {
        return account.getPassword();
    }

    /**
     * Sets the accounts note
     * @param note
     */
    public void setNote(String note) {
        account.getNote();
    }

    /**
     * Returns the accounts note
     * @return note
     */
    public String getNote() {
        return account.getNote();
    }

    /**
     * Called when the copy password button is pressed. Copies the current selected password onto the clipboard.
     */
    public void copyTextPressed() {
        String password = accountView.getNewPasswordField();
        ClipboardManager cpMng = (ClipboardManager) accountView.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Password", password);
        cpMng.setPrimaryClip(data);
    }

    /**
     * Called when the copy old password button is pressed. Copies the previous password onto the clipboard.
     */
    public void copyOldTextPressed() {
        String oldPassword = accountView.getCurrentPassword();
        ClipboardManager cpMng = (ClipboardManager) accountView.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Password", oldPassword);
        cpMng.setPrimaryClip(data);
    }

    /**
     * Called when save changes is pressed. Saves whatever is enterd in the username, password and note filed
     * in the Account view.
     */
    public void onSaveChangesPressed() {
        if(Util.isValidate(accountView.getAccountName()) && Util.isValidate(accountView.getUsernameField())
                && Util.isValidate(accountView.getNewPasswordField()) && Util.isValidate(accountView.getNote())) {
            accountToAdd = new Account(accountView.getAccountName(),
                    accountView.getUsernameField(),
                    accountView.getNewPasswordField(),
                    accountView.getNote());
            updateValues(accountToAdd);
            new UpdateAccountTask().execute();
        } else {
            accountView.invalidToast();
        }
    }

    /**
     * Updates the values in the Account View.
     * @param account
     */
    public void updateValues(Account account) {
        if(Util.isValidate(account.getUsername()) && Util.isValidate(account.getPassword())
                && Util.isValidate(account.getNote())) {
        accountView.setUsernameField(account.getUsername());
        accountView.setNewPasswordField(account.getPassword());
        accountView.setNote(account.getNote());
        } else {
            accountView.invalidToast();
        }
    }

    /**
     * Called when the delete account button is pressed. Deletes the current account.
     */
    public void onDeleteAccountPressed() {
        new DeleteAccountTask().execute();
    }

    /**
     * Handles the database call required when updating accounts, and any execution following.
     */
    public class UpdateAccountTask extends AsyncTask<Void, Void, Void> {
        /**
         * Handles the database call required when updating accounts.
         * @param params
         * @return null
         */
        @Override
        protected Void doInBackground(Void... params) {
            Util.updateAccounts(accountToAdd, accountView.getUsersUsername(), accountView.getCurrentPassword(), key);
            return null;
        }

        /**
         * Updates the old password view.
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            accountView.setCurrentPasswordField(accountView.getNewPasswordField());
        }
    }

    /**
     * Handles the database call required when deleting a account.
     */
    public class DeleteAccountTask extends AsyncTask<Void, Void, Void> {

        /**
         * Handles the database call required when deleting a account.
         */
        @Override
        protected Void doInBackground(Void... params) {
            Util.deleteAccount(accountView.getAccountName(), accountView.getUsersUsername());
            return null;
        }
    }

}
