package com.thea.itailor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.thea.itailor.utils.ImageHelper;

public class SettingsActivity extends ActionBarActivity {

    private SharedPreferences settingSP;
    private SharedPreferences userSP;

    private ImageView headIcon;

    private Switch autoSyncSwitch;
    private Switch notificationSwitch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingSP = getSharedPreferences(Constant.SP_SETTINGS, Context.MODE_PRIVATE);
        userSP = getSharedPreferences(Constant.SP_USER_INFO, Context.MODE_PRIVATE);

        headIcon = (ImageView) findViewById(R.id.civ_small_icon);
        autoSyncSwitch = (Switch) findViewById(R.id.switch_auto_sync);
        notificationSwitch = (Switch) findViewById(R.id.switch_notification);

        findViewById(R.id.rl_user_control).setOnClickListener(mClickListener);
        findViewById(R.id.tv_check_update).setOnClickListener(mClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        headIcon.setImageDrawable(null);
        if (userSP.getBoolean(Constant.USER_LOGGED_IN, false))
            headIcon.setImageBitmap(ImageHelper.getImageFromStore(
                    Constant.DIRECTORY_USER, Constant.CUR_PORTRAIT));

        autoSyncSwitch.setChecked(settingSP.getBoolean("auto_sync", false));
        notificationSwitch.setChecked(settingSP.getBoolean("display_notification", true));
        autoSyncSwitch.setOnCheckedChangeListener(mChangeListener);
        notificationSwitch.setOnCheckedChangeListener(mChangeListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        switch (v.getId()) {
        case R.id.rl_user_control:
            startActivity(new Intent(SettingsActivity.this, UserControlActivity.class));
            break;
        case R.id.tv_check_update:
            Toast.makeText(SettingsActivity.this, "当前为最新版", Toast.LENGTH_SHORT).show();
            break;
        }
        }
    };

    private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            settingSP.edit().putBoolean((String) buttonView.getTag(), isChecked).commit();
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
