package com.thea.itailor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thea.itailor.utils.ImageHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonCenterFragment extends Fragment {
    private static final String TAG = "PersonCenterFragment";

    private Activity activity;
    private SharedPreferences sp;

    private NavigationCallbacks mCallbacks;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private RelativeLayout personCenter;
    private View perCenView;

    private boolean mUserLearnedDrawer;
    private boolean isUserLoggedIn;

    public PersonCenterFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        try {
            mCallbacks = (NavigationCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationCallbacks.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        mUserLearnedDrawer = sp.getBoolean(Constant.PREF_USER_LEARNED_DRAWER, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        personCenter = (RelativeLayout) inflater.inflate(
                R.layout.personal_center, container, false);
        personCenter.findViewById(R.id.rl_user_info).setOnClickListener(null);
        personCenter.findViewById(R.id.fab_camera).setOnClickListener(mClickListener);
        personCenter.findViewById(R.id.ll_to_armoire).setOnClickListener(mClickListener);
        personCenter.findViewById(R.id.ll_to_body_data).setOnClickListener(mClickListener);
        personCenter.findViewById(R.id.ll_to_recommend).setOnClickListener(mClickListener);
        personCenter.findViewById(R.id.ll_to_collection).setOnClickListener(mClickListener);
        personCenter.findViewById(R.id.ll_settings).setOnClickListener(mClickListener);
        personCenter.findViewById(R.id.ll_feedback).setOnClickListener(mClickListener);
        return personCenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        isUserLoggedIn = sp.getBoolean(Constant.USER_LOGGED_IN, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        CircleImageView headIcon = (CircleImageView) personCenter.findViewById(R.id.civ_head_icon);
        TextView username = (TextView) personCenter.findViewById(R.id.tv_username);
        View beforeLoginView = personCenter.findViewById(R.id.ll_before_login);
        if (isUserLoggedIn) {
            headIcon.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            beforeLoginView.setVisibility(View.INVISIBLE);
            headIcon.setImageBitmap(ImageHelper.getImageFromStore(Constant.DIRECTORY_USER, Constant.CUR_PORTRAIT));
            username.setText(sp.getString(Constant.CUR_USER_NAME, ""));
        }
        else {
            headIcon.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            beforeLoginView.setVisibility(View.VISIBLE);
            personCenter.findViewById(R.id.btn_go_login).setOnClickListener(
                    new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
            });
        }
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(perCenView);
    }

    public void openDrawer() {
        if (!isDrawerOpen())
            drawerLayout.openDrawer(perCenView);
    }

    public void closeDrawer() {
        if (isDrawerOpen())
            drawerLayout.closeDrawer(perCenView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        perCenView = activity.findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        this.drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded())
                    return;

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded())
                    return;

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    sp.edit().putBoolean(Constant.PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        if (!mUserLearnedDrawer)
            openDrawer();

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.onNavigationItemSelected((String) v.getTag());
        }
    };

    public interface NavigationCallbacks {
        void onNavigationItemSelected(String tag);
    }
}
