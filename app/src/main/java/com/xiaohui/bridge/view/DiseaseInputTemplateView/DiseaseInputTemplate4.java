package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.BaseInputModel;
import com.xiaohui.bridge.model.InputType4;

/**
 * 病害录入模板4
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate4 extends DiseaseBaseInputTemplate{
    private Context context;
    private EditText position;
    private EditText etLength;
    private EditText etWidth;
    private EditText etImageNumber;
    private EditText etMore;

    public DiseaseInputTemplate4(Context context) {
        super(context);
        initView(context);
    }

    public DiseaseInputTemplate4(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiseaseInputTemplate4(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void initView(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_disease_input_4, this);
        position = (EditText) view.findViewById(R.id.et_point);
        etLength = (EditText) view.findViewById(R.id.et_length);
        etWidth = (EditText) view.findViewById(R.id.et_width);
        etImageNumber = (EditText) view.findViewById(R.id.et_image_number);
        etMore = (EditText) view.findViewById(R.id.et_more);
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
        return new InputType4(getEditTextString(position), getEditTextString(etLength),
                getEditTextString(etWidth), getEditTextString(etImageNumber), getEditTextString(etMore));
    }

}
