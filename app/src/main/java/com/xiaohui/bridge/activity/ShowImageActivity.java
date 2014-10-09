package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showimage_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_image) {
            Intent intent = new Intent();
            intent.putExtra(KeyStore.KeyContent, picPath);
            setResult(KeyStore.ResultCodeDelete, intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
