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
 * The view associated with the account. This view is in charge of creating the controller when created.
 * Allows the controller to communicate with the UI, and provides get and set methods in order to interact
 * with UI data.
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

    /**
     * Either creates a default account, without information being filled in, or an account view populated with
     * the users saved data.
     * @param savedInstanceState
     */
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

    /**
     * Called when the user presses the copy password button.
     * @param view
     */
    public void copyText(View view) {
        controller.copyTextPressed();
    }

    /**
     * Called when the user pressesd the copy old password button.
     * @param view
     */
    public void copyOldText(View view) {
        controller.copyOldTextPressed();
    }

    /**
     * Called when the user presses the save changes button.
     * @param view
     */
    public void saveChanges(View view) {
        controller.onSaveChangesPressed();
    }

    /**
     * Sets the account name of an account.
     * @param accountName
     */
    public void setAccountName(String accountName) {
        accountNameField.setText(accountName);
    }

    /**
     * Returns the account name.
     * @return accountName
     */
    public String getAccountName() {
        return accountNameField.getText().toString();
    }

    /**
     * Sets the value of the username Field.
     * @param name
     */
    public void setUsernameField(String name) {
        if(Util.isValidate(name)) {
            usernameField.setText(name);
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Gets the value of the username field
     * @return username
     */
    public String getUsernameField() {
        return usernameField.getText().toString();
    }

    /**
     * Sets the value of the new password field.
     * @param password
     */
    public void setNewPasswordField(String password) {
        if(Util.isValidate(password)) {
            passwordField.setText(password);
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the value of the new Password field.
     * @return password
     */
    public String getNewPasswordField() {
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();
        return password;
    }

    /**
     * Sets the value of the note.
     * @param note
     */
    public void setNote(String note) {
        if (Util.isValidate(note)) {
            noteField.setText(note);
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the note.
     * @return
     */
    public String getNote() {
        return noteField.getText().toString();
    }

    /**
     * Sets the value of the current password field
     * @param password
     */
    public void setCurrentPasswordField(String password) {
        if(Util.isValidate(password)) {
            oldPasswordField.setText(password);
            oldPassword = password;
        } else {
            Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called in the event that input fails the validation step.
     */
    public void invalidToast() {
        Toast.makeText(this, "This contains invalid input", Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the current password from the old password field.
     * @return
     */
    public String getCurrentPassword() {
        return oldPasswordField.getText().toString();
    }

    /**
     * Returns the system service
     * @return Object
     */
    public Object getSystemService() {
        return this.getSystemService();
    }

    /**
     * Returns the Users username
     * @return
     */
    public String getUsersUsername() {
        return usersUsername;
    }

    /**
     * Called when the delete account button is pressed.
     * @param view
     */
    public void deleteAccount(View view) {
        controller.onDeleteAccountPressed();
    }

    public void createPassword(View view) {
        controller.createPassword(view);
    }

    /**
     * Called when the back button is pressed. Ensures that the ListActivity maintains updated.
     */
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
