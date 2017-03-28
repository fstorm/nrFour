package com.example.felix.nrfour;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Felix on 17/02/2017.
 */

public class AccountDisplay extends Activity {

    private static Account account ;
    private String oldPassword;

    private TextView nameField;
    private TextView usernameField;
    private TextView passwordField;
    private TextView noteField;
    private TextView oldPasswordField;

    private static String[] charsToUse = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountoverview);

        Intent prevIntent = getIntent();
        account = (Account) prevIntent.getSerializableExtra("selectedAccount");
        oldPassword = account.getPassword();

        nameField = (EditText) findViewById(R.id.nameView);
        nameField.setText(account.getAccountName());

        usernameField = (EditText) findViewById(R.id.usernameView);
        usernameField.setText(account.getUsername());

        passwordField = (EditText) findViewById(R.id.passwordView);
        passwordField.setText(account.getPassword());

        noteField = (EditText) findViewById(R.id.noteView);
        noteField.setText(account.getNote());

        oldPasswordField = (TextView) findViewById(R.id.twOldPasswordField);
        oldPasswordField.setText(oldPassword);

    }

    public void copyText(View view) {
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();
        ClipboardManager cpMng = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Password", password);
        cpMng.setPrimaryClip(data);
    }

    public void copyOldText(View view) {
        String oldPassword = ((TextView) findViewById(R.id.twOldPasswordField)).getText().toString();
        ClipboardManager cpMng = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Password", oldPassword);
        cpMng.setPrimaryClip(data);
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

        passwordField.setText(password);
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

    public void saveChanges(View view) {
        // re-save fields
    }
}
