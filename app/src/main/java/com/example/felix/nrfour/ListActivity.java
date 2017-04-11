package com.example.felix.nrfour;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by Felix on 31/01/2017.
 */

public class ListActivity extends Activity {

    private ArrayList<Account> accountList = new ArrayList<Account>();
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent prevIntent = getIntent();
        username = prevIntent.getExtras().getString("username");
        password = prevIntent.getExtras().getString("passwordReference");

//        accountList = Util.getUserAccounts(username, password);

        new listTask().execute();
//
//        System.out.println("This is the accountList:"+accountList);
//        if(!(accountList == null)) {
//
//
//            String[] accountNames = new String[accountList.size()];
//            for (int i = 0; i < accountNames.length; i++) {
//                accountNames[i] = accountList.get(i).getAccountName();
//            }
//
//            ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_account, R.id.textView1,
//                    accountNames);
//
//            ListView theListView = (ListView) findViewById(R.id.theListView);
//            theListView.setAdapter(adapter);
//
//            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
//                    for (Account a : accountList) {
//                        if (a.getAccountName() == String.valueOf(adapterView.getItemAtPosition(i))) {
//
//                            Intent showAccount = new Intent(ListActivity.this, AccountView.class);
//                            showAccount.putExtra("new", "false");
//                            showAccount.putExtra("selectedAccount", a);
//                            showAccount.putExtra("username", username);
//                            startActivity(showAccount);
//                        }
//                    }
//                }
//            });
//        }
    }

    public void doAfter() {
        System.out.println("This is the list now"+accountList);

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
                        }
                    }
                }
            });
        }
    }

    public void createNewAccount(View view) {
        Intent intent = new Intent(this, AccountView.class);
        intent.putExtra("new", "true");
        intent.putExtra("key", password);
        intent.putExtra("username", username);
        System.out.println("ListActivity: Sending "+username);
        startActivity(intent);
    }

    private class listTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<Account> accountList2 = new ArrayList<Account>();

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("Getting account List now");
            accountList2 = Util.getUserAccounts(username, password);
            System.out.println("accoutnLst2 is now updated.");
            updateAccountList(accountList2);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            doAfter();
        }
    }

    private void updateAccountList(ArrayList<Account> newAccountList) {
        accountList = newAccountList;
    }
}