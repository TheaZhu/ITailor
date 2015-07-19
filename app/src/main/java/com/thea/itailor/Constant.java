package com.thea.itailor;

import android.os.Environment;

/**
 * Created by hunter on 2015/6/19.
 */
public class Constant {
    public static final String PATH = Environment.getExternalStorageDirectory() + "/iTailor";
    public static final String DIRECTORY_ARMOIRE = "/armoire";
    public static final String DIRECTORY_USER = "/user";

    public static final String CUR_PORTRAIT = "/cur_portrait.jpg";
    public static final String USER_PORTRAIT = "/portrait.jpg";

    public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    public static final String USER_LOGGED_IN = "user_logged_in";

    public static final String CUR_USER_NAME = "cur_user_name";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";

    public static final String APP_ID = "222222";

    public static final String LIST_PATTERN = "list_pattern";
    public static final String IMAGE_PATTERN = "image_pattern";

    public static final String QQ_OPEN_ID = "qq_open_id";
    public static final String QQ_ACCESS_TOKEN = "qq_access_token";
    public static final String QQ_EXPIRES_IN = "qq_expires_in";

    public static final String SP_USER_INFO = "userinfo";
    public static final String SP_SETTINGS = "settings";

    public static final String BODY_LENGTH = "body_length";
    public static final String CHEST = "chest";
    public static final String WAIST_LINE = "waist_line";
    public static final String HIP_LINE = "hip_line";
    public static final String SHOULDER = "shoulder";
    public static final String ARM_LENGTH = "arm_length";

    public static final int REQUEST_CAMERA = 45;

    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_SHORT = 2;
}
