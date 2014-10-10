package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.activity.CoordinateActivity;

/**
 * 病害录入模板2
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate2 extends LinearLayout implements View.OnClickListener{
    private Context context;
    private Button btnAddFromScreen;

    public DiseaseInputTemplate2(Context context) {
        super(context);
        initView(context);
    }

    public DiseaseInputTemplate2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiseaseInputTemplate2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_disease_input_2, this);
        btnAddFromScreen = (Button) view.findViewById(R.id.btn_add_position_from_screen);
        btnAddFromScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnAddFromScreen){
            Intent intent = new Intent();
            intent.setClass(context, CoordinateActivity.class);
            context.startActivity(intent);
        }
    }
}
