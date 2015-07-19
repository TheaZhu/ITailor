package com.thea.itailor;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BodyDataActivity extends ActionBarActivity {
    private SharedPreferences sp;

    private SeekBar blSeekBar;
    private TextView bodyLengthText;

    private SeekBar chestSeekBar;
    private TextView chestText;

    private SeekBar wlSeekBar;
    private TextView waistLineText;

    private SeekBar hlSeekBar;
    private TextView hipLineText;

    private SeekBar shoulderSeekBar;
    private TextView shoulderText;

    private SeekBar alSeekBar;
    private TextView armLengthText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_data);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(Constant.SP_USER_INFO, MODE_PRIVATE);

        blSeekBar = (SeekBar) findViewById(R.id.sb_body_length);
        bodyLengthText = (TextView) findViewById(R.id.tv_body_length);
        blSeekBar.setOnSeekBarChangeListener(mChangeListener);

        chestSeekBar = (SeekBar) findViewById(R.id.sb_chest);
        chestText = (TextView) findViewById(R.id.tv_chest);
        chestSeekBar.setOnSeekBarChangeListener(mChangeListener);

        wlSeekBar = (SeekBar) findViewById(R.id.sb_waist_line);
        waistLineText = (TextView) findViewById(R.id.tv_waist_line);
        wlSeekBar.setOnSeekBarChangeListener(mChangeListener);

        hlSeekBar = (SeekBar) findViewById(R.id.sb_hip_line);
        hipLineText = (TextView) findViewById(R.id.tv_hip_line);
        hlSeekBar.setOnSeekBarChangeListener(mChangeListener);

        shoulderSeekBar = (SeekBar) findViewById(R.id.sb_shoulder);
        shoulderText = (TextView) findViewById(R.id.tv_shoulder);
        shoulderSeekBar.setOnSeekBarChangeListener(mChangeListener);

        alSeekBar = (SeekBar) findViewById(R.id.sb_arm_length);
        armLengthText = (TextView) findViewById(R.id.tv_arm_length);
        alSeekBar.setOnSeekBarChangeListener(mChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        blSeekBar.setProgress(sp.getInt(Constant.BODY_LENGTH, 30));
        chestSeekBar.setProgress(sp.getInt(Constant.CHEST, 20));
        wlSeekBar.setProgress(sp.getInt(Constant.WAIST_LINE, 15));
        hlSeekBar.setProgress(sp.getInt(Constant.HIP_LINE, 30));
        shoulderSeekBar.setProgress(sp.getInt(Constant.SHOULDER, 15));
        alSeekBar.setProgress(sp.getInt(Constant.ARM_LENGTH, 30));
    }

    private OnSeekBarChangeListener mChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
            case R.id.sb_body_length:
                bodyLengthText.setText(progress+140+"");
                sp.edit().putInt(Constant.BODY_LENGTH, progress).apply();
                break;
            case R.id.sb_chest:
                chestText.setText(progress+70+"");
                sp.edit().putInt(Constant.CHEST, progress).apply();
                break;
            case R.id.sb_waist_line:
                waistLineText.setText(progress+45+"");
                sp.edit().putInt(Constant.WAIST_LINE, progress).apply();
                break;
            case R.id.sb_hip_line:
                hipLineText.setText(progress+60+"");
                sp.edit().putInt(Constant.HIP_LINE, progress).apply();
                break;
            case R.id.sb_shoulder:
                shoulderText.setText(progress+30+"");
                sp.edit().putInt(Constant.SHOULDER, progress).apply();
                break;
            case R.id.sb_arm_length:
                armLengthText.setText(progress+40+"");
                sp.edit().putInt(Constant.ARM_LENGTH, progress).apply();
                break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
