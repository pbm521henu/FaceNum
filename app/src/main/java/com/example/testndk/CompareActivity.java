package com.example.testndk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

import httpClient.FaMethod;
import utils.FileService;

public class CompareActivity extends AppCompatActivity {
    private ImageView img1;
    private ImageView img2;

    private boolean flag = false;
    private static TextView textView;
    private final String uri = "";
    private final static String TAG        = "FACEALL SDK TEST ";
    /*
     * Step 1
     * 初始化api
     */
    private final static String apiKey     = "uD8kxo8E3pis6c0zktiqZtANvftDRgW1W2p5bWxZ";//你的api_key和_secret,请联系我们获得:contact@faceall.cn
    private final static String apiSecret  = "OMRdmmy2Ua0dXgUBpoYffrNnm2worVDXRHQxdg6N";
    private final static String apiVersion = "v2";
    /*
     * Step 2
     * 初始化图片路径
     */
    private static final File      FILE1   = new File(Environment.getExternalStorageDirectory() + "/picture/idcardp.jpg"); //手机存储卡路径，如/picture/testPicture1.jpeg
    private static final File      FILE2   = new File(Environment.getExternalStorageDirectory() + "/picture/face.jpg"); //手机存储卡路径，如/picture/testPicture2.jpeg

    /*
    * Step 3
    * 初始化暂存数据的变量
    */
    private String faceId1                 = "";
    private String faceId2                 = "";
    private Button btn_ok;
    private static String score;

    JSONObject resultObj                   = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            double dscore = (Double)msg.obj;
//            Log.i("====scoresss===",dscore+"");
            DecimalFormat df = new DecimalFormat("#0.00");
            String strScore = df.format(dscore*100)+"%";
            textView.setText("相似度为："+ strScore);
            flag = true;
            btn_ok.setEnabled(true);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        btn_ok = (Button)findViewById(R.id.btn_ok);

        textView = (TextView) findViewById(R.id.pec);
        Intent intent = getIntent();

        int i = intent.getIntExtra("idcardpic",0);

        if(i==1) {
            byte[] face = FileService.readContentFromSdcard("idcardp.jpg");
            Bitmap a = BitmapFactory.decodeByteArray(face, 0, face.length);
            img1.setImageBitmap(a);
        }
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CompareActivity.this, FaceActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
    }

    public static String getScore(){
        return score;
    }

    public void btnOk(View view){
        if(flag) {
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1000 && resultCode == 1001){
            //Toast.makeText(CompareActivity.this,"hhh",Toast.LENGTH_LONG).show();
            byte[] face = FileService.readContentFromSdcard("face.jpg");
            Bitmap a = BitmapFactory.decodeByteArray(face, 0, face.length);
            img2.setImageBitmap(a);
                            new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                                /* Step 4
						         * 发送数据，并获取结果*/

                            Log.d(TAG, "==============detection======================");
                            FaMethod famethod = new FaMethod(apiKey, apiSecret, apiVersion);
                            JSONObject json1 = famethod.detection_detect(FILE1, "", "", "false");
                            JSONObject json2 = famethod.detection_detect(FILE2, "", "", "false");
                            Log.e(TAG, json1.toString());
                            faceId1 = json1.getJSONArray("faces").getJSONObject(0).getString("id");
                            faceId2 = json2.getJSONArray("faces").getJSONObject(0).getString("id");

                            Log.d(TAG, json2.toString());
                            resultObj = famethod.recognition_compare_face(faceId1, faceId2, "life");
                            Log.e(TAG, resultObj.toString());

                            score = resultObj.getString("score");
                            double fscore = Double.parseDouble(score);
                            Message message = new Message();
                            message.obj = fscore;
                            handler.sendMessage(message);
                            Log.d(TAG, "==============" + resultObj.toString() + "============");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }).start();
        }
    }
}
