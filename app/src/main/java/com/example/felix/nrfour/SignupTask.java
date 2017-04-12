package com.example.felix.nrfour;

import android.os.AsyncTask;

/**
 * Inserts a new User into the users part of the database.
 */
public class SignupTask extends AsyncTask<User, Void, Void> {
    @Override
    protected Void doInBackground(User... user) {
        if (Util.unusedUsername(user[0].getUsername())) {
            Util.insertIntoUsers(user[0]);
        }

        return null;
    }
}
