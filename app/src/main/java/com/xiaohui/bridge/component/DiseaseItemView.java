package com.xiaohui.bridge.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.util.DeviceParamterUtil;

/**
 * 病害统计页面单个病害视图
 * Created by Administrator on 2014/11/9.
 */
public class DiseaseItemView extends LinearLayout{
    private Context context;
    private TextView diseaseTypeName;
    private LinearLayout diseaseContentLayout;

    public DiseaseItemView(Context context) {
        super(context);
        this.context = context;
    }

    public DiseaseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DiseaseItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    private void initView(){
        View view = View.inflate(context, R.layout.view_disease_statistics_item_layout, null);
        diseaseTypeName = (TextView) view.findViewById(R.id.tv_disease_type_name);
        diseaseContentLayout = (LinearLayout) findViewById(R.id.ll_disease_content_layout);
    }

    public LinearLayout getInitDataView(){
        diseaseTypeName.setText("裂缝");


        return this;
    }

    /**
     * 获取单条显示内容的线性布局
     */
    private LinearLayout getItemLinearLayout(boolean isTitle){
        LinearLayout linearLayout= new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(HORIZONTAL);
        int padding = (int)(10 * DeviceParamterUtil.getScreenDensity());
        linearLayout.setPadding(padding, padding, padding, padding);
        if(isTitle){
            linearLayout.setBackgroundResource(R.color.color_099fde);
        } else {
            linearLayout.setBackgroundResource(R.color.color_ffffff);
        }
        return linearLayout;
    }

    /**
     * 获取单个显示内容TextView
     */
    private TextView getItemTextView(String text){
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams((int)(100 * DeviceParamterUtil.getScreenDensity()), LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setPadding(0, 0, (int)(5 * DeviceParamterUtil.getScreenDensity()), 0);
        return textView;
    }
}
