package com.example.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

public class PictureUtils {

    private static final String LOG = "PictureUtils_log";

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options() ;
        options.inJustDecodeBounds = true ;
        BitmapFactory.decodeFile(path, options) ;

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1 ;
        if (srcHeight > destHeight || srcWidth > destWidth ) {
            float heightScale = srcHeight/destHeight ;
            float widthScale = srcWidth/destHeight ;
            inSampleSize = Math.round(Math.max(heightScale, widthScale));
            Log.d(LOG,
                    "heightScale = " + heightScale + ", widthScale = " + widthScale
                            + ", inSampleSize = " + inSampleSize) ;
        }
        options = new BitmapFactory.Options() ;
        options.inSampleSize = inSampleSize ;
        Bitmap newBitmap = BitmapFactory.decodeFile(path, options) ;
        float newWidth = options.outWidth ;
        float newHeight = options.outHeight ;
        Log.d(LOG, "newWidth = " + newWidth + ", newHeight = " + newHeight) ;
        Log.d(LOG, "destWidth = " + destWidth + ", destHeight = " + destHeight) ;
        return newBitmap ;
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point() ;
        activity.getWindowManager()
                .getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y) ;
    }
}
