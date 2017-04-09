package com.example.felix.nrfour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this gets the intent needed to open this activity
        Intent prevIntent = getIntent();
        String username = prevIntent.getExtras().getString("userIDReference");
        String password = prevIntent.getExtras().getString("passwordReference");

        ArrayList<Account> accountList = Util.getUserAccounts(username, password);

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
                        startActivity(showAccount);
                    }
                }
            }
        });
    }

    public void createNewAccount(View view) {
        Intent intent = new Intent(this, AccountView.class);
        startActivity(intent);
    }
}