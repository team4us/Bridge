package com.xiaohui.bridge.component.PickPicture;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.activity.AbstractActivity;
import com.xiaohui.bridge.Keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ImageGridActivity extends AbstractActivity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_grid);

        setTitle("相册");

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_picture_done) {
            ArrayList<String> list = new ArrayList<String>();
            Collection<String> c = adapter.map.values();
            Iterator<String> it = c.iterator();
            for (; it.hasNext(); ) {
                list.add(it.next());
            }

            if (Bimp.act_bool) {
                setResult(Keys.ResultCodeSuccess);
                Bimp.act_bool = false;
            }
            for (int i = 0; i < list.size(); i++) {
                if (Bimp.drr.size() < 9) {
                    Bimp.drr.add(list.get(i));
                }
            }
            setResult(Keys.ResultCodeSuccess);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }
                adapter.notifyDataSetChanged();
            }

        });

    }
}
