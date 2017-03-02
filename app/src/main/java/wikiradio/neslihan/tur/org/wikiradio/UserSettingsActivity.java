package wikiradio.neslihan.tur.org.wikiradio;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by nesli on 01.03.2017.
 */

public class UserSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();



    }

    public static class PrefsFragment extends PreferenceFragment {
        CheckBoxPreference onlyCommons;
        CheckBoxPreference onlyTTS;
        CheckBoxPreference both;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);


            onlyCommons = (CheckBoxPreference) findPreference("only_commons");
            onlyTTS = (CheckBoxPreference) findPreference("only_tts");
            both = (CheckBoxPreference) findPreference("both");

            onlyCommons.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    onlyCommons.setChecked(true);
                    onlyTTS.setChecked(false);
                    both.setChecked(false);

                    return true;
                }
            });

            onlyTTS.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    onlyCommons.setChecked(false);
                    onlyTTS.setChecked(true);
                    both.setChecked(false);

                    return true;
                }
            });

            both.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    onlyCommons.setChecked(false);
                    onlyTTS.setChecked(false);
                    both.setChecked(true);

                    return true;
                }
            });
        }
    }
}
