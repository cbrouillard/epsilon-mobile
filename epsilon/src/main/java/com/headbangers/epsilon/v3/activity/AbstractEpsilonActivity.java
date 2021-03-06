package com.headbangers.epsilon.v3.activity;


import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DecimalFormat;

@EActivity
public abstract class AbstractEpsilonActivity extends AppCompatActivity{

    protected static DecimalFormat df = new DecimalFormat("0.00");

    @Pref
    protected EpsilonPrefs_ epsilonPrefs;

    @Bean
    protected EpsilonAccessServiceImpl accessService;

    protected boolean isLogged (){
        String authToken = epsilonPrefs.token().get();

        return !(authToken == null || "".equals(authToken));
    }

    protected void startAuth(){
        AuthActivity_.intent(this).startForResult(AuthActivity.AUTH_RESULT);
    }

    protected void colorizeAmount (TextView toColorize, Double valueToCheck, Double max){
        int pL = toColorize.getPaddingLeft();
        int pT = toColorize.getPaddingTop();
        int pR = toColorize.getPaddingRight();
        int pB = toColorize.getPaddingBottom();
        if (valueToCheck < max){
            toColorize.setBackgroundResource(R.drawable.span_ko);
        }else {
            toColorize.setBackgroundResource(R.drawable.span_ok);
        }
        toColorize.setPadding(pL, pT, pR, pB);
    }

    protected void setupDefaultBackNavigationOnToolbar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @StringRes(R.string.opened_at)
    protected String openedAt;
    @StringRes(R.string.operation_added)
    protected String operationAdded;

    @StringRes(R.string.error_loading)
    protected String errorLoading;
    @StringRes(R.string.error_form_amount)
    protected String errorFormAmount;
    @StringRes(R.string.error_form_tiers)
    protected String errorFormTiers;
    @StringRes(R.string.error_form_category)
    protected String errorFormCategory;
    @StringRes(R.string.error_form_name)
    protected String errorFormName;

    @OptionsItem(android.R.id.home)
    public void back() {
        this.finish();
    }
}
