package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.view.View;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;

/**
 * Created by Administrator on 2014/10/9.
 */
public class CoordinateActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);
    }

    public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
