package com.thea.itailor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class ShowBodyActivity extends ActionBarActivity {
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_body);

        fab = (FloatingActionButton) findViewById(R.id.fab_to_armoire);
        fab.setOnClickListener(mClickListener);

    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ShowBodyActivity.this, ArmoireActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    public void turnPage(View view) {
        if (view.getId() == R.id.ib_previous) {
            Toast.makeText(this, R.string.previous, Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.ib_next) {
            Toast.makeText(this, R.string.next, Toast.LENGTH_SHORT).show();
        }
    }

}
