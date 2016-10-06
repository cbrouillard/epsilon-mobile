package com.headbangers.epsilon.v3.activity;


import android.support.v7.app.AppCompatActivity;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DecimalFormat;

@EActivity
public abstract class AbstractEpsilonActivity extends AppCompatActivity{

    protected static DecimalFormat df = new DecimalFormat("0.00");

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

    @StringRes(R.string.opened_at)
    String openedAt;
    @StringRes(R.string.operation_added)
    String operationAdded;

    @StringRes(R.string.error_loading)
    String errorLoading;
    @StringRes(R.string.error_form_amount)
    String errorFormAmount;
    @StringRes(R.string.error_form_tiers)
    String errorFormTiers;
    @StringRes(R.string.error_form_category)
    String errorFormCategory;
}
