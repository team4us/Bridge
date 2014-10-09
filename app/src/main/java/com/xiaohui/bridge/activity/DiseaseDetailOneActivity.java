package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaohui.bridge.R;

/**
 * 病害录入模板一界面
 * Created by Administrator on 2014/9/27.
 */
public class DiseaseDetailOneActivity extends BaseDiseaseDetailActivity{
    private Button btnStartAddPictureFromScreen;
    private Button btnEndAddPictureFromScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_disease_detail_one);
        super.onCreate(savedInstanceState);
        setTitle("病害录入模板一");

        btnStartAddPictureFromScreen = (Button) findViewById(R.id.btn_start_addposition_from_screen);
        btnEndAddPictureFromScreen = (Button) findViewById(R.id.btn_end_addposition_from_screen);

        btnStartAddPictureFromScreen.setOnClickListener(this);
        btnEndAddPictureFromScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if(isHaveTag){
            return;
        }

        if(v == btnStartAddPictureFromScreen){

        } else if(v == btnEndAddPictureFromScreen){

        }
    }
}
