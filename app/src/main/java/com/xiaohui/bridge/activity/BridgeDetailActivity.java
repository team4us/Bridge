package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.bean.ChildBridge;
import com.xiaohui.bridge.viewmodel.BridgeDetailViewModel;
import com.xiaohui.bridge.viewmodel.ChildBridgeDetailViewModel;

/**
 * 桥梁详情界面
 * Created by Administrator on 2014/9/27.
 */
public class BridgeDetailActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bridge bridge = (Bridge) getCookie().get(Keys.BRIDGE);
        setContentView(R.layout.activity_bridge_detail, new BridgeDetailViewModel(bridge));
        setTitle("桥梁详情");

        for (ChildBridge childBridge : bridge.getChildBridges()) {
            LinearLayout llChild = (LinearLayout) findViewById(R.id.ll_child);
            View view = inflateView(R.layout.view_child_bridge_detail, new ChildBridgeDetailViewModel(childBridge));
            llChild.addView(view);
        }
    }
}
