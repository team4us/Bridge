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
 * 病害录入模板1
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate1 extends LinearLayout implements View.OnClickListener {
    private Context context;

    public DiseaseInputTemplate1(Context context) {
        super(context);
        initView(context);
    }

    public DiseaseInputTemplate1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiseaseInputTemplate1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_disease_input_1, this);
        view.findViewById(R.id.btn_add_position_from_screen).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_position_from_screen) {
            Intent intent = new Intent();
            intent.setClass(context, CoordinateActivity.class);
            context.startActivity(intent);
        }
    }
}
