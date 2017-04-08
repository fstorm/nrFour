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


/**
 * Created by Felix on 31/01/2017.
 */

public class ListActivity extends Activity {

    private static String accountQuery;
    private static String userIDQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this gets the intent needed to open this activity
        Intent prevIntent = getIntent();
        String userId = prevIntent.getExtras().getString("userIDReference");

        accountQuery = "SELECT * FROM accounts WHERE user_id = "+"";



        // here, get all inforamtion from the database
        final Account[] accounts = {new Account("", "A", "pizza"),
                new Account("skype", "A", "qweasd879F0","this is a note!")};
        String[] accountNames = new String[accounts.length];
        for (int i = 0; i < accountNames.length; i++) {
            accountNames[i] = accounts[i].getAccountName();
        }

//        String[] toUse = {"Example 1","Example 2","Example 3","Example 4","Example 5"};

        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_account, R.id.textView1,
                accountNames);

        ListView theListView = (ListView) findViewById(R.id.theListView);
        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                for (Account a : accounts) {
                    if (a.getAccountName() == String.valueOf(adapterView.getItemAtPosition(i))) {

                        Intent showAccount = new Intent(ListActivity.this, AccountView.class);
                        showAccount.putExtra("selectedAccount", a);
                        startActivity(showAccount);
                    }
                }
            }
        });

    }

}