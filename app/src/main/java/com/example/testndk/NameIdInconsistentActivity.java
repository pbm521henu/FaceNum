package com.example.testndk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NameIdInconsistentActivity extends AppCompatActivity {
    private TextView textView;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_id_inconsistent);
        textView = (TextView)findViewById(R.id.text);
        btnBack = (Button)findViewById(R.id.btn_back);

        Intent intent = getIntent();
        int i = intent.getIntExtra("nameid",0);
        if (i == 1){
            textView.setText("姓名与身份证号不一致");
        } else if (i == 2){
            textView.setText("无此号，请核对身份证号码");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
