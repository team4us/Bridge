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
import com.xiaohui.bridge.util.DeviceParamterUtil;
import com.xiaohui.bridge.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class PictureRemoverActivity extends AbstractActivity {

    private DisplayImageOptions options;
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
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_no_picture)
                .showImageForEmptyUri(R.drawable.icon_no_picture)
                .showImageOnFail(R.drawable.icon_no_picture)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(true)
                .build();
        imageLoader = ImageLoader.getInstance();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        photoList = getIntent().getStringArrayListExtra(Keys.Content);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return newInstance(photoList.get(position));
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

    private ImageFragment newInstance(String url) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Keys.Content, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    class ImageFragment extends Fragment {

        private ImageView ivImage;
        private ImageView ivNoImage;
        private ProgressBar pbLoading;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_image_loader, container, false);
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
            ivNoImage = (ImageView) view.findViewById(R.id.iv_no_image);
            pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

            String url = "file://" + getArguments().getString(Keys.Content);
            imageLoader.displayImage(url, ivImage, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    ivNoImage.setVisibility(View.GONE);
                    ivImage.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ivNoImage.setVisibility(View.VISIBLE);
                    ivImage.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ivNoImage.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceParamterUtil.getScreenPixelsWidth(),
                            ((loadedImage.getHeight() * DeviceParamterUtil.getScreenPixelsWidth()) / loadedImage.getWidth()));
                    ivImage.setLayoutParams(params);
                    ivImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    ivNoImage.setVisibility(View.VISIBLE);
                    ivImage.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                }
            });
            return view;
        }
    }
}
