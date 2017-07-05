package com.mygdx.purefaithstudio.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.mygdx.purefaithstudio.Config;

/**
 * Created by harsimran singh on 04-07-2017.
 */

public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    public static int maximum    = 50;
    public static int interval   = 1;
    private float oldValue = 40;

    public SeekBarPreference(Context context) {
        super(context);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(defaultValue);
        this.oldValue = (int)defaultValue;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = li.inflate( R.layout.seekbar_preference, parent, false);

        mSeekBar = view.findViewById(R.id.seekbar);
        mSeekBar.setMax(maximum);
        mSeekBar.setProgress((int)this.oldValue);
        mSeekBar.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        progress = Math.round(((float)progress)/interval)*interval;
        if(!fromUser){
            seekBar.setProgress((int)this.oldValue);
            return;
        }
        seekBar.setProgress(progress);
        this.oldValue = progress;
        notifyChanged();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        updatePreference(seekBar.getProgress());
        Log.i("progress",""+Config.Sensitivity);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index) {

        int dValue = (int)ta.getInt(index,50);
        return validateValue(dValue);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        int temp = restoreValue ? getPersistedInt(50) : (Integer)defaultValue;

        if(!restoreValue)
            persistInt(temp);

        this.oldValue = temp;
    }


    private int validateValue(int value){

        if(value > maximum)
            value = maximum;
        else if(value < 0)
            value = 0;
        else if(value % interval != 0)
            value = Math.round(((float)value)/interval)*interval;


        return value;
    }
   private void updatePreference(int newValue){

        Config.Sensitivity = newValue*0.1f;
        Config.save();
    }
}
