package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.BaseInputModel;
import com.xiaohui.bridge.model.InputType5;

/**
 * 病害录入模板5
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate5 extends DiseaseBaseInputTemplate{
    private Context context;
    private EditText position;
    private EditText etDescription;
    private EditText etImageNumber;
    private EditText etMore;

    public DiseaseInputTemplate5(Context context) {
        super(context);
        initView(context);
    }

    public DiseaseInputTemplate5(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiseaseInputTemplate5(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void initView(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_disease_input_5, this);
        position = (EditText) view.findViewById(R.id.et_position);
        etDescription = (EditText) view.findViewById(R.id.et_description);
        etImageNumber = (EditText) view.findViewById(R.id.et_image_number);
        etMore = (EditText) view.findViewById(R.id.et_more);
    }

    @Override
    public boolean isHasEmptyData() {
        if(position.getText().toString().trim().length() < 1){
            return true;
        }
        if(etDescription.getText().toString().trim().length() < 1){
            return true;
        }
        if(etImageNumber.getText().toString().trim().length() < 1){
            return true;
        }
        return false;
    }

    @Override
    public BaseInputModel getInputModel() {
        return new InputType5(getEditTextString(position), getEditTextString(etDescription),
                getEditTextString(etImageNumber), getEditTextString(etMore));
    }

}
