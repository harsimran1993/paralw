package com.mygdx.purefaithstudio.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.mygdx.purefaithstudio.Config;

public class LWP_Android extends AndroidLiveWallpaperService {
	public static float pixelOffset = 0;

	@Override
	public void onCreateApplication () {
		super.onCreateApplication();

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

		final ApplicationListener listener =new WallpaperListener();
		initialize(listener, config);

	}

	public static class WallpaperListener extends com.mygdx.purefaithstudio.LWP implements AndroidWallpaperListener {
		@Override
		public void create() {
			super.resolver = new com.mygdx.purefaithstudio.Resolver() {
				@Override
				public float getxPixelOffset() {
					return pixelOffset;
				}
			};

			super.create();
		};

		/*
		 * never use xOffset/yOffset and xOffsetStep/yOffsetStep, because custom launchers will fuck your 
		 * brain and this problem can't be fixed! Use only xPixelOffset/yPixelOffset (who used yPixelOffset???)))
		 */

		@Override
		public void offsetChange (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			pixelOffset = xPixelOffset;
		}

		@Override
		public void previewStateChange (boolean isPreview) {
		}
	}
}
