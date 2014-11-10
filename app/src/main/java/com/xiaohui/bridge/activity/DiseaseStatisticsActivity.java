package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.component.DiseaseItemView;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseStatisticsActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_statistics);
        setTitle("病害统计");

        LinearLayout diseaseContentLayout = (LinearLayout) findViewById(R.id.ll_disease_content_layout);
        ComponentModel componentModel = (ComponentModel) getCookie().get(Keys.COMPONENT);

        for(int i = 0; i < componentModel.getDiseases().size(); i ++){
            DiseaseItemView itemView = new DiseaseItemView(this);
            diseaseContentLayout.addView(itemView.getInitDataView(componentModel.getDiseases().get(i).getDisease()));
        }
    }

}

