package com.thea.itailor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends ActionBarActivity {
    private EditText fbInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fbInfo = (EditText) findViewById(R.id.et_feedback);
        findViewById(R.id.btn_submit).setOnClickListener(mClickListener);
        findViewById(R.id.btn_reset).setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_submit:
                Toast.makeText(FeedbackActivity.this, "谢谢您的反馈，我们会更加努力的~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_reset:
                fbInfo.setText("");
                break;
            }
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
