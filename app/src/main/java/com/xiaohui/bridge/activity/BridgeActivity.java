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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.bean.ChildBridge;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ChildBridgeModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.util.DeviceParamterUtil;
import com.xiaohui.bridge.util.LogUtil;

import java.util.Iterator;


public class BridgeActivity extends AbstractOrmLiteActivity<DatabaseHelper> {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private BridgesFragment bridgesFragment;

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

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.elv_content);
        listView.setAdapter(adapter);
        //设置item点击的监听器
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(BridgeActivity.this, DiseaseListActivity.class);
                intent.putExtra("title", (String) adapter.getChild(groupPosition, childPosition));
                intent.putExtra(Keys.KeySelectedComponentName, (String) adapter.getGroup(groupPosition));
                intent.putExtra(Keys.KeySelectedPositionName, (String) adapter.getChild(groupPosition, childPosition));
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
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.i("checkedId:" + checkedId);
            }
        });
        BridgeModel bridge = (BridgeModel) getCookie().get(Keys.BRIDGE);
        Iterator<ChildBridgeModel> iterator = bridge.getChildBridges().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            ChildBridgeModel childBridgeModel = iterator.next();
            ChildBridge childBridge = childBridgeModel.getChildBridge();
            RadioButton rb = new RadioButton(this);
            rb.setText(childBridge.getName());
            rb.setTextSize(20);
            rb.setTextColor(getResources().getColorStateList(R.color.tv_black_blue));
            rb.setBackgroundResource(0);
            rb.setButtonDrawable(0);
            rb.setGravity(Gravity.CENTER);
            rb.setTag(i);
            rb.setId(View.NO_ID);
            RadioGroup.LayoutParams lp
                    = new RadioGroup.LayoutParams(DeviceParamterUtil.dip2px(150),
                    ViewGroup.LayoutParams.MATCH_PARENT);
            rg.addView(rb, lp);
            if (i == 0) {
                rg.check(rb.getId());
            }
            i++;
        }
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
                {"主梁"},
                {"横隔板", "其它"},
                {"支座"},
                {"翼墙", "耳墙"},
                {"锥坡", "护坡"},
                {"墩身", "盖梁", "系梁"},
                {"台身", "台帽"},
                {"基础"},
                {"河床"},
                {"调治构造物"},
                {"沥青混凝土铺装", "水泥混凝土铺装", "其它"},
                {"伸缩缝装置"},
                {"人行道"},
                {"栏杆、护栏"},
                {"排水系统"},
                {"照明", "标志"}
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
                convertView = View.inflate(BridgeActivity.this, R.layout.view_part_item, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTitle.setText((groupPosition + 1) + " " + generalsTypes[groupPosition]);
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
