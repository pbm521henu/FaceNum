package com.example.testndk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faceall_lib.FaceallHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import utils.FileService;
import utils.RoundProgressBar;

public class FaceActivity extends Activity implements
        SurfaceHolder.Callback ,Camera.PreviewCallback{
    private static final String TAG = "FaceActivity";
    private byte[] image;
    private Camera camera;
    private int i = 0; //帧数的下标，每次检测获取5帧，所以最大为4。
    private int j = 0; // 错误的次数，一次检测如果连续错误五次，就跳出报错
    private int width, height;
    private boolean isPre = false;
    private byte use[][] = new byte[5][];
    private  SurfaceView preview;
    private  SurfaceHolder holder;
    MediaPlayer player;
    private boolean wait = true;
    private RoundProgressBar mRoundProgressBar3;
//    private int sign = 0; //sign = 1正在检测 =0 检测完成
    private ImageView iv = null;
    private boolean muc = false;
    private static TextView text;
    private Thread initThread;
    private int step = 0;  //检测步数，0初始化，1正脸，2随意一个动作，3结束
    private boolean suc = false;
    private boolean end = false;
    private static boolean exit;
    private AnimationDrawable localAnimationDrawable = null;
    private FaceallApplication application;
    private int ready_judge;
    private FaceallHandler faceallHandler;
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1: {
                    Log.e(TAG, "flag = " + msg.arg1);
                    text.setText("请您缓慢闭眼、张嘴、点头");
                    player.stop();

                    player = MediaPlayer.create(FaceActivity.this, R.raw.face_eye_mouse);
                    player.start();
                    iv.setBackgroundResource(R.drawable.anim);
                    localAnimationDrawable = (AnimationDrawable) iv.getBackground();
                    localAnimationDrawable.start();
                }
                break;
                case 2: {
                    Log.e(TAG, "flag = " + msg.arg1);
                    text.setText("检测成功");
                    player.stop();
                    suc =true;
                    end = true;
                    Intent intent = new Intent();

                    setResult(1001, intent);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    finish();

                }break;
                case 3: {
                    text.setText("检测失败");
                    player.stop();
                    new AlertDialog.Builder(FaceActivity.this)
                            .setTitle("活体检测失败，请重试")
                            .setMessage("可能失败的原因：\n" +
                                    "1.光线太暗或太亮,\n" +
                                    "2.非常规操作")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                }
                break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        exit = false;
        application = FaceallApplication.getInstance();
        faceallHandler = application.landmarkHandler;
        setContentView(R.layout.activity_face);

        text = (TextView)findViewById(R.id.text10);
        iv = (ImageView) this.findViewById(R.id.img10);
        player = MediaPlayer.create(FaceActivity.this, R.raw.face_in_frome);
        player.start();
        mRoundProgressBar3 = (RoundProgressBar) findViewById(R.id.roundProgressBar3);
        initView();

        try {
            preview();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        initThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (i < 5 && !exit) {
                    if(wait){
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        wait = false;
                    }

                    camera.setOneShotPreviewCallback(FaceActivity.this);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //takePicture();

                    if (i == 5) {
                        image = use[2];
                        Message message = null;
                        switch (step){
                            case 0: {//正脸
                                    ready_judge = faceallHandler.EXFACE_ready_to_judge(image);
                                    if (ready_judge == 0) {//0正脸检测通过
                                        step++;
                                        Log.e(TAG, "正脸检测通过ready_judge***" + ready_judge);
                                        i = 0;

                                        message = Message.obtain();
                                        message.arg1 = 1;
                                        handler.sendMessage(message);
                                        j = 0;
                                        FileService.saveFileToSdcard("face.jpg", image);
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else if (ready_judge == -1) { //-1基准帧不是正脸
                                        Log.e(TAG, "正脸检测没通过ready_judge***" + ready_judge);
                                        i = 0;//i,帧数的下标，每次检测获取5帧，所以最大为4
                                        j++;
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    } else if (ready_judge == -2) { //-2人脸离屏幕过远
                                        Log.e(TAG, "正脸检测没通过ready_judge***" + ready_judge);
                                        i = 0;//i,帧数的下标，每次检测获取5帧，所以最大为4
                                        j++;
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    } else if (ready_judge == -3) { //-3检测不到人脸或多张人脸
                                        Log.e(TAG, "正脸检测没通过ready_judge***" + ready_judge);
                                        i = 0;//i,帧数的下标，每次检测获取5帧，所以最大为4
                                        j++;
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    } else if (ready_judge == -4) { //-4不是真人（照片、视频攻击）
                                        Log.e(TAG, "正脸检测没通过step-------***" + step);
                                        i = 0;//i,帧数的下标，每次检测获取5帧，所以最大为4
                                        j++;
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    }
                                }
                                break;
                            case 1: {//随意一个动作
                                    Log.e(TAG, "随意一个动作step---***---");
                                    int judge_action_face = faceallHandler.EXFACE_judge_action(image, faceallHandler.FACE);
                                    FileService.saveFileToSdcard("face11.jpg", image);
                                    int judge_action_eye = faceallHandler.EXFACE_judge_action(image, faceallHandler.EYE);
                                    int judge_action_mouth = faceallHandler.EXFACE_judge_action(image, faceallHandler.MOUTH);
                                    int judge_action_pose = faceallHandler.EXFACE_judge_action(image, faceallHandler.POSE);
                                    Log.e(TAG, "judge_action_face ======================= " + judge_action_face);
                                    Log.e(TAG, "judge_action_eye====== " + judge_action_eye);
                                    Log.e(TAG, "judge_action_mouth==== " + judge_action_mouth);
                                    Log.e(TAG, "judge_action_pose==== " + judge_action_pose);
                                    if (1 == judge_action_face) { //1通过,-1脸太大或没检测到脸，不通过，0有脸没动作，不通过
                                        Log.e(TAG, "judge_action_face = 1通过");
                                        i = 0;
                                        message = Message.obtain();
                                        message.arg1 = 2;

                                        handler.sendMessage(message);
                                        j = 0;//j,错误的次数，一次检测如果连续错误五次，就跳出报错
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else { //没有动作，不通过
                                        Log.e(TAG, "judge_action_face = 没有动作，不通过");
                                        i = 0;
                                        j++;
//                                        try {
//                                            Thread.sleep(1000);
//                                        } catch (InterruptedException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
                                        if (j == 4) {
                                            i = 5;

                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    }
                                    Log.e(TAG, "judge_action_face=====" + judge_action_face);
                                    if(judge_action_pose == 1) { //1通过
                                        i = 0;
                                        message = Message.obtain();
                                        message.arg1 = 2;

                                        handler.sendMessage(message);
                                        j = 0;
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else { //没有动作，不通过
                                        i = 0;
                                        j++;
//                                        try {
//                                            Thread.sleep(1000);
//                                        } catch (InterruptedException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    }
                                    if(judge_action_eye == 1) { //1通过
                                        i = 0;
                                        message = Message.obtain();
                                        message.arg1 = 2;

                                        handler.sendMessage(message);
                                        j = 0;
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else { //没有动作，不通过
                                        i = 0;
                                        j++;
//                                        try {
//                                            Thread.sleep(1000);
//                                        } catch (InterruptedException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    }

                                    if(judge_action_mouth == 1) { //1通过
                                        i = 0;
                                        message = Message.obtain();
                                        message.arg1 = 2;

                                        handler.sendMessage(message);
                                        j = 0;
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else { //没有动作，不通过
                                        i = 0;
                                        j++;
//                                        try {
//                                            Thread.sleep(1000);
//                                        } catch (InterruptedException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
                                        if (j == 4) {
                                            i = 5;
                                            message = Message.obtain();
                                            message.arg1 = 3;
                                            handler.sendMessage(message);
                                        }
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }


        });
        initThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit = true;
        i=5;
        //DetectLandmarkLib.ReleaseFaceLandmark();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        iv.setBackgroundResource(R.drawable.fir);
        localAnimationDrawable = (AnimationDrawable) iv.getBackground();
        localAnimationDrawable.start();
    }

    private void initView() {
        preview = (SurfaceView) findViewById(R.id.preview);

        holder = preview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void preview() throws IOException {
        camera = Camera.open(1);
        camera.setDisplayOrientation(90);
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(1);
        width = selected.width;
        height = selected.height;

        params.setPreviewSize(320, 240);
//        params.setPictureFormat(PixelFormat.RGB_565);
//        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        params.setPreviewSize(width, height);
        camera.setParameters(params);
        camera.setPreviewDisplay(holder);
        camera.startPreview();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera arg1) {
        // TODO Auto-generated method stub
//        Log.i(TAG, "onPreviewFrame");

        Camera.Size size = camera.getParameters().getPreviewSize(); //��ȡԤ����С
        final int w = size.width; //���
        final int h = size.height; //�߶�
        final YuvImage image = new YuvImage(data, ImageFormat.NV21, w, h, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        System.out.println("in2");
        if(!image.compressToJpeg(new Rect(0, 0, w, h), 100, os)){
            return;
        }
        // System.out.println("out1");
        byte[] tmp = os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        // doSomethingNeeded(bmp); //自己定义的实时分析预览帧视频的算法
        Bitmap bmp1 = utils.PicProcessUtils.rotaingImageView(270, bmp);
//		 iv[i].setImageBitmap(bmp1);
        System.out.println("out2");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp1.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        tmp = stream.toByteArray();
        use[i] = tmp;

        // doSomethingNeeded(bmp); //�Լ������ʵʱ����Ԥ��֡��Ƶ���㷨
        i++;

        System.out.println("out3");
        return;
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        // if(isPre) {
        // camera.stopPreview();
        // }
        Camera.Parameters params = camera.getParameters();
//        params.setPreviewSize(width, height);
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        isPre = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        try {
            camera.setPreviewDisplay(preview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        //super.onDestroy();
        camera.stopPreview();
        isPre = false;
        camera.release();
        holder = null;
        exit = true;
        i=5;
    }
}

