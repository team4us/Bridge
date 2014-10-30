package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IDiseaseView;
import com.xiaohui.bridge.viewmodel.DiseaseViewModel;

/**
 * Created by xhChen on 14/10/29.
 */
public class DiseaseActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IDiseaseView {

    private DiseaseViewModel viewModel;
    private boolean isNewDisease;
    private ComponentModel componentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNewDisease = getIntent().getBooleanExtra(Keys.FLAG, true);
        componentModel = (ComponentModel) getCookie().get(Keys.COMPONENT);
        viewModel = new DiseaseViewModel(this, getCookie());
        setContentView(R.layout.activity_disease, viewModel);
        setTitle(isNewDisease ? "病害新增" : "病害编辑");
    }
}
