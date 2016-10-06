package com.headbangers.epsilon.v3.activity;


import android.support.v7.app.AppCompatActivity;

import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class AbstractEpsilonActivity extends AppCompatActivity{

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    @Bean
    EpsilonAccessServiceImpl accessService;

    protected boolean isLogged (){
        String authToken = epsilonPrefs.token().get();

        return !(authToken == null || "".equals(authToken));
    }

    protected void startAuth(){
        AuthActivity_.intent(this).startForResult(AuthActivity.AUTH_RESULT);
    }

    protected String token(){
        return epsilonPrefs.token().get();
    }

}
