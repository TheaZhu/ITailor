package com.thea.itailor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.thea.itailor.utils.HttpUtil;
import com.thea.itailor.utils.ImageHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity {
    private static final String TAG = "LoginActivity";

    private static final int SUCCESSFUL_LOGIN = 111;

    private SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View focusView;

    private Tencent mTencent;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESSFUL_LOGIN) {
                editor.putBoolean(Constant.USER_LOGGED_IN, true).commit();
                Toast.makeText(LoginActivity.this, R.string.successful_login, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(Constant.SP_USER_INFO, Context.MODE_PRIVATE);
        editor = sp.edit();

        mTencent = Tencent.createInstance(Constant.APP_ID, getApplicationContext());

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.actv_email);
        mEmailView.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        mPasswordView = (EditText) findViewById(R.id.et_password);
        mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL)
                    attemptLogin();
                return false;
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        findViewById(R.id.btn_qq_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTencent.login(LoginActivity.this, "all", new BaseUiListener());
            }
        });
    }

    public void register() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", email);
            jsonObject.put("password", password);
            StringEntity entity = new StringEntity(jsonObject.toString());
            HttpUtil.post(this, "", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean isPasswordValid = checkPassword(password);
        boolean isEmailValid = checkEmail(email);

        if (isEmailValid && isPasswordValid) {
            if (login(email, password)) {
                editor.putString(Constant.CUR_USER_NAME, email);
                editor.putString(Constant.USER_NAME, email);
                editor.putString(Constant.PASSWORD, password);
                editor.putBoolean(Constant.USER_LOGGED_IN, true);
                editor.commit();
                Toast.makeText(this, R.string.successful_login, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else
                Toast.makeText(this, R.string.fail_login, Toast.LENGTH_SHORT).show();
        }
        else
            focusView.requestFocus();
    }

    public boolean checkEmail(String email) {
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            return false;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            return false;
        }
        return true;
    }

    public boolean checkPassword(String password) {
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            return false;
        }
        return true;
    }

    public boolean login(String email, String password) {
        /*try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", email);
            jsonObject.put("password", password);
            StringEntity entity = new StringEntity(jsonObject.toString());
            HttpUtil.post(this, "", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
//        if (sp.getString(Constant.USER_NAME, "").equalsIgnoreCase(email))
        return true;
    }

    private boolean isEmailValid(String email) {
        return email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            try {
                editor.putString(
                        Constant.QQ_OPEN_ID, ((JSONObject) o).getString("openid"));
                editor.putString(
                        Constant.QQ_EXPIRES_IN, ((JSONObject) o).getString("expires_in"));
                editor.putString(
                        Constant.QQ_ACCESS_TOKEN, ((JSONObject) o).getString("access_token"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            QQToken qqToken = mTencent.getQQToken();
            UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    final JSONObject response = (JSONObject) o;
                    if (response.has("nickname")) {
                        try {
                            editor.putString(Constant.CUR_USER_NAME, response.getString("nickname")).commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bitmap = ImageHelper.getImageFromInternet(response.getString("figureurl_qq_2"));
                                ImageHelper.saveImageToStore(bitmap, Constant.DIRECTORY_USER, Constant.CUR_PORTRAIT);
                                Message msg = new Message();
                                msg.what = SUCCESSFUL_LOGIN;
                                handler.sendMessage(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            Log.i(TAG, "onError");
            Toast.makeText(LoginActivity.this, R.string.fail_login, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "onCancel");
        }
    }
}
