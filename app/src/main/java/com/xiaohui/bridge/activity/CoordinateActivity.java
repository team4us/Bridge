package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.enums.EDiseaseMethod;
import com.xiaohui.bridge.component.CoordinateView;

/**
 * Created by Administrator on 2014/10/9.
 */
public class CoordinateActivity extends AbstractActivity {

    private CoordinateView coordinateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);
        setTitle("坐标选择");
        EDiseaseMethod method = (EDiseaseMethod) getIntent().getSerializableExtra(Keys.Content);
        coordinateView = (CoordinateView) findViewById(R.id.cv_coordinate);
        coordinateView.setSelectOnePoint(method == EDiseaseMethod.MethodTwo);
        PointF pointStart = getIntent().getParcelableExtra(Keys.PointStart);
        PointF pointStop = getIntent().getParcelableExtra(Keys.PointStop);
        coordinateView.setSelectPointStart(pointStart);
        coordinateView.setSelectPointStop(pointStop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coordinate_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            onDone();
        } else if (id == R.id.action_clear) {
            onClear();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDone() {
        PointF pointStart = coordinateView.getSelectPointStart();
        PointF pointStop = coordinateView.getSelectPointStop();
        if (pointStart == null) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra(Keys.PointStart, pointStart);
            intent.putExtra(Keys.PointStop, pointStop);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private void onClear() {
        coordinateView.clear();
    }
}
