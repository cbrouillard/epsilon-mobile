package com.headbangers.epsilon.v3.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.AuthAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.authentication)
@OptionsMenu(R.menu.menu_ok)
public class AuthActivity extends AppCompatActivity implements Refreshable<SimpleResult> {

    public static final int AUTH_RESULT = 400;

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.server)
    EditText server;

    @ViewById(R.id.login)
    EditText login;

    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.showPass)
    ImageButton showPass;

    @Bean
    EpsilonAccessServiceImpl accessService;

    @AfterViews
    protected void bindActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        String storedServer = this.epsilonPrefs.server().getOr(null);
        if (storedServer != null){
            this.server.setText(storedServer);
            this.login.requestFocus();
        }
    }


    @Click(R.id.showPass)
    void showOrHidePass() {
        if (this.password.getTransformationMethod() != null) {
            this.password.setTransformationMethod(null);
        } else {
            this.password.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    @OptionsItem(R.id.action_ok)
    @EditorAction(R.id.password)
    void ok() {

        if (validateForm()) {
            new AuthAsyncLoader(this.accessService, this, progressBar).execute(
                    cleanAndCompleteServerUrl(),
                    this.login.getText().toString(),
                    this.password.getText().toString());
        }
    }

    @OptionsItem(android.R.id.home)
    public void back() {
        this.finish();
    }

    private String cleanAndCompleteServerUrl() {
        String server = this.server.getText().toString();

        if (!server.startsWith("http")) {
            server = "http://" + server;
        }

        if (server.endsWith("/")) {
            server = server.substring(0, server.length() - 1);
        }

        if (!server.endsWith("/api")){
            server += "/api";
        }

        return server;
    }

    private boolean validateForm() {
        String server = this.server.getText().toString();
        String login = this.login.getText().toString();
        String password = this.password.getText().toString();

        if (server == null || server.isEmpty()) {
            this.server.setError(errorFormServer);
        }

        if (login == null || login.isEmpty()) {
            this.login.setError(errorFormLogin);
        }

        if (password == null || password.isEmpty()) {
            this.password.setError(errorFormPassword);
        }

        return server != null && !server.isEmpty()
                && login != null && !login.isEmpty()
                && password != null && !password.isEmpty();
    }

    @Override
    public void refresh(SimpleResult result) {
        if (result != null && result.getCode() != null && !result.getCode().equals("null")) {
            this.epsilonPrefs.edit().server().put(cleanAndCompleteServerUrl())
                    .token().put(result.getCode()).apply();

            setResult(AUTH_RESULT);
            finish();
        } else {
            Toast.makeText(this, errorLogin, Toast.LENGTH_LONG).show();
        }
    }

    @StringRes(R.string.error_login)
    String errorLogin;
    @StringRes(R.string.error_form_server)
    String errorFormServer;
    @StringRes(R.string.error_form_login)
    String errorFormLogin;
    @StringRes(R.string.error_form_password)
    String errorFormPassword;
}
