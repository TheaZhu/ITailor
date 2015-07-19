package com.thea.itailor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.thea.itailor.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hunter on 2015/6/1.
 */
public class ImageHelper {

    public static File createNewFile(String directory, String name) {
        File dir1 = new File(Constant.PATH);
        if (dir1.exists() && !dir1.isDirectory())
            dir1.delete();
        if (!dir1.exists())
            dir1.mkdir();

        File dir2 = new File(Constant.PATH + directory);
        if (dir2.exists() && !dir2.isDirectory())
            dir2.delete();
        if (!dir2.exists())
            dir2.mkdir();

        File file = new File(Constant.PATH + directory + name);
        if (file.exists())
            file.delete();

        return file;
    }

    public static void saveImageToStore(Bitmap bitmap, String directory, String name) {
        File file = createNewFile(directory, name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImageFromStore(String directory, String name) {
        File file = new File(Constant.PATH + directory + name);
        if (file.exists())
            file.delete();
    }

    public static Bitmap getImageFromStore(String directory, String name) {
        String pathName = Constant.PATH + directory + name;
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        Matrix matrix = new Matrix();
        matrix.postRotate(calculateImageDegree(pathName));
        Bitmap bm = BitmapFactory.decodeFile(pathName, options);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    public static Bitmap getImageFromStore(String directory, String name, float sx, float sy) {
        String pathName = Constant.PATH + directory + name;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        matrix.postRotate(calculateImageDegree(pathName));
        Bitmap bm = BitmapFactory.decodeFile(pathName, options);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static int calculateImageDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap getImageFromInternet(String uri) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
