package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.activity.CoordinateActivity;
import com.xiaohui.bridge.model.BaseInputModel;
import com.xiaohui.bridge.model.InputType2;

/**
 * 病害录入模板2
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate2 extends DiseaseBaseInputTemplate implements View.OnClickListener{
    private Context context;
    private EditText position;
    private EditText etLength;
    private EditText etWidth;
    private EditText etImageNumber;
    private EditText etMore;

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

    public void initView(Context context){
        this.context = context;
        View view = View.inflate(context, R.layout.view_disease_input_2, this);
        position = (EditText) view.findViewById(R.id.et_startpoint);
        etLength = (EditText) view.findViewById(R.id.et_length);
        etWidth = (EditText) view.findViewById(R.id.et_width);
        etImageNumber = (EditText) view.findViewById(R.id.et_image_number);
        etMore = (EditText) view.findViewById(R.id.et_more);
        view.findViewById(R.id.btn_add_position_from_screen).setOnClickListener(this);
    }

    @Override
    public boolean isHasEmptyData() {
        if(position.getText().toString().trim().length() < 1){
            return true;
        }
        if(etLength.getText().toString().trim().length() < 1){
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
    public BaseInputModel getInputModel() {
        return new InputType2(getEditTextString(position), getEditTextString(etLength),
                getEditTextString(etWidth), getEditTextString(etImageNumber), getEditTextString(etMore));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add_position_from_screen){
            Intent intent = new Intent();
            intent.setClass(context, CoordinateActivity.class);
            context.startActivity(intent);
        }
    }
}
