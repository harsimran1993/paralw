package com.mygdx.purefaithstudio.android;

import com.mygdx.purefaithstudio.Config;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Preferences extends PreferenceActivity implements OnPreferenceChangeListener {
	private CheckBoxPreference checkBoxTest,moveBox,setLock;
	private ListPreference listTest;
    private int points=0;

	@Override
	protected void onPostCreate(Bundle bundle) {
		super.onPostCreate(bundle);

		getPreferenceManager().setSharedPreferencesName("preferences"); // Don't change name here
		getPreferenceManager().setSharedPreferencesMode(0);

		addPreferencesFromResource(R.xml.preferences);

		Config.load();

		checkBoxTest = (CheckBoxPreference) findPreference("checkBoxTest");
		checkBoxTest.setChecked(Config.persistent);
		checkBoxTest.setOnPreferenceChangeListener(this);


        moveBox = (CheckBoxPreference) findPreference("moveBox");
        moveBox.setChecked(Config.moving);
        moveBox.setOnPreferenceChangeListener(this);


		setLock = (CheckBoxPreference) findPreference("setLockScreen");
		setLock.setChecked(Config.lockScreen);
		setLock.setOnPreferenceChangeListener(this);

		listTest = (ListPreference) findPreference("listTest");
		listTest.setValueIndex(Integer.valueOf(Config.listTest));
		listTest.setSummary(listTest.getEntry());
		listTest.setOnPreferenceChangeListener(this);

		Preference more = (Preference) findPreference("more");
		more.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				openUrl(R.string.url_more);

				return true;
			}
		});
	}

	private void openUrl(int i) {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) getText(i))));
		} catch (Exception e) {
			toast();
		}
	}

	private void toast() {
		Toast.makeText(this, R.string.settings_toast_unavailable, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == checkBoxTest) {
			if(Config.points >= 25) {
                Config.persistent = (Boolean) newValue;
                checkBoxTest.setChecked(Config.persistent);
                Config.save();
			}
			else{
				Toast.makeText(this,"insufficient points u need 25",Toast.LENGTH_SHORT).show();
                return false;
			}
			return true;
		}
        else if (preference == moveBox) {
            if(Config.points >= 10) {
                Config.moving = (Boolean) newValue;
                moveBox.setChecked(Config.moving);
                Config.save();
            }
            else{
                Toast.makeText(this,"insufficient points u need 10",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        else if (preference == setLock) {
            if(Config.points >= 90) {
                Config.lockScreen = (Boolean) newValue;
                setLock.setChecked(Config.lockScreen);
                Config.save();
            }
            else{
                Toast.makeText(this,"insufficient points u need 90",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        else if (preference == listTest) {
            points = Integer.parseInt(newValue.toString()) * 10;
            if( points >= (Config.points + 40)){
                Toast.makeText(this,"You need "+(points - (Config.points + 30))+" more points for this effect",Toast.LENGTH_SHORT).show();
                points=0;
                return false;
            }
            else {
                Config.listTest = newValue.toString();
                listTest.setValueIndex(Integer.valueOf(Config.listTest));
                listTest.setSummary(listTest.getEntry());
                Config.save();
            }
			return true;
		}
		return false;
	}
}
