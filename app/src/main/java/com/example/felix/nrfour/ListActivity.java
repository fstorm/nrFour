package com.example.felix.nrfour;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Activity that lists all the accounts that a user has, and allows for the creation of a new account.
 * Called from either the MainActivity or the SignUp activity.
 */

public class ListActivity extends Activity {

    private ArrayList<Account> accountList = new ArrayList<Account>();
    private String username;
    private String password;

    /**
     * Retrieves all the information send by the previous intent.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent prevIntent = getIntent();
        username = prevIntent.getExtras().getString("username");
        password = prevIntent.getExtras().getString("passwordReference");

        new ListTask().execute();
    }

    /**
     * Called after the database operations have been called.
     * Creates the list with available accounts.
     * Starts a new activity when a account name is pressed, or if the user wants to create a new account.
     * After that, it calls finish().
     */
    public void doAfter() {
        if(!(accountList == null)) {
            String[] accountNames = new String[accountList.size()];
            for (int i = 0; i < accountNames.length; i++) {
                accountNames[i] = accountList.get(i).getAccountName();
            }

            ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_account, R.id.textView1,
                    accountNames);

            ListView theListView = (ListView) findViewById(R.id.theListView);
            theListView.setAdapter(adapter);

            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                    for (Account a : accountList) {
                        if (a.getAccountName() == String.valueOf(adapterView.getItemAtPosition(i))) {
                            Intent showAccount = new Intent(ListActivity.this, AccountView.class);
                            showAccount.putExtra("new", "false");
                            showAccount.putExtra("selectedAccount", a);
                            showAccount.putExtra("username", username);
                            showAccount.putExtra("key", password);
                            startActivity(showAccount);
                            finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * Called when the user want to create a new account.
     * @param view
     */
    public void createNewAccount(View view) {
        Intent intent = new Intent(this, AccountView.class);
        intent.putExtra("new", "true");
        intent.putExtra("key", password);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    /**
     * Task in charge of handeling the database.
     */
    private class ListTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<Account> accountList2 = new ArrayList<Account>();

        /**
         * Gets the accounts a user has from the database, and then updates the global accountlist.
         * @param params
         * @return null
         */
        @Override
        protected Void doInBackground(Void... params) {
            accountList2 = Util.getUserAccounts(username, password);
            updateAccountList(accountList2);
            return null;
        }

        /**
         * Called doAfter() after the accountlist has been updated.
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            doAfter();
        }
    }

    /**
     * Updates the accountlist with a new accountlist.
     * @param newAccountList
     */
    private void updateAccountList(ArrayList<Account> newAccountList) {
        accountList = newAccountList;
    }

}