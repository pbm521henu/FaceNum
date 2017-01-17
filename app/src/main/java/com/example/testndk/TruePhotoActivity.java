package com.example.testndk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import utils.FileService;

public class TruePhotoActivity extends AppCompatActivity {
    private ImageView imgId,imgRes;
    private TextView resMessage;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_photo);

        imgId = (ImageView) findViewById(R.id.imgId);
        imgRes = (ImageView) findViewById(R.id.imgRes);
        resMessage = (TextView)findViewById(R.id.pec);
        btnNext = (Button)findViewById(R.id.btn_next);

        Intent intent = getIntent();
        int i = intent.getIntExtra("idcardpic",0);
        if(i==1) {
            byte[] face = FileService.readContentFromSdcard("idcardp.jpg");
            Bitmap a = BitmapFactory.decodeByteArray(face, 0, face.length);
            imgId.setImageBitmap(a);
            byte[] faceRes = FileService.readContentFromSdcard("idcardphoto.jpg");
            Bitmap b = BitmapFactory.decodeByteArray(faceRes, 0, faceRes.length);
            imgRes.setImageBitmap(b);
            resMessage.setText("姓名与身份证号一致");
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(TruePhotoActivity.this, CompareActivity.class);
                    intent.putExtra("idcardpic", 1);
                    startActivityForResult(intent, 1000);
                    finish();
                }
            });
        }
    }
}
