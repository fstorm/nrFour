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
import android.widget.Toast;

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
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountoverview);
        Intent prevIntent = getIntent();

        key = prevIntent.getStringExtra("key");
        usersUsername = prevIntent.getStringExtra("username");
        if (prevIntent.getStringExtra("new").equals("false")) {
            account = (Account) prevIntent.getSerializableExtra("selectedAccount");
            controller = new AccountController(account, this, key);

            oldPassword = account.getPassword();

            accountNameField = (EditText) findViewById(R.id.nameView);
            accountNameField.setText(account.getAccountName());

            usernameField = (EditText) findViewById(R.id.usernameView);
            usernameField.setText(account.getUsername());

            passwordField = (EditText) findViewById(R.id.passwordView);
            passwordField.setText(account.getPassword());

            noteField = (EditText) findViewById(R.id.noteView);
            noteField.setText(account.getNote());

            oldPasswordField = (TextView) findViewById(R.id.twOldPasswordField);
            oldPasswordField.setText(oldPassword);

        } else {
            // start a default page
            account = new Account("default","","");
            controller = new AccountController(account, this, key);

            accountNameField = (EditText) findViewById(R.id.nameView);
            usernameField = (EditText) findViewById(R.id.usernameView);
            passwordField = (EditText) findViewById(R.id.passwordView);
            noteField = (EditText) findViewById(R.id.noteView);
            oldPasswordField = (TextView) findViewById(R.id.twOldPasswordField);
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
        if(Util.isValidate(name)) {
            usernameField.setText(name);
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }

    }

    public String getUsernameField() {
        return usernameField.getText().toString(); // maybe new String(blabla);
    }

    public void setNewPasswordField(String password) {
        if(Util.isValidate(password)) {
            passwordField.setText(password);
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public String getNewPasswordField() {
        System.out.println(passwordField.getText().toString());
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();
        System.out.println(password);
        return password;
    }

    public void setNote(String note) {
        if (Util.isValidate(note)) {
            noteField.setText(note);
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public String getNote() {
        return noteField.getText().toString();
    }

    public void setCurrentPasswordField(String password) {
        if(Util.isValidate(password)) {
            oldPasswordField.setText(password);
            oldPassword = password;
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public void invalidToast() {
        Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
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

    public void createPassword(View view) {
        controller.createPassword(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("username", usersUsername);
        intent.putExtra("passwordReference", key);
        startActivity(intent);
        finish();
        return;
    }

}
