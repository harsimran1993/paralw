package com.mygdx.purefaithstudio.android;

import android.content.pm.PackageManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidDaydream;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public class LWP_Android_Daydream extends AndroidDaydream {
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		PackageManager packageManager = getPackageManager();
		//SharedPreferences prefs = getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
		//Config.useGyro = prefs.getBoolean("gyroscope",false);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useWakelock = false;
		if(packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)){
			config.useGyroscope = true;
			config.useAccelerometer = false;
		}
		else{
			config.useGyroscope = false;
			config.useAccelerometer=true;
		}
		config.getTouchEventsForLiveWallpaper = false;
		config.disableAudio = true;

		final ApplicationListener listener =new LWP_Android.WallpaperListener();
		initialize(listener, config);
	}

	public static class WallpaperListener extends com.mygdx.purefaithstudio.LWP implements AndroidWallpaperListener {
		@Override
		public void offsetChange (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		}

		@Override
		public void previewStateChange (boolean isPreview) {
		}
	}
}
