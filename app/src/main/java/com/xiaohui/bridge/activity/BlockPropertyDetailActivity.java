package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.BlockModel;
import com.xiaohui.bridge.storage.DatabaseHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 构件详情页面
 * Created by Administrator on 2014/10/31.
 */
public class BlockPropertyDetailActivity extends AbstractOrmLiteActivity<DatabaseHelper>{
    private Map<String, String> blockProperty = new HashMap<String, String>();
    private LinearLayout llPropertyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_property);

        llPropertyLayout = (LinearLayout) findViewById(R.id.ll_property_layout);

        blockProperty = ((BlockModel) getCookie().get(Keys.BLOCK)).getBlockProperty();
        Iterator iterator = blockProperty.keySet().iterator();
        while (iterator.hasNext()) {
            String title = (String) iterator.next();
            String value = blockProperty.get(title);
            llPropertyLayout.addView(getItemView(title, value));
        }
    }

    private View getItemView(String title, String value){
        View view = View.inflate(this,R.layout.view_property_item,null);
        TextView titleView = (TextView) view.findViewById(R.id.tv_property_title);
        TextView valueView = (TextView) view.findViewById(R.id.tv_property_value);
        titleView.setText(title);
        valueView.setText(value);
        return view;
    }
}
