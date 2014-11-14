package com.xiaohui.bridge.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.enums.EDiseaseMethod;
import com.xiaohui.bridge.component.CoordinateView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/9.
 */
public class CoordinateActivity extends AbstractActivity {

    private final String[] surfaces = {"全部", "左腹板", "底板", "右腹板"};
    private List<PointF> points = new ArrayList<PointF>();
    private List<Integer[]> shapes = new ArrayList<Integer[]>();
    private CoordinateView coordinateView;
    private boolean needOnePoint;
    private Spinner spSurface;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            spSurface.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        coordinateView.setShapes(points, shapes);
                    } else {
                        coordinateView.setShapes(points, shapes.subList(position - 1, position));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);
        initPoints();
        EDiseaseMethod method = (EDiseaseMethod) getIntent().getSerializableExtra(Keys.Content);
        PointF pointStart = getIntent().getParcelableExtra(Keys.PointStart);
        PointF pointStop = getIntent().getParcelableExtra(Keys.PointStop);
        needOnePoint = (method == EDiseaseMethod.MethodTwo);
        coordinateView = (CoordinateView) findViewById(R.id.cv_coordinate);
        coordinateView.setSelectOnePoint(needOnePoint);
        coordinateView.setShapes(points, shapes);
        coordinateView.setSelectPointStart(pointStart);
        coordinateView.setSelectPointStop(pointStop);
        initActionBar();
    }

    private void initPoints() {
        points.add(new PointF(0, 1));
        points.add(new PointF(0, 0.5f));
        points.add(new PointF(0, -0.5f));
        points.add(new PointF(0, -1));
        points.add(new PointF(20, 1));
        points.add(new PointF(20, 0.5f));
        points.add(new PointF(20, -0.5f));
        points.add(new PointF(20, -1));

        shapes.add(new Integer[]{1, 2, 6, 5, 1});
        shapes.add(new Integer[]{2, 3, 7, 6, 2});
        shapes.add(new Integer[]{3, 4, 8, 7, 3});
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayShowCustomEnabled(true);
        View view = View.inflate(this, R.layout.view_coordinate_action_bar, null);
        actionBar.setCustomView(view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        spSurface = (Spinner) view.findViewById(R.id.sp_surface);
        spSurface.setAdapter(new ArrayAdapter<String>(this, R.layout.view_spinner_item, surfaces));
        handler.sendEmptyMessageDelayed(0, 100);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("请选择1个点");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }
        if (!needOnePoint && pointStop == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("需要选择2个点");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Keys.PointStart, pointStart);
        intent.putExtra(Keys.PointStop, pointStop);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onClear() {
        coordinateView.clear();
    }
}
