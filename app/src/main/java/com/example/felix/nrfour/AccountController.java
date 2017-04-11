package com.example.felix.nrfour;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Felix on 05/04/2017.
 */

public class AccountController {

    private static Account account;
    private static Account accountToAdd;
    private static AccountView accountView;
    private static String key;
    private static String IV;

    private static String[] charsToUse = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z"};

    public AccountController(Account account, AccountView view, String key) {
        this.account = account;
        this.accountView = view;
        this.key = key;
        System.out.println("THis is the key:"+key);
    }


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

    public static String createPass(int length) {
        String toReturn = "";
        Random rnd = new Random();
        for(int i = 0; i<length; i++){
            int toUse = rnd.nextInt(charsToUse.length);
            toReturn += charsToUse[toUse];
        }
        return toReturn;
    }

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
     * Set and get methods for accessing account information
     */
    public void setAccountName(String name) {
        account.setName(name);
    }

    public String getAccountName() {
        return account.getAccountName();
    }

    public void setUsername(String name) {
        account.setUsername(name);
    }

    public String getUsername() {
        return account.getUsername();
    }

    public void setPassword(String password) {
        account.setPassword(password);
    }

    public String getPassword() {
        return account.getPassword();
    }

    public void setNote(String note) {
        account.getNote();
    }

    public String getNote() {
        return account.getNote();
    }

    public void copyTextPressed() {
        String password = accountView.getCurrentPassword();
        ClipboardManager cpMng = (ClipboardManager) accountView.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Password", password);
        cpMng.setPrimaryClip(data);
    }


    public void copyOldTextPressed() {
        String oldPassword = accountView.getCurrentPassword();
        ClipboardManager cpMng = (ClipboardManager) accountView.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Password", oldPassword);
        cpMng.setPrimaryClip(data);
    }

    public void onSaveChangesPressed() {
        System.out.println("onSaveChangesPressed: "+accountView.getAccountName());
        accountToAdd = new Account(accountView.getAccountName(),
                accountView.getUsernameField(),
                accountView.getNewPasswordField(),
                accountView.getNote());
        new UpdateAccountTask().execute();

    }

    public void onDeleteAccountPressed() {
        new DeleteAccountTask().execute();
    }

    public class UpdateAccountTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Util.updateAccounts(accountToAdd, accountView.getUsersUsername(), accountView.getCurrentPassword(), key);
            return null;
        }
    }

    public class DeleteAccountTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Util.deleteAccount(accountView.getAccountName(), accountView.getUsersUsername());
            return null;
        }
    }

}
