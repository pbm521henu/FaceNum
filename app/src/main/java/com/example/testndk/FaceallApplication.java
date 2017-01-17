package com.example.testndk;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.example.faceall_lib.FaceallHandler;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by guoyu24k on 16/6/14.
 */
public class FaceallApplication extends Application {

    public static FaceallHandler landmarkHandler;

    public static ContentResolver mContentResolver;

    private static FaceallApplication appContext = null;

    public final String TAG = "faceall_predict";
    static byte[] landmarkSymbol;
    static byte[] leftSymbol;
    static byte[] rightSymbol;
    static byte[] poseSymbol;
    static byte[] liveSymbol;

    @Override
    public void onCreate() {
        super.onCreate();
        long startTime = System.currentTimeMillis();
        landmarkSymbol =readRawFile(getApplicationContext(), R.raw.landmark);
        leftSymbol = readRawFile(getApplicationContext(),R.raw.left);
        rightSymbol = readRawFile(getApplicationContext(),R.raw.right);
        poseSymbol = readRawFile(getApplicationContext(), R.raw.pose);
        liveSymbol = readRawFile(getApplicationContext(), R.raw.live_attack);

        Log.i(TAG, "The length of landmark model: " + landmarkSymbol.length);
        Log.i(TAG, "The length of leftSymbol model: " + leftSymbol.length);
        Log.i(TAG, "The length of rightSymbol model: " + rightSymbol.length);
        Log.i(TAG, "The length of poseSymbol model: " + poseSymbol.length);
        Log.i(TAG, "The length of liveSymbol model: " + liveSymbol.length);

        mContentResolver = getContentResolver();

        landmarkHandler = new FaceallHandler(landmarkSymbol, leftSymbol, rightSymbol, poseSymbol, liveSymbol);
//        leftHandler = new FaceallHandler(leftSymbol);
//        rightHandler = new FaceallHandler(rightSymbol);
//        poseHandler = new FaceallHandler(poseSymbol);

        long endTime = System.currentTimeMillis();
        Log.i(TAG, "Load raw file eclipsed: " + Long.toString(endTime - startTime) + " ms");
        appContext = this;
    }

    public static byte[] readRawFile(Context ctx, int resId)
    {
        int size = 0;
        byte[] result = null;

        int pos = 0;

        // while read file
        try (InputStream ins = ctx.getResources().openRawResource(resId)) {
            int rawSize = ins.available();
            Log.i("test", rawSize + "  -----");
            result = new byte[rawSize + 1024];
            while((size=ins.read(result,pos,1024))>=0){
                pos+=size;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static FaceallApplication getInstance() {
        return appContext;
    }
}
