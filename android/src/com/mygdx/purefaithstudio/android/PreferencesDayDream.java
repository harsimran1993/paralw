package com.mygdx.purefaithstudio.android;

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
public class PreferencesDayDream extends PreferenceActivity implements OnPreferenceChangeListener {
	private CheckBoxPreference testCheckBox;
	private ListPreference testList;

	private boolean checkBoxTest;
	private String listTest;

	@Override
	protected void onPostCreate(Bundle bundle) {
		super.onPostCreate(bundle);

		getPreferenceManager().setSharedPreferencesName("preferences"); // Don't change name here
		getPreferenceManager().setSharedPreferencesMode(0);

		addPreferencesFromResource(R.xml.preferences);

		// There is solution.
		// Like in Config.java, but without libgdx stuff, because in lwp situation, lwp is started first.
		// In daydream case this class called before  libgdx stuff is initialized. Look to the end of file!

		checkBoxTest = getPreferenceManager().getSharedPreferences().getBoolean("checkBoxTest", false);
		listTest     = getPreferenceManager().getSharedPreferences().getString("listTest", "0");

		testCheckBox = (CheckBoxPreference) findPreference("checkBoxTest");
		testCheckBox.setChecked(checkBoxTest);
		testCheckBox.setOnPreferenceChangeListener(this);

		testList = (ListPreference) findPreference("listTest");
		testList.setValueIndex(Integer.valueOf(listTest));
		testList.setSummary(testList.getEntry());
		testList.setOnPreferenceChangeListener(this);

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
		if (preference == testCheckBox) {
			checkBoxTest = (Boolean) newValue;
			testCheckBox.setChecked(checkBoxTest);
			save();
			return true;
		} else if (preference == testList) {
			listTest = newValue.toString();
			testList.setValueIndex(Integer.valueOf(listTest));
			testList.setSummary(testList.getEntry());
			save();
			return true;
		}

		return false;
	}

	/*
	 * DAYDREAM ONLY. Called in onPreferenceChange()
	 */
	private void save() {
		if (getPreferenceManager().getSharedPreferences().edit() != null) {
			getPreferenceManager().getSharedPreferences().edit();
			getPreferenceManager().getSharedPreferences().edit().putBoolean("checkBoxTest", checkBoxTest);
			getPreferenceManager().getSharedPreferences().edit().putString("listTest", listTest);
			getPreferenceManager().getSharedPreferences().edit().commit();
		}
	}
}
