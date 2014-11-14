package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.component.ImageFragment;
import com.xiaohui.bridge.util.DeviceParameterUtil;
import com.xiaohui.bridge.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class PictureRemoverActivity extends AbstractActivity {

    private ImageLoader imageLoader;
    private ViewPager viewPager;
    private int currentIndex;
    private List<String> photoList;
    private String title;
    private ArrayList<Integer> removedList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        title = "图片选择";
        imageLoader = ImageLoader.getInstance();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        photoList = getIntent().getStringArrayListExtra(Keys.Content);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ImageFragment.newInstance(photoList.get(position));
            }

            @Override
            public int getCount() {
                return photoList.size();
            }

            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }
        });
        currentIndex = getIntent().getIntExtra(Keys.SelectedIndex, 0);
        viewPager.setCurrentItem(currentIndex);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int position) {// 页面选择响应函数
                currentIndex = position;
                updateTitle();
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

            }

            public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

            }
        });
        updateTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageLoader.stop();
    }

    private void updateTitle() {
        String flag = " (" + (currentIndex + 1) + "/" + photoList.size() + ")";
        setTitle(title + flag);
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickDelete(View view) {
        removedList.add(currentIndex);
        photoList.remove(currentIndex);
        int size = photoList.size();
        if (size <= 0) {
            done();
        }
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setCurrentItem(currentIndex);
        updateTitle();
    }

    public void onClickDone(View view) {
        done();
    }

    private void done() {
        int size = ListUtil.sizeOfList(removedList);
        if (size <= 0) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putIntegerArrayListExtra(Keys.Content, removedList);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
