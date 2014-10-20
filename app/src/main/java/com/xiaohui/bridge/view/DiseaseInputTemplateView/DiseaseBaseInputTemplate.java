package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.activity.CoordinateActivity;
import com.xiaohui.bridge.model.BaseInputModel;

/**
 * 病害录入模板2
 * Created by Administrator on 2014/10/11.
 */
public abstract class DiseaseBaseInputTemplate extends LinearLayout{
    public DiseaseBaseInputTemplate(Context context) {
        super(context);
        initView(context);
    }

    public DiseaseBaseInputTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiseaseBaseInputTemplate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public abstract void initView(Context context);

    public abstract boolean isHasEmptyData();

    public abstract BaseInputModel getInputModel();

    public String getEditTextString(EditText et){
        return et.getText().toString().trim();
    }
}
