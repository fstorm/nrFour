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

/**
 * Created by Felix on 17/02/2017.
 */

public class AccountView extends Activity {

    private static Account account ;
    private static AccountController controller;
    private String oldPassword;

    private TextView accountNameField;
    private TextView usernameField;
    private TextView passwordField;
    private TextView noteField;
    private TextView oldPasswordField;

    private String usersUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountoverview);

        Intent prevIntent = getIntent();
        if (prevIntent.getStringExtra("new").equals("false")) {
            account = (Account) prevIntent.getSerializableExtra("selectedAccount");
            controller = new AccountController(account, this);

            oldPassword = account.getPassword();

            accountNameField = (TextView) findViewById(R.id.nameView);
            accountNameField.setText(account.getAccountName());

            usernameField = (EditText) findViewById(R.id.usernameView);
            usernameField.setText(account.getUsername());

            passwordField = (EditText) findViewById(R.id.passwordView);
            passwordField.setText(account.getPassword());

            noteField = (EditText) findViewById(R.id.noteView);
            noteField.setText(account.getNote());

            oldPasswordField = (TextView) findViewById(R.id.twOldPasswordField);
            oldPasswordField.setText(oldPassword);

            usersUsername = prevIntent.getStringExtra("username");
        } else {
            // start a default page
        }
    }

    public void copyText(View view) {
        controller.copyTextPressed();
//        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();
//        ClipboardManager cpMng = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData data = ClipData.newPlainText("Password", password);
//        cpMng.setPrimaryClip(data);
    }



    public void copyOldText(View view) {
        controller.copyOldTextPressed();
//        String oldPassword = ((TextView) findViewById(R.id.twOldPasswordField)).getText().toString();
//        ClipboardManager cpMng = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData data = ClipData.newPlainText("Password", oldPassword);
//        cpMng.setPrimaryClip(data);
    }

    public void saveChanges(View view) {
        controller.onSaveChangesPressed();
    }

    public void setAccountName(String accountName) {
        accountNameField.setText(accountName);
    }

    public String getAccountName() {
        return accountNameField.getText().toString();
    }

    public void setUsernameField(String name) {
        usernameField.setText(name);
    }

    public String getUsernameField() {
        return usernameField.getText().toString(); // maybe new String(blabla);
    }

    public void setNewPasswordField(String password) {
        passwordField.setText(password);
    }

    public String getNewPasswordField() {
        return passwordField.getText().toString();
    }

    public void setNote(String note) {
        noteField.setText(note);
    }

    public String getNote() {
        return noteField.getText().toString();
    }

    public void setCurrentPasswordField(String password) {
        oldPasswordField.setText(password);
        oldPassword = password;
    }

    public String getCurrentPassword() {
        return oldPasswordField.getText().toString();
    }

    public Object getSystemService() {
        return this.getSystemService();
    }

    public String getUsersUsername() {
        return usersUsername;
    }

    public void deleteAccount(View view) {
        controller.onDeleteAccountPressed();
    }
}
