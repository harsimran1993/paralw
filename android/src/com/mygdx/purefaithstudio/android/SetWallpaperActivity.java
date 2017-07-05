package com.mygdx.purefaithstudio.android;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mygdx.purefaithstudio.Config;

import java.util.ArrayList;

public class SetWallpaperActivity extends AppCompatActivity implements RewardedVideoAdListener{
    //google
	private RewardedVideoAd mAd;
    private InterstitialAd mInterstitialAd;
    //private FirebaseAnalytics mFirebaseAnalytics;
    //me
    private boolean promoDone=false;
    private Context context;
    private ColorPicker colorp;
    private TextView r, g, b,wp,loadingVid;
    Button earn;
    private ImageButton colorCollapser;
    private LinearLayout colorBackLayout;
    private int color;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private GridView gridView;
    private GridViewImageAdapter gridAdapter;

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
        loadingVid = (TextView) findViewById(R.id.Loading);
        earn = (Button) findViewById(R.id.earnbtn);
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
        /*editor.putInt("points",300);
        editor.commit();*/
		Config.points = prefs.getInt("points", 0);		wp.setText("" + Config.points);

        //colorPicker Collapse
        colorCollapser = (ImageButton) findViewById(R.id.colorCollapser);
        colorBackLayout = (LinearLayout) findViewById(R.id.colorBackLayout);
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

        //Image grid
        gridView = (GridView) findViewById(R.id.imageGrid);
        gridAdapter = new GridViewImageAdapter(SetWallpaperActivity.this,R.layout.lwitem,getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position * 10 < (Config.points + 40)){
                    Config.listTest = ""+position;
                    editor.putString("listTest",""+position);
                    editor.commit();
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
            }
        });

        //ads
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

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

        //analytics
        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("Activity", "launcher");
        params.putString("WP", ""+Config.points);
        mFirebaseAnalytics.logEvent("share_image", params);*/

    }

	@Override
	protected void onResume() {
		super.onResume();
		wp.setText("" + prefs.getInt("points", 0));
		Log.e("harsim", "" + prefs.getInt("points", 0));
	}

    @Override
    protected void onPause() {
        super.onPause();
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
                            editor.putInt("points", 80);
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
		}
	}

	//ads
	private void loadRewardedVideoAd() {
		mAd.loadAd("ca-app-pub-5437679392990541/4895162915", new AdRequest.Builder().addTestDevice("49C0FA06A59AFA686D150669805EA0E1").build());
	}
	@Override
	public void onRewardedVideoAdLoaded() {
        earn.setVisibility(View.VISIBLE);
        loadingVid.setVisibility(View.GONE);
	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        earn.setVisibility(View.GONE);
        loadingVid.setVisibility(View.VISIBLE);
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
        loadRewardedVideoAd();

	}

	private ArrayList<ImageItem> getData(){
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        TypedArray names = getResources().obtainTypedArray(R.array.image_names);
        for (int i = 0; i < imgs.length(); i++) {
            imageItems.add(new ImageItem(imgs.getResourceId(i, -1), names.getString(i)));
        }
        return imageItems;
    }

	/*private class ImageData{
        public String imageURL,imageName;
        public ImageData(String imageURL,String imageName){
            this.imageName = imageName;
            this.imageURL = imageURL;
        }
    }
	public class DownloadImage extends AsyncTask<ArrayList<ImageData>, Integer, Long>{

        @Override
        protected Long doInBackground(ArrayList<ImageData>... imgData) {
            int count = imgData.length;
            long totalSize = 0;
            int i=0;
            for (ImageData imd:imgData[0]) {
                i++;
                try {
                    downloadImageURL(imd.imageURL,context,imd.imageName);
                publishProgress(i);
                // Escape early if cancel() is called
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) break;
            }
            return totalSize;
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            showDialog(0);
        }

    }

    public Bitmap loadLocalImage(String fileName, Context context) throws FileNotFoundException {
        File imageFile = new File(getSaveFolder(context),fileName);
        InputStream inputStream = new FileInputStream(imageFile);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 1;
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap wallpaperBitmap = BitmapFactory.decodeStream(inputStream,null,bitmapOptions);
        return wallpaperBitmap;
    }

    public void downloadImageURL(String imageUrl,Context context,String fileName) throws IOException {
        URL wallpaperURL = new URL(imageUrl);
        URLConnection con =  wallpaperURL.openConnection();
        InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(),10240);
        File localDir = getSaveFolder(context);
        File imageFile = new File(localDir,fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);

        byte buffer[] = new byte[1024];
        int datasize;
        int loadsize = 0;
        while((datasize = inputStream.read(buffer))!= -1){
            loadsize += datasize;
            fileOutputStream.write(buffer,0,datasize);
        }
        fileOutputStream.close();


    }

    public File getSaveFolder(Context context){
        File dataDir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = new File(Environment.getExternalStorageDirectory(),"LWPData");
            if(!dataDir.isDirectory())
                dataDir.mkdir();
        }
        if(!dataDir.isDirectory())
            dataDir = context.getFilesDir();

        return dataDir;
    }*/

}
