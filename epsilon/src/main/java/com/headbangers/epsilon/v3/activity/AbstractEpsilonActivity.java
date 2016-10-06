package com.headbangers.epsilon.v3.activity;


import android.support.v7.app.AppCompatActivity;

import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class AbstractEpsilonActivity extends AppCompatActivity{

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    protected boolean isLogged (){
        String authToken = epsilonPrefs.token().get();

        return !(authToken == null || "".equals(authToken));
    }

    abstract void init ();

    @AfterInject
    void initData() {
        if (!isLogged()) {
            // goto AuthActivity
            startAuth();
        } else {
            // charge des comptes
            init();
        }
    }

    protected void startAuth(){
        AuthActivity_.intent(this).startForResult(AuthActivity.AUTH_RESULT);
    }

}
