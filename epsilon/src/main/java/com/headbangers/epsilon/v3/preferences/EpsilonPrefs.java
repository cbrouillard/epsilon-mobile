package com.headbangers.epsilon.v3.preferences;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref (value=SharedPref.Scope.UNIQUE)
public interface EpsilonPrefs {

    @DefaultString("")
    String token ();

    @DefaultString("")
    String server ();
}
