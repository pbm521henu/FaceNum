package com.example.faceall_lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;


/**
 * Created by guoyu24k on 16/6/14.
 */

public class FaceallHandler {

    public static final int FACE = 0;
    public static final int EYE = 1;
    public static final int MOUTH = 2;
    public static final int POSE = 3;

    static {
        System.loadLibrary("detect");
        System.loadLibrary("opencv_java");
    }

    private long handle = 0;

    public FaceallHandler(byte[] model1, byte[] model2, byte[] model3, byte[] model4, byte[] model5) {
        this.handle = FACEALL_load_model(model1, model2, model3, model4, model5);
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator +"1_1_banshenzhao.jpg", options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

            byte[] tmp = stream.toByteArray();
            EXFACE_ready_to_judge(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // FACEALL_detect_init();
        Log.i("Application", "Init done.");
    }

    public native static void FACEALL_detect_init();
    public native static int FACEALL_detect(byte[] img_data, int[] faces_rects);
    public native static void FACEALL_detect_release();
    /**
     * 人脸类型
     * 0  //landmark model
     * 1  //left eye
     * 2  //right eye
     * 3  //pose model
     */
    public native static int FACEALL_load_model(byte[] model1, byte[] model2, byte[] model3, byte[] model4, byte[] model5);

    public native static int EXFACE_ready_to_judge(byte[] img_data);// -1没有人脸或人脸离屏幕过近，0正脸检测通过，
    public native static int EXFACE_judge_action(byte[] img_data, int type);//无论检测哪个动作0就是没有动作，不通过，1代表做动作了，通过。
    public native static int EXFACE_finish_judge();

    public native static void FACEALL_destroy_model();

}
