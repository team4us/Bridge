package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Bridge;


public class BridgeActivity extends AbstractActivity
        implements BridgesFragment.OnBridgeSelectListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private BridgesFragment bridgesFragment;
    private Bridge bridge;
    private ExpandableListView listView;

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

        listView = (ExpandableListView) findViewById(R.id.elv_content);
        listView.setAdapter(adapter);
        //设置item点击的监听器
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                startActivity(new Intent(BridgeActivity.this, DiseaseListActivity.class));
                return false;
            }
        });
    }

    @Override
    public void onSelectedBridge(Bridge bridge) {
        this.bridge = bridge;
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

    final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

        private String[] generalsTypes = new String[]{"上部承重构件",
                "上部一般构件",
                "支座",
                "翼墙、耳墙",
                "锥坡、护坡",
                "桥墩",
                "桥台",
                "墩台基础",
                "河床",
                "调治构造物",
                "桥面铺装",
                "伸缩缝装置",
                "人行道",
                "栏杆、护栏",
                "排水系统",
                "照明、标志"};

        private String[][] generals = new String[][]{
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"},
                {"构件1", "构件2", "构件3"}
        };

        @Override
        public int getGroupCount() {
            return generalsTypes.length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return generalsTypes[groupPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return generals[groupPosition].length;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return generals[groupPosition][childPosition];
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
                convertView = View.inflate(BridgeActivity.this, R.layout.view_group, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTitle.setText(groupPosition + "." + generalsTypes[groupPosition]);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(BridgeActivity.this, R.layout.view_child, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTitle.setText(generals[groupPosition][childPosition]);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition,
                                         int childPosition) {
            return true;
        }

    };
}