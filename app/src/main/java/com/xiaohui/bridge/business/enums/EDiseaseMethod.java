package com.xiaohui.bridge.business.enums;

import com.xiaohui.bridge.R;

import java.io.Serializable;

/**
 */
public enum EDiseaseMethod implements Serializable {
    MethodOne(R.string.method_name_0, R.layout.view_disease_method_0, R.id.rb_method_0, 4),
    MethodTwo(R.string.method_name_1, R.layout.view_disease_method_1, R.id.rb_method_1, 3),
    MethodThree(R.string.method_name_2, R.layout.view_disease_method_2, R.id.rb_method_2, 4),
    MethodFour(R.string.method_name_3, R.layout.view_disease_method_3, R.id.rb_method_3, 4),
    MethodFive(R.string.method_name_4, R.layout.view_disease_method_4, R.id.rb_method_4, 3),
    MethodSix(R.string.method_name_5, R.layout.view_disease_method_5, R.id.rb_method_5, 2);

    private int nameResId;
    private int viewResId;
    private int radioButtonResId;
    private int count; //标识该种记录方法包含几个键值对

    private EDiseaseMethod(int nameResId, int viewResId, int radioButtonResId, int count) {
        this.nameResId = nameResId;
        this.viewResId = viewResId;
        this.radioButtonResId = radioButtonResId;
        this.count = count;
    }

    public int getViewResId() {
        return viewResId;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getRadioButtonResId() {
        return radioButtonResId;
    }

    public int getCount() {
        return count;
    }
}
