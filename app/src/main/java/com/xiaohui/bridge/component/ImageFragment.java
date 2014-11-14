package com.xiaohui.bridge.component;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.xiaohui.bridge.util.DeviceParameterUtil;

/**
 * Created by xiaohui on 14/11/14.
 */
public class ImageFragment extends Fragment {
    private ImageView ivImage;
    private ImageView ivNoImage;
    private ProgressBar pbLoading;
    private DisplayImageOptions options;

    public static ImageFragment newInstance(String url) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Keys.Content, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ImageFragment() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_no_picture)
                .showImageForEmptyUri(R.drawable.icon_no_picture)
                .showImageOnFail(R.drawable.icon_no_picture)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_image_loader, container, false);
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        ivNoImage = (ImageView) view.findViewById(R.id.iv_no_image);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        final int screenWidth = DeviceParameterUtil.getScreenPxWidth(view.getContext());

        String url = "file://" + getArguments().getString(Keys.Content);
        ImageLoader.getInstance().displayImage(url, ivImage, options, new ImageLoadingListener() {
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,
                        ((loadedImage.getHeight() * screenWidth) / loadedImage.getWidth()));
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
