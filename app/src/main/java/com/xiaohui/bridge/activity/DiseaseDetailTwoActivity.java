package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaohui.bridge.R;

/**
 * 病害录入模板二界面
 * Created by Administrator on 2014/9/27.
 */
public class DiseaseDetailTwoActivity extends BaseDiseaseDetailActivity{
    private Button btnAddPictureFromScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_disease_detail_two);
        super.onCreate(savedInstanceState);
        setTitle("病害录入模板二");

        btnAddPictureFromScreen = (Button) findViewById(R.id.btn_addposition_from_screen);
        btnAddPictureFromScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if(v == btnAddPictureFromScreen){

        }
    }
}
