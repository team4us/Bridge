package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.component.DiseaseItemView;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        List<DiseaseModel> allDiseaseModels = componentModel.getDiseases();
        Map<String, ArrayList<DiseaseModel>> diseasesMap = new HashMap<String, ArrayList<DiseaseModel>>();
        // 将该部件下的所有病害分类
        for (int j = 0; j < allDiseaseModels.size(); j++) {
            DiseaseModel diseaseModel = allDiseaseModels.get(j);
            String diseaseKey = diseaseModel.getDisease().getType() + diseaseModel.getDisease().getMethod();
            // 判断这个病害的病害类型和输入方式在diseaseTypeList这个列表中是否存在，如果存在，那么将这个病害
            // 放到他的集合中，如果不存在，那么新增一个集合

            if (diseasesMap.size() > 0) {
                boolean isContain = false;
                String sameDiseaseKey = "";

                Iterator iterator = diseasesMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String key = (String) entry.getKey();
                    if (key.equals(diseaseKey)) {
                        isContain = true;
                        sameDiseaseKey = diseaseKey;
                        break;
                    }
                }

                if (isContain) {
                    ArrayList<DiseaseModel> models = diseasesMap.get(sameDiseaseKey);
                    models.add(diseaseModel);
                    diseasesMap.put(sameDiseaseKey, models);
                } else {
                    ArrayList<DiseaseModel> models = new ArrayList<DiseaseModel>();
                    models.add(diseaseModel);
                    diseasesMap.put(diseaseKey, models);
                }
            } else {
                ArrayList<DiseaseModel> models = new ArrayList<DiseaseModel>();
                models.add(diseaseModel);
                diseasesMap.put(diseaseKey, models);
            }
        }
        // TODO 用分类的病害初始化视图
//      diseaseContentLayout.addView(itemView.getInitDataView(componentModel.getDiseases().get(i).getDisease()));
    }

}

