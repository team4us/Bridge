package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.Keys;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaohui on 14-1-14.
 */
public class MenuActivity extends AbstractActivity implements View.OnClickListener {

    private LinearLayout llMenuContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        llMenuContent = (LinearLayout) findViewById(R.id.ll_menu_content);
        initView();
    }

    @SuppressWarnings("unchecked")
    private void initView() {
        ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) getIntent().getSerializableExtra(Keys.KeyContent);
        int size = menuItems.size();
        for (int i = 0; i < size; i++) {
            View view = getViewWithData(menuItems.get(i));
            view.setTag(i);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.selector_btn_5);
            } else {
                llMenuContent.addView(getLineView());
            }
            view.setBackgroundResource(R.drawable.selector_btn_5);
            view.setClickable(true);
            view.setOnClickListener(this);
            llMenuContent.addView(view);
        }

    }

    private View getViewWithData(MenuItem menuItem) {
        View view = getLayoutInflater().inflate(R.layout.view_menu_item, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(menuItem.getTitle());
        return view;
    }

    private ImageView getLineView() {
        ImageView line = new ImageView(this);
        line.setPadding(0,10,0,0);
        line.setBackgroundColor(getResources().getColor(R.color.color_transparent));
        return line;
    }

    public void onClickCancel(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int selectedIndex = (Integer) v.getTag();
        intent.putExtra(Keys.KeySelectedIndex, selectedIndex);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static class MenuItem implements Serializable {

        private String title;

        public MenuItem(String title) {
            setTitle(title);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
