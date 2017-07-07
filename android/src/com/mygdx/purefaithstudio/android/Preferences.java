package com.mygdx.purefaithstudio.android;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mygdx.purefaithstudio.Config;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Preferences extends PreferenceActivity implements OnPreferenceChangeListener {
	private CheckBoxPreference checkBoxTest,moveBox,setLock;
	private ListPreference listTest;
    private int points=0;
	private InterstitialAd mInterstitialAd;
    private SeekBarPreference seekBar;

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
		/*PreferenceCategory mCategory =(PreferenceCategory) findPreference("settings_general");
		mCategory.removePreference(checkBoxTest);*/

        moveBox = (CheckBoxPreference) findPreference("moveBox");
        moveBox.setChecked(Config.moving);
        moveBox.setOnPreferenceChangeListener(this);


		setLock = (CheckBoxPreference) findPreference("setLockScreen");
		setLock.setChecked(Config.lockScreen);
		setLock.setOnPreferenceChangeListener(this);

		listTest = (ListPreference) findPreference("listTest");
		listTest.setValueIndex(Integer.parseInt(Config.listTest));
		listTest.setSummary(listTest.getEntry());
		listTest.setOnPreferenceChangeListener(this);

        seekBar = (SeekBarPreference) findPreference("Sensitivity");
        seekBar.setDefaultValue((int)(Config.Sensitivity * 10));

		Preference more = (Preference) findPreference("more");
		more.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				openUrl(R.string.url_more);

				return true;
			}
		});

		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-5437679392990541/1888200515");

		mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("49C0FA06A59AFA686D150669805EA0E1").build());

		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				// Load the next interstitial.
				mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("49C0FA06A59AFA686D150669805EA0E1").build());
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
            if( points >= (Config.points + Config.fps)){
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setTitle("Insufficient Points!!");
				builder.setMessage("You have "+Config.points+" points, You need "+(points - (Config.points + Config.fps - 10))+" points.\n\n" +
						"You can get more points using earn button above.");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						dialog.cancel();
					}
				});
				builder.show();
				points=0;
                return false;
            }
            else {
                Config.listTest = newValue.toString();
                listTest.setValueIndex(Integer.valueOf(Config.listTest));
                listTest.setSummary(listTest.getEntry());
                Config.save();
            }
            mInterstitialAd.show();
			return true;
		}
		return false;
	}
}
