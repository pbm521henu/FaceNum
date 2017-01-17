package com.example.testndk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import exocr.engine.DataCallBack;
import exocr.engine.EngineManager;
import exocr.exocrengine.EXIDCardResult;
import exocr.idcard.IDCardManager;
import utils.DesEncrypter;
import utils.FileService;

public class IDCardActivity extends AppCompatActivity implements DataCallBack {

    private EditText name_edt_id,sex_edt_id,nation_edt_id,birth_edt_id,address_edt_id,num_edt_id,sign_edt_id,data_edt_id;
    private ImageView head_img_id,font_img_id,back_img_id;
    private boolean font = true;
    private EXIDCardResult result;
    private Button btn_ok;
    private TextView isCompleteFront;
    private TextView isCompleteBack;

    private static boolean flag = false;
    public static Bitmap IDCardFaceImage = null;
    private byte[] idcardp;
    private String idCardName;//姓名
    private String idCardCode;//身份证号
    private String TAG = "IDCardActivity";
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        //        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
//            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
//            tintManager.setStatusBarTintResource(R.color.default_gray);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(R.color.navi_white);
        }
        EngineManager.getInstance().initEngine(this);
        //初始化控件
        initWidget();
    }
    //初始化控件
    private void initWidget() {
        //EditText
        name_edt_id = ((EditText) findViewById(R.id.name_edt_id));
        sex_edt_id = ((EditText) findViewById(R.id.sex_edt_id));
        nation_edt_id = ((EditText) findViewById(R.id.nation_edt_id));
        birth_edt_id = ((EditText) findViewById(R.id.birth_edt_id));
        address_edt_id = ((EditText) findViewById(R.id.address_edt_id));
        num_edt_id = ((EditText) findViewById(R.id.num_edt_id));
        //ImageView
        head_img_id = ((ImageView) findViewById(R.id.head_img_id));
        font_img_id = ((ImageView) findViewById(R.id.font_img_id));
        btn_ok = ((Button) findViewById(R.id.btn_ok_id_id));
        isCompleteFront = ((TextView) findViewById(R.id.iscomplete_front_tv_id));
    }
    public void openDetecte(View view){
        if(R.id.font_ib_id==view.getId()) {
            font = true;
            IDCardManager.getInstance().setFront(font);
        }
//        switch (view.getId()){
//            case R.id.font_ib_id:
//                font = true;
//                IDCardManager.getInstance().setFront(font);
//                break;
//        }
//        IDCardManager.getInstance().setScanMode(IDCardManager.ID_IMAGEMODE_LOW);
        IDCardManager.getInstance().setShowLogo(false);
        IDCardManager.getInstance().recognize(this,this);

    }
    public void back(View view){

        if (flag) {
//            Intent intent = new Intent();
//            intent.setClass(IDCardActivity.this, CompareActivity.class);
//            intent.putExtra("idcardpic", 1);
//            startActivity(intent);
            if (resResult.equals("00")){
                Intent intent = new Intent();
                intent.setClass(IDCardActivity.this, TruePhotoActivity.class);
                intent.putExtra("idcardpic", 1);
                startActivity(intent);
                finish();
            } else if (resResult.equals("01")){
                Intent intent = new Intent(IDCardActivity.this,NameIdInconsistentActivity.class);
                intent.putExtra("nameid",1);
                startActivity(intent);
                finish();
            } else if (resResult.equals("02")) {
                Intent intent = new Intent(IDCardActivity.this, NameIdInconsistentActivity.class);
                intent.putExtra("nameid", 2);
                startActivity(intent);
                finish();
            }
        }
    }
    //身份证回调
    @Override
    public void onCardDetected(boolean success) {
        if (success){

            result = IDCardManager.getInstance().getResult();
            if (result.type==1){
                ByteArrayOutputStream baos = null;
                IDCardFaceImage = result.GetFaceBitmap();//脸部截图

                idCardName = result.name.trim();
                Log.d("name=====",idCardName);
                name_edt_id.setText(idCardName);

                sex_edt_id.setText(result.sex);
                nation_edt_id.setText(result.nation);
                birth_edt_id.setText(result.birth);
                address_edt_id.setText(result.address);

                idCardCode = result.cardnum.trim();
                num_edt_id.setText(idCardCode);

                head_img_id.setImageBitmap(IDCardFaceImage);
                baos = new ByteArrayOutputStream();
                IDCardFaceImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                idcardp = baos.toByteArray();
                FileService.saveFileToSdcard("idcardp.jpg", idcardp);
                font_img_id.setImageBitmap(result.stdCardIm);
                if (IDCardManager.getInstance().getScanMode()== IDCardManager.ID_IMAGEMODE_HIGH){
                    if(result.isComplete==1){
                        isCompleteFront.setTextColor(Color.RED);
                        isCompleteFront.setText(R.string.isCompleteFront);
                    }else if (result.isComplete==0){
                        isCompleteFront.setTextColor(Color.DKGRAY);
                        isCompleteFront.setText(R.string.notCompleteFront);
                    }
                }
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dataEstimate(idCardName,idCardCode);
                    }
                });
                thread.start();
                flag = true;
            }
            btn_ok.setEnabled(true);
        }
    }

    //身份证数据判断
    public void dataEstimate(String idCardName,String idCardCode){
        // 连接 url 地址
        String strUrl = "http://apidata.datatang.com/data/credit/queryIDCard";
        //dtkey 通过页面申请的 API KEY 。 ( 必须项目 )
        String strKey = "78288504a7ed3451b4e5d6a3f8e15d74";
        //rettype 需要返回的格式（支持 XML 及 JSON ） ( 必须项目 )
        String strRettype = "json";
        // API 需要的参数
        String encryptParam = "";
        DesEncrypter desEncrypter;
        try {
            desEncrypter = new DesEncrypter(strKey);
            encryptParam = desEncrypter.encrypt("idCardName="+idCardName+"&idCardCode="+idCardCode);
//            encryptParam="UddcrF9RqHlrGiTyzcI0Z0cIu6iRGKyoAbZjpcYjc2xQ6ZYSFC1hkn7cRkQu7i+K";//姓名和身份证号不一致
//            Log.d("*******",encryptParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 访问 URL 地址
        String url = strUrl + "?apikey=" + strKey + "&rettype=" + strRettype + "&encryptParam=" + encryptParam;
//        System.out.println("*****url*******" + url);
        String res;
        try {
            res = readByGet(url);//返回的json结果
//            System.out.println("***res****"+res);
            parseJsonResult(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String resIdCardPhoto;//返回的身份证照片，经过base64加密
    public String resResult;//返回状态，“00” 姓名与身份证号一致,“01”姓名与身份证号不一致,“02”无此号，请核对身份证号
    //解析返回的json结果
    public void parseJsonResult(String json){
        try {
            JSONObject jsonRes = JSON.parseObject(json);//把java对象转换为json对象
            resResult = jsonRes.getJSONObject("data").getString("result");
//            Log.d(TAG,resResult);
            if(resResult.equals("00")) {
                resIdCardPhoto = jsonRes.getJSONObject("data").getString("idCardPhoto");
//                Log.d(TAG,resIdCardPhoto);
                File idCardPhotoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/picture","idcardphoto.jpg");
                DesEncrypter.decodeBASE64(resIdCardPhoto,idCardPhotoFile);
            }
        } catch (JSONException ex){

        }
    }

    /**
     *  通过 GET 请求调用 url 获取结果
     * @param inUrl  请求 url
     * @throws IOException
     * @return String 获取的结果
     */
    private static String readByGet(String inUrl) throws IOException {
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        // 模拟浏览器
        String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36" + "(KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
        // 连接 URL 地址
        URL url = new URL(inUrl);
        // 根据拼凑的 URL ，打开连接， URL.openConnection 函数会根据 URL 的类型 ,
        // 返回不同的 URLConnection 子类的对象，这里 URL 是一个 http ，因此实际返回的是 HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接访问方法及超时参数
        connection.setRequestMethod("GET");
        connection.setReadTimeout(30000);
        connection.setConnectTimeout(30000);
        connection.setRequestProperty("User-agent", userAgent);
        // 进行连接，但是实际上 get request 要在下一句的connection.getInputStream() 函数中才会真正发到 服务器
        connection.connect();
        // 取得输入流，并使用 Reader 读取
        InputStream is = connection.getInputStream();
        // 读取数据编码处理
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(
                is, "UTF-8"));
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return sbf.toString();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        EngineManager.getInstance().finishEngine();
    }
}
