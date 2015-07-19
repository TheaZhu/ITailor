package com.thea.itailor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.thea.itailor.db.ClothDao;
import com.thea.itailor.db.ClothSQLiteOpenHelper;
import com.thea.itailor.utils.ImageHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScanActivity extends ActionBarActivity {
    private static final String TAG = "ScanActivity";

    private String filename = "";

    private EditText newName;
    private Spinner groupSpinner;
    private ImageView clothView;

    private ClothDao dao;
    private List<String> groups = new ArrayList<>();

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filename = getIntent().getStringExtra("filename");

        dao = new ClothDao(new ClothSQLiteOpenHelper(this));

        newName = (EditText) findViewById(R.id.et_cloth_name);
        groupSpinner = (Spinner) findViewById(R.id.spn_group);
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, groups);
        groupSpinner.setAdapter(mAdapter);

        clothView = (ImageView) findViewById(R.id.iv_camera_image);
        clothView.setImageBitmap(ImageHelper.getImageFromStore(Constant.DIRECTORY_ARMOIRE, filename));
    }

    @Override
    protected void onResume() {
        super.onResume();
        groups.clear();
        groups.addAll(dao.findGroups());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            return true;
        case R.id.action_done:
            dao.add((String) groupSpinner.getSelectedItem(),
                    newName.getText().toString(), filename, null, new Date());
            Toast.makeText(this, R.string.successful_save, Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
