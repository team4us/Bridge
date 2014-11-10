package com.xiaohui.bridge.activity;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.component.DataAdapter;

import java.util.ArrayList;
import java.util.List;


public class PictureSelectActivity extends AbstractActivity {

    private DataAdapter<String> pictureAdapter;
    private int remainCount;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        setTitle("选择图片");
        Bundle bundle = getIntent().getExtras();
        remainCount = bundle.getInt(Keys.PictureCount, 9);

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        CursorLoader cursorLoader = new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
        Cursor imageCursor = cursorLoader.loadInBackground();

        List<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imageCursor.getCount(); i++) {
            imageCursor.moveToPosition(i);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imageCursor.getString(dataColumnIndex));
        }


        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_no_picture)
                .showImageForEmptyUri(R.drawable.icon_no_picture)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        pictureAdapter = new DataAdapter<String>(this, new DataAdapter.IViewCreator<String>() {
            @Override
            public View createView(LayoutInflater inflater, ViewGroup parent) {
                return inflater.inflate(R.layout.view_multi_picture_item, parent, false);
            }

            @Override
            public void bindDataToView(View view, String data, int position) {
                ImageView imageView = (ImageView) findViewById(R.id.iv_thumb);
                ImageLoader.getInstance().displayImage("file://" + data, imageView, options);
            }
        });
        pictureAdapter.setContent(imageUrls);

        GridView gridView = (GridView) findViewById(R.id.gv_content);
        gridView.setAdapter(pictureAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView ivCheck = (ImageView) view.findViewById(R.id.iv_check);
                if (ivCheck.getVisibility() == View.GONE) {
                    if (remainCount <= 0) {
                        Toast.makeText(PictureSelectActivity.this, R.string.more_than_nine_pictures, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    remainCount--;
                    ivCheck.setVisibility(View.VISIBLE);
                    sparseBooleanArray.put(position, true);
                } else {
                    remainCount++;
                    ivCheck.setVisibility(View.GONE);
                    sparseBooleanArray.put(position, false);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        ImageLoader.getInstance().stop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            onClickDone();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArray = new ArrayList<String>();

        for (int i = 0; i < pictureAdapter.getCount(); i++) {
            if (sparseBooleanArray.get(i)) {
                mTempArray.add(pictureAdapter.getItem(i));
            }
        }

        return mTempArray;
    }

    private void onClickDone() {
        ArrayList<String> selectedItems = getCheckedItems();
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Keys.Content, selectedItems);
        setResult(RESULT_OK, intent);
        finish();
    }
}