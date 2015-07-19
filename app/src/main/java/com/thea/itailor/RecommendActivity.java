package com.thea.itailor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thea.itailor.utils.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecommendActivity extends ActionBarActivity {
    private static final String TAG = "RecommendActivity";
    public static final int SUCCESS = 200;
    private ActionBar actionBar;

    private GestureDetector gestureDetector;
    private static ViewFlipper viewFlipper;
    private static LayoutInflater inflater;

    private int curPosition = 1;
    private int sum = 3;

    private Handler mHandler =  new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewFlipper = (ViewFlipper) findViewById(R.id.vf_suits);
        gestureDetector = new GestureDetector(this, mGestureListener);
        inflater = getLayoutInflater();

        viewFlipper.addView(generateChild());
        viewFlipper.addView(generateChild());
        viewFlipper.addView(generateChild());
    }

    public void refresh() {
        RequestParams params = new RequestParams();
        HttpUtil.get(this, "", params, httpResponseHandler);
    }

    private JsonHttpResponseHandler httpResponseHandler = new JsonHttpResponseHandler() {
        /*@Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Message message = new Message();
            message.what = SUCCESS;
            message.obj = response;
            mHandler.sendMessage(message);
        }*/

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Message message = new Message();
            message.what = SUCCESS;
            message.obj = response;
            mHandler.sendMessage(message);
        }
    };

    public CardView generateChild() {
        CardView suit = (CardView) getLayoutInflater().inflate(R.layout.item_suit, null);

        TextView tv = (TextView) suit.findViewById(R.id.tv_suit_description);
        tv.setText(R.string.description);

        ImageView tops = (ImageView) suit.findViewById(R.id.iv_image);
//        tops.setImageBitmap();
        tops.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final ImageButton ib = (ImageButton) suit.findViewById(R.id.ib_collect);
//        ib.setImageResource(R.drawable.ic_action_favorite);
        ib.setOnClickListener(mClickListener);

        return suit;
    }

    public static CardView generateChild(String description, int score, Bitmap bitmap) {
        CardView suit = (CardView) inflater.inflate(R.layout.item_suit, viewFlipper, false);

        TextView tv = (TextView) suit.findViewById(R.id.tv_suit_description);
        tv.setText(description);

        ImageView tops = (ImageView) suit.findViewById(R.id.iv_image);
        tops.setImageBitmap(bitmap);
        tops.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final ImageButton ib = (ImageButton) suit.findViewById(R.id.ib_collect);
//        ib.setImageResource(R.drawable.ic_action_favorite);
        ib.setOnClickListener(mClickListener);

        return suit;
    }

    private static OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton imageButton = (ImageButton) v;
            if (imageButton.getTag().equals("0")) {
                Log.i(TAG, "collect");
                imageButton.setTag("1");
            } else {
                Log.i(TAG, "not collect");
                imageButton.setTag("0");
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private OnGestureListener mGestureListener = new OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > 120 && curPosition != sum) {  //下一个
                curPosition++;
                viewFlipper.setInAnimation(RecommendActivity.this, R.anim.abc_slide_in_bottom);
                viewFlipper.setOutAnimation(RecommendActivity.this, R.anim.abc_slide_out_top);
                viewFlipper.showNext();
            }
            else if (e2.getY() - e1.getY() > 120 && curPosition != 1) { //上一个
                curPosition--;
                viewFlipper.setInAnimation(RecommendActivity.this, R.anim.abc_slide_in_top);
                viewFlipper.setOutAnimation(RecommendActivity.this, R.anim.abc_slide_out_bottom);
                viewFlipper.showPrevious();
            }

            if (e1.getY() > e2.getY() && actionBar.isShowing())
                actionBar.hide();
            else if (e1.getY() < e2.getY() && !actionBar.isShowing())
                actionBar.show();
            return true;
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

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SUCCESS:
                JSONArray jsonArray = (JSONArray) msg.obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String description = jsonObject.getString("description");
                        int score = jsonObject.getInt("score");
                        viewFlipper.addView(generateChild(description, score, null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            super.handleMessage(msg);
        }
    }
}
