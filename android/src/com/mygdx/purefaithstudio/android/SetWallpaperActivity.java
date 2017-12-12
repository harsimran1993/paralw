package com.mygdx.purefaithstudio.android;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mygdx.purefaithstudio.Config;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;

public class SetWallpaperActivity extends AppCompatActivity implements RewardedVideoAdListener,View.OnClickListener,GalleryAdapter.ItemClickListener{
    //google ads
	private RewardedVideoAd mAd;
    private InterstitialAd mInterstitialAd;
    //analytics
    //private FirebaseAnalytics mFirebaseAnalytics;
    private boolean promoDone=false;
    private Context context;
    private ColorPicker colorp;
    private TextView r, g, b,wp,loadingVid;
    private Button earn;
    private int requestCode = 1;
    private ImageButton colorCollapser;
    private LinearLayout colorBackLayout;
    private int color;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private String lastWallp="0";
    private ResideMenu resideMenu;
    private ResideMenuItem itemPromo;
    private ResideMenuItem itemRate;
    private ResideMenuItem itemShare;
    private ResideMenuItem itemContact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_main);try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger);
            getSupportActionBar().setHomeButtonEnabled(true);

        }
        catch (Exception e){}
        setUpMenu();
        context = getApplicationContext();
		colorp = (ColorPicker) findViewById(R.id.colorPicker);
		/*r =  (TextView) findViewById(R.id.red);
		g =  (TextView) findViewById(R.id.green);
		b =  (TextView) findViewById(R.id.blue);*/
		wp = (TextView) findViewById(R.id.pointText);
        loadingVid = (TextView) findViewById(R.id.Loading);
        earn = (Button) findViewById(R.id.earnbtn);
		prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = prefs.edit();
		/*// center text
		r.setGravity(Gravity.CENTER);
		g.setGravity(Gravity.CENTER);
		b.setGravity(Gravity.CENTER);
		// select color
		color = colorp.getColor();
		r.setText("R:" + Color.red(color));
		g.setText("G:" + Color.green(color));
		b.setText("B:" + Color.blue(color));*/
        /*editor.putInt("points",300);
        editor.commit();*/
		Config.points = prefs.getInt("points", 0);
        wp.setText("" + Config.points);
        //Config.useGyro = prefs.getBoolean("gyroscope",false);
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
       //image grid recycler view
        recyclerView = (RecyclerView) findViewById(R.id.parallaxGallery);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new GalleryAdapter(this,getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        /*if(!Config.useGyro){

            PackageManager packageManager = getPackageManager();
            if(packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)){
                Config.useGyro = true;
                editor.putBoolean("gyroscope",true);
                editor.commit();
            }
        }*/
        //ads
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        //loadRewardedVideoAd();

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
        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);*/

    }
    //reside menu starts
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //set background of menu
        resideMenu.setBackground(R.drawable.wallpdef);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.5f);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        // create menu items;
        itemPromo = new ResideMenuItem(this, R.drawable.promo, "Promo");
        itemRate = new ResideMenuItem(this, R.drawable.rate, "Rate It");
        itemShare = new ResideMenuItem(this, R.drawable.share, "Share");
        itemContact = new ResideMenuItem(this, R.drawable.email, "Mail us");

        itemPromo.setOnClickListener(this);
        itemRate.setOnClickListener(this);
        itemShare.setOnClickListener(this);
        itemContact.setOnClickListener(this);

        //resideMenu.addMenuItem(itemPromo, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRate, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);
    }
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger);
            //Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onClick(View view) {

        /*if (view == itemPromo){
            promo();
        }else*/ if (view == itemRate) {
            rateit();
        }else if (view == itemShare) {
            shareTextUrl();
        }else if (view == itemContact) {
            contactUs();
        }

        resideMenu.closeMenu();
    }

    //reside menu ends here
	@Override
	protected void onResume() {
		super.onResume();
		wp.setText("" + prefs.getInt("points", 0));
		//Log.i("harsim", "" + prefs.getInt("points", 0));
	}

    @Override
    protected void onPause() {
        super.onPause();
    }

	public void colorBackPick(View view) {
		color = colorp.getColor();
		//Log.i("harsim", "" + color);
		Config.backColor[0] = Color.red(color);
		Config.backColor[1] = Color.green(color);
		Config.backColor[2] = Color.blue(color);
        Config.backColorchange = true;
        colorCollapser.setBackgroundResource(R.drawable.plus);
        colorBackLayout.setVisibility(View.GONE);
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
        if (id == android.R.id.home) {
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            return true;
        }
		if (id == R.id.Promocode) {
            promo();
			return true;
		}

        if (id == R.id.Rateus) {
            rateit();
            return true;
        }

        if (id == R.id.Share) {
            shareTextUrl();
            return true;
        }
        if(id == R.id.Contactus){
            contactUs();
            return true;
        }
        /*if(id == R.id.Help){

        }*/
		return super.onOptionsItemSelected(item);

	}

	public void earn(View view){

        earn.setVisibility(View.GONE);
        loadingVid.setVisibility(View.VISIBLE);
        loadRewardedVideoAd();
		/*if (mAd.isLoaded()) {
			mAd.show();
		}
		else{
		}*/
	}

	//ads
	private void loadRewardedVideoAd() {
		mAd.loadAd("ca-app-pub-5437679392990541/4895162915", new AdRequest.Builder().addTestDevice("49C0FA06A59AFA686D150669805EA0E1").build());
	}
	@Override
	public void onRewardedVideoAdLoaded() {
        mAd.show();
        //earn.setVisibility(View.VISIBLE);
        //loadingVid.setVisibility(View.GONE);
	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {
        //loadRewardedVideoAd();
        //earn.setVisibility(View.GONE);
        //loadingVid.setVisibility(View.VISIBLE);
        earn.setVisibility(View.VISIBLE);
        loadingVid.setVisibility(View.GONE);
	}

	@Override
	public void onRewarded(RewardItem reward) {
        try {
            Config.points += reward.getAmount();
            editor.putInt("points", Config.points);
            editor.commit();
            //wp.setText(""+Config.points);
            adapter.notifyDataSetChanged();
        }
        catch(Exception e){

        }
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
        //loadRewardedVideoAd();
        earn.setVisibility(View.VISIBLE);
        loadingVid.setVisibility(View.GONE);

	}

	private ArrayList<ImageItem> getData(){
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        TypedArray names = getResources().obtainTypedArray(R.array.settings_listTestEntries);
        for (int i = 0; i < imgs.length(); i++) {
            imageItems.add(new ImageItem(imgs.getResourceId(i, -1), names.getString(i),"",1000));
        }
        return imageItems;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == requestCode) {
            //Log.i("harsim","activityresult recieved");
            // Make sure the request was successful
            /*if (resultCode == RESULT_CANCELED) {
                Log.i("harsim","cancel");
                Config.listTest = lastWallp;
                editor.putString("listTest",lastWallp);
                editor.commit();
            }
            if(resultCode == RESULT_OK){
                Log.i("harsim","Ok");
            }*/

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }
    private void promo(){
        Toast.makeText(SetWallpaperActivity.this, "The promo has ended",   Toast.LENGTH_LONG).show();
/*
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
                        editor.putInt("points", 40);
                        editor.putBoolean("promo",true);
                        editor.commit();
                        wp.setText("" + prefs.getInt("points", 0));
                        Config.points = prefs.getInt("points", 0);
                        adapter.notifyDataSetChanged();
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
        }*/
    }
    private void rateit(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Review Us!!");
        builder.setMessage("Please give us a review.\nIt helps support Development.");
        builder.setPositiveButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Uri uri = Uri.parse("market://details?id=com.mygdx.purefaithstudio.android");
                Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(openPlayStore);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SetWallpaperActivity.this, " unable to find market app",   Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }
    private void shareTextUrl() {
        try {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Live Wallpaper - 3D parallax backgrounds hd");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.mygdx.purefaithstudio.android";
            share.putExtra(Intent.EXTRA_TEXT, sAux);

            startActivity(Intent.createChooser(share, "Share link!"));
        }
        catch (Exception e){
            //e.printStackTrace();
        }
    }

    public void contactUs(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","harsimran1994@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "enter your message");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void fb(){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + "purefaithstudio"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + "purefaithstudio"));
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        choosewall(position);
    }

    public void choosewall(int position){
        Log.i("harsim","position"+position);
        if(position * 10 < (Config.points + Config.fps)){
            lastWallp = Config.listTest;
            Config.listTest = ""+position;
            editor.putString("listTest",""+position);
            editor.commit();
            try {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                if(Config.lockScreen)
                    intent.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, LWP_Android.class));
                startActivityForResult(intent,requestCode);
            } catch (Exception e) {
                try {
                    //Log.e("harsim", "moved to chooser");
                    Intent intent2 = new Intent();
                    Toast makeText = Toast.makeText(SetWallpaperActivity.this, "Choose Live wallpaper-3d parallax...\n in the list to start the Live Wallpaper.", Toast.LENGTH_SHORT);
                    intent2.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivityForResult(intent2, requestCode);
                    makeText.show();
                } catch (Exception e2) {
                    Toast.makeText(SetWallpaperActivity.this, "Please go to your system settings or long press on your homescreen to set Live Wallpaper", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SetWallpaperActivity.this);
            builder.setTitle("Insufficient Points!!");
            builder.setMessage("You have "+Config.points+" points, You need "+(position*10 - (Config.points+Config.fps -10))+" more points.\n\n" +
                    "You can get more points using earn button above.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
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
