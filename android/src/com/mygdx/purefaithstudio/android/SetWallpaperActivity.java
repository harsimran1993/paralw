package com.mygdx.purefaithstudio.android;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mygdx.purefaithstudio.Config;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetWallpaperActivity extends AppCompatActivity implements RewardedVideoAdListener{
    //ads
	private RewardedVideoAd mAd;
    private InterstitialAd mInterstitialAd;
    private boolean promoDone=false;
    Context context;
	ColorPicker colorp;
	TextView r, g, b,wp;
	ImageButton colorCollapser;
    LinearLayout colorBackLayout;
	int color;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_main);
		context = getApplicationContext();
		colorp = (ColorPicker) findViewById(R.id.colorPicker);
        colorCollapser = (ImageButton) findViewById(R.id.colorCollapser);
        colorBackLayout = (LinearLayout) findViewById(R.id.colorBackLayout);
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
		Config.points = prefs.getInt("points", 0);
		wp.setText("" + Config.points);

        //ads
		mAd = MobileAds.getRewardedVideoAdInstance(this);
		mAd.setRewardedVideoAdListener(this);
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



        loadRewardedVideoAd();

		colorCollapser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorBackLayout.getVisibility() == View.VISIBLE) {
                    view.setBackgroundResource(R.drawable.plus);
                    colorBackLayout.setVisibility(View.GONE);
                } else {
                    view.setBackgroundResource(R.drawable.minus);
                    colorBackLayout.setVisibility(View.VISIBLE);
                }
            }
        });
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
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
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
		if (id == R.id.Promocode) {
            promoDone = prefs.getBoolean("promo",false);
            if(!promoDone) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter PromoCode:");
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if (m_Text.equals(Config.promo)) {
                            editor.putInt("points", 50);
                            editor.putBoolean("promo",true);
                            editor.commit();
                            wp.setText("" + prefs.getInt("points", 0));
                            Config.points = prefs.getInt("points", 0);
                        }
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Already Recieved!!");
                builder.setMessage("You have already claimed the promo rewards");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
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
