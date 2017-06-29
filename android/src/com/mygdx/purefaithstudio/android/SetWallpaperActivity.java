package com.mygdx.purefaithstudio.android;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mygdx.purefaithstudio.Config;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SetWallpaperActivity extends AppCompatActivity implements RewardedVideoAdListener{
	private RewardedVideoAd mAd;
	Context context;
	ColorPicker colorp;
	TextView r, g, b,wp;
	int color;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_main);
		context = getApplicationContext();
		colorp = (ColorPicker) findViewById(R.id.colorPicker);
		r =  (TextView) findViewById(R.id.red);
		g =  (TextView) findViewById(R.id.green);
		b =  (TextView) findViewById(R.id.blue);
		wp = (TextView) findViewById(R.id.pointText);
		prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = prefs.edit();
		// center text
		r.setGravity(Gravity.CENTER);
		g.setGravity(Gravity.CENTER);
		b.setGravity(Gravity.CENTER);
		// select color
		color = colorp.getColor();
		r.setText("R:" + Color.red(color));
		g.setText("G:" + Color.green(color));
		b.setText("B:" + Color.blue(color));
		editor.putInt("points", 300);
		editor.commit();
		Config.points = prefs.getInt("points", 0);
		wp.setText("" + Config.points);

		mAd = MobileAds.getRewardedVideoAdInstance(this);
		mAd.setRewardedVideoAdListener(this);

		loadRewardedVideoAd();

		/*
		try {
			Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, LWP_Android.class));
			startActivity(intent);
		} catch (Exception e) {
			Log.e("harsim", "moved to chooser");
			Intent intent = new Intent();
			intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
			startActivity(intent);
		}
		*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		wp.setText("" + prefs.getInt("points", 0));
		Log.e("harsim", "" + prefs.getInt("points", 0));
	}

	public void onClick(View view) {
		try {
			Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            if(Config.lockScreen)
			    intent.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, LWP_Android.class));
			startActivity(intent);
		} catch (Exception e) {
			Log.e("harsim", "moved to chooser");
			Intent intent = new Intent();
			intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
			startActivity(intent);
		}
	}

	/*public void colorPick(View view) {
		color = colorp.getColor();
		Log.e("harsim", "" + color);
		r.setText("" + Color.red(color));
		g.setText("" + Color.green(color));
		b.setText("" + Color.blue(color));
		Config.color[0] = Color.red(color);
		Config.color[1] = Color.green(color);
		Config.color[2] = Color.blue(color);
        Config.effectColorChange =true;
		//Config.saveColor();
	}*/

	public void colorBackPick(View view) {
		color = colorp.getColor();
		Log.e("harsim", "" + color);
		r.setText("R:" + Color.red(color));
		g.setText("G:" + Color.green(color));
		b.setText("B:" + Color.blue(color));
		Config.backColor[0] = Color.red(color);
		Config.backColor[1] = Color.green(color);
		Config.backColor[2] = Color.blue(color);
        Config.backColorchange = true;
		//Config.saveColor();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_wallpaper, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void earn(View view){
		if (mAd.isLoaded()) {
			mAd.show();
		}
		else{
			Toast.makeText(this, "Loading!!! Try Again in a While!!!", Toast.LENGTH_SHORT).show();
		}
	}
	//ads id
	private void loadRewardedVideoAd() {
		mAd.loadAd("ca-app-pub-5437679392990541/4895162915", new AdRequest.Builder().addTestDevice("49C0FA06A59AFA686D150669805EA0E1").build());
	}
	@Override
	public void onRewardedVideoAdLoaded() {

	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {

	}

	@Override
	public void onRewarded(RewardItem reward) {
		Config.points += reward.getAmount();
		editor.putInt("points", Config.points);
		editor.commit();
		//wp.setText(""+Config.points);
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
		Toast.makeText(this, "Reward Ads Failed To Load!!! trying again!!!", Toast.LENGTH_SHORT).show();


	}
}
