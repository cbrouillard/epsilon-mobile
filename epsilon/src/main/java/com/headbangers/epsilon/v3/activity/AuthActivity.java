package com.headbangers.epsilon.v3.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.RegisterAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.authentication)
@OptionsMenu(R.menu.menu_auth)
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

    @Bean
    EpsilonAccessServiceImpl accessService;

    @AfterViews
    protected void bindActionBar() {
        setSupportActionBar(toolbar);
    }

    @OptionsItem(R.id.action_auth_ok)
    void ok() {

        if (validateForm()) {
            new RegisterAsyncLoader(this.accessService, this, progressBar).execute(
                    cleanAndCompleteServerUrl (),
                    this.login.getText().toString(),
                    this.password.getText().toString());
        }
    }

    @EditorAction(R.id.password)
    void enterOnPassText(){
        ok();
    }

    private String cleanAndCompleteServerUrl(){
        String server = this.server.getText().toString();

        if (!server.startsWith("http")){
            server = "http://" + server;
        }

        if (server.endsWith("/")){
            server = server.substring(0, server.length()-1);
        }

        return server + "/mobile";
    }

    private boolean validateForm() {
        String server = this.server.getText().toString();
        String login = this.login.getText().toString();
        String password = this.password.getText().toString();

        if (server == null || server.isEmpty()) {
            this.server.setError("Le serveur sur lequel se connecter est requis.");
        }

        if (login == null || login.isEmpty()) {
            this.login.setError("Le login est requis.");
        }

        if (password == null || password.isEmpty()) {
            this.password.setError("Le mot de passe est requis.");
        }

        return server != null && !server.isEmpty()
                && login != null && !login.isEmpty()
                && password != null && !password.isEmpty();
    }

    @Override
    public void refresh(SimpleResult result) {
        if (result != null && result.getCode() != null && !result.getCode().equals("null")){
            this.epsilonPrefs.edit().server().put(cleanAndCompleteServerUrl())
                    .token().put(result.getCode()).apply();

            setResult(AUTH_RESULT);
            finish();
        } else {
            Toast.makeText(this, "Erreur login.", Toast.LENGTH_LONG).show();
        }
    }
}
