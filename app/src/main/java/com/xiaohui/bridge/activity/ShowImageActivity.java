package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.store.KeyStore;
import com.xiaohui.bridge.util.BitmapUtil;
import com.xiaohui.bridge.view.ZoomImage.ZoomableImageView;

/**
 * 展示图片
 * Created by Administrator on 2014/10/9.
 */
public class ShowImageActivity extends AbstractActivity{
    private ZoomableImageView ivImage;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ivImage = (ZoomableImageView) findViewById(R.id.iv_image);
        picPath = getIntent().getExtras().getString(KeyStore.KeyContent);

        initToShowImage();
    }

    private void initToShowImage(){
        ivImage.setZoomEnable(true);

        if(picPath.isEmpty()){
            Toast.makeText(this, "出现未知错误！", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        ivImage.setImageBitmap(BitmapUtil.getBitmapFromFilePath(picPath));
    }
}
