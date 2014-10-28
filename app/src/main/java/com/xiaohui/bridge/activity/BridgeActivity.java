package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.BlockModel;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ChildBridgeModel;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.util.DeviceParamterUtil;


public class BridgeActivity extends AbstractOrmLiteActivity<DatabaseHelper> {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private BridgesFragment bridgesFragment;
    private ChildBridgeModel childBridgeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        bridgesFragment = (BridgesFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        bridgesFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        BridgeModel bridge = (BridgeModel) getCookie().get(Keys.BRIDGE);
        childBridgeModel = bridge.getChildBridges().get(0);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.elv_content);
        listView.setAdapter(adapter);
        //设置item点击的监听器
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(BridgeActivity.this, DiseaseListActivity.class);
                ComponentModel componentModel = (ComponentModel) adapter.getChild(groupPosition, childPosition);
                getCookie().put(Keys.COMPONENT, componentModel);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!bridgesFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.bridge_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bridge_detail) {
            startActivity(new Intent(this, BridgeDetailActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateChildBridgeView() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_child_bridge);
        rg.removeAllViews();

        BridgeModel bridge = (BridgeModel) getCookie().get(Keys.BRIDGE);
        for (int i = 0; i < bridge.getChildBridges().size(); i++) {
            ChildBridgeModel model = bridge.getChildBridges().get(i);
            RadioButton rb = new RadioButton(this);
            rb.setText(model.getChildBridge().getName());
            rb.setTextSize(20);
            rb.setTextColor(getResources().getColorStateList(R.color.tv_black_blue));
            rb.setBackgroundResource(0);
            rb.setButtonDrawable(0);
            rb.setGravity(Gravity.CENTER);
            rb.setTag(model);
            rb.setId(View.NO_ID);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    childBridgeModel = null;
                    if (isChecked) {
                        childBridgeModel = (ChildBridgeModel) buttonView.getTag();
                    }
                }
            });
            RadioGroup.LayoutParams lp
                    = new RadioGroup.LayoutParams(DeviceParamterUtil.dip2px(150),
                    ViewGroup.LayoutParams.MATCH_PARENT);
            rg.addView(rb, i, lp);
            if (i == 0) {
                rg.check(rb.getId());
            }
        }
    }

    final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

        @Override
        public int getGroupCount() {
            return childBridgeModel.getBlocks().size();
        }

        @Override
        public BlockModel getGroup(int groupPosition) {
            return childBridgeModel.getBlocks().get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return getGroup(groupPosition).getComponents().size();
        }

        @Override
        public ComponentModel getChild(int groupPosition, int childPosition) {
            return getGroup(groupPosition).getComponents().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(BridgeActivity.this, R.layout.view_part_item, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTitle.setText((groupPosition + 1) + " " + getGroup(groupPosition).getBlock().getName());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expandable);
            imageView.setImageResource(isExpanded ? R.drawable.icon_expand : R.drawable.icon_collapse);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(BridgeActivity.this, R.layout.view_part_child_item, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTitle.setText(getChild(groupPosition, childPosition).getComponent().getName());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition,
                                         int childPosition) {
            return true;
        }

    };
}
