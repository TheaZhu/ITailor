package com.thea.itailor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.thea.itailor.utils.ImageHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArmoireActivity extends ActionBarActivity
        implements PersonCenterFragment.NavigationCallbacks {
    private static final String TAG = "ArmoireActivity";

    private FragmentManager fragmentManager;
    private PersonCenterFragment perCenFragment;

    private static String pattern;

    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armoire);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_armoire, ContainerFragment.newInstance(Constant.LIST_PATTERN), "fragment")
                .commit();
        pattern = Constant.LIST_PATTERN;

        perCenFragment = (PersonCenterFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_person_center);
        perCenFragment.setUp(R.id.fragment_person_center,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    @Override
    public void onNavigationItemSelected(String tag) {
        if (tag.equalsIgnoreCase(getString(R.string.my_armoire)))
            perCenFragment.closeDrawer();
        if (tag.equalsIgnoreCase(getString(R.string.personal_recommend)))
            startActivity(new Intent(this, RecommendActivity.class));
        else if (tag.equalsIgnoreCase(getString(R.string.settings)))
            startActivity(new Intent(this, SettingsActivity.class));
        else if (tag.equalsIgnoreCase(getString(R.string.body_dataset)))
            startActivity(new Intent(this, BodyDataActivity.class));
        else if (tag.equalsIgnoreCase(getString(R.string.my_collection)))
            startActivity(new Intent(this, CollectionActivity.class));
        else if (tag.equalsIgnoreCase(getString(R.string.feedback)))
            startActivity(new Intent(this, FeedbackActivity.class));
        else if (tag.equalsIgnoreCase(getString(R.string.scan)))
            scan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CAMERA && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("filename", filename);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        perCenFragment = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_armoire, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_pattern:
            changePattern(item);
            return true;
        case R.id.action_new_group:
            new ContainerFragment.MyDialogOnClickListener(this, 0, -1).addNew();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    //改变显示模式
    public void changePattern(MenuItem item) {
        if (pattern.equalsIgnoreCase(Constant.LIST_PATTERN)) {
            item.setIcon(R.mipmap.ic_action_view_as_list_white);
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_armoire, ContainerFragment.newInstance(Constant.IMAGE_PATTERN))
                    .commit();
            pattern = Constant.IMAGE_PATTERN;
        }
        else {
            item.setIcon(R.mipmap.ic_action_view_as_grid_white);
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_armoire, ContainerFragment.newInstance(Constant.LIST_PATTERN))
                    .commit();
            pattern = Constant.LIST_PATTERN;
        }
    }

    public void scan() {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyMMdd_HHmmss");
        filename = "/C" + timeFormat.format(new Date()) + ".jpg ";
        File file = ImageHelper.createNewFile(Constant.DIRECTORY_ARMOIRE, filename);
        Uri uri = Uri.fromFile(file);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, Constant.REQUEST_CAMERA);
    }
}
