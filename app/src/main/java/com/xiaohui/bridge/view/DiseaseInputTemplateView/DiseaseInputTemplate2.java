package com.xiaohui.bridge.view.DiseaseInputTemplateView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaohui.bridge.R;

/**
 * 病害录入模板2
 * Created by Administrator on 2014/10/11.
 */
public class DiseaseInputTemplate2 extends LinearLayout{
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_disease_input_2, this);
    }
}
