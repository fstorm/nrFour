package com.example.felix.nrfour;

import android.os.AsyncTask;

/**
 * Created by Felix on 11/04/2017.
 */

public class SignupTask extends AsyncTask<User, Void, Void> {
    @Override
    protected Void doInBackground(User... user) {
        System.out.println("Running in backgroud");
        if (Util.unusedUsername(user[0].getUsername())) {
            Util.insertIntoUsers(user[0]);
        }

        return null;
    }
}
