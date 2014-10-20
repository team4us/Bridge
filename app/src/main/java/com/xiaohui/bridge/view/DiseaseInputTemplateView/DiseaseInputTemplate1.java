package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.activity.CoordinateActivity;

/**
 * 病害录入模板1
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate1 extends DiseaseBaseInputTemplate implements View.OnClickListener {
    private Context context;
    private EditText etStartPoint;
    private EditText etEndPoint;
    private EditText etLenght;
    private EditText etWidth;
    private EditText etImageNumber;

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

    public void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_disease_input_1, this);
        etStartPoint = (EditText) view.findViewById(R.id.et_startpoint);
        etEndPoint = (EditText) view.findViewById(R.id.et_endpoint);
        etLenght = (EditText) view.findViewById(R.id.et_length);
        etWidth = (EditText) view.findViewById(R.id.et_width);
        etImageNumber = (EditText) view.findViewById(R.id.et_image_number);
        view.findViewById(R.id.btn_add_position_from_screen).setOnClickListener(this);
    }

    @Override
    public boolean isHasEmptyData() {
        if(etStartPoint.getText().toString().trim().length() < 1){
            return true;
        }
        if(etEndPoint.getText().toString().trim().length() < 1){
            return true;
        }
        if(etLenght.getText().toString().trim().length() < 1){
            return true;
        }
        if(etWidth.getText().toString().trim().length() < 1){
            return true;
        }
        if(etImageNumber.getText().toString().trim().length() < 1){
            return true;
        }
        return false;
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
