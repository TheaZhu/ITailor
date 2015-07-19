package com.thea.itailor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.tencent.tauth.Tencent;

public class UserControlActivity extends ActionBarActivity {
    private Tencent mTencent;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_control);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        mTencent = Tencent.createInstance(Constant.APP_ID, getApplicationContext());
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sp.getBoolean(Constant.USER_LOGGED_IN, false)) {
                    Toast.makeText(UserControlActivity.this, R.string.already_logout, Toast.LENGTH_SHORT).show();
                    return;
                }
                mTencent.logout(getApplicationContext());
                Toast.makeText(UserControlActivity.this, R.string.successful_logout, Toast.LENGTH_SHORT).show();
                sp.edit().putBoolean(Constant.USER_LOGGED_IN, false).commit();
            }
        });
    }
}
