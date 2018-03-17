package com.phonebook.phonebook;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

/**
 * Created by nikola kosmajac on 17-Mar-18.
 */

public class MyPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
