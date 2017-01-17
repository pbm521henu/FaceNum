package com.example.testndk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class StartActivity extends Activity {
    private Button button;
    private Button buttonG;
    private final static String TAG = "StartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = (Button)findViewById(R.id.button);
        buttonG = (Button)findViewById(R.id.buttonG);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File root = Environment.getExternalStorageDirectory();
                File file = new File(root.getAbsolutePath() + "/picturessss");
                //File file = new File("/picture");
                if(!file.exists()) {
                    file.mkdirs();
                }
                Log.e("qqqqqqqqqq",Environment.getExternalStorageState());
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, IDCardActivity.class);
                startActivity(intent);

            }
        });
        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

}
