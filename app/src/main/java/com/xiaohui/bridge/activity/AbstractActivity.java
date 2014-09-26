package com.xiaohui.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.XhApplication;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;

/**
 * Created by xhChen on 14/9/22.
 */
public abstract class AbstractActivity extends Activity {

    private ActionBar actionBar;

    public void initializeContentView(int layoutId, Object presentationModel) {
        ViewBinder viewBinder = createViewBinder();
        View rootView = viewBinder.inflateAndBind(layoutId, presentationModel);
        setContentView(rootView);
    }

    public XhApplication getGlobalApplication() {
        return (XhApplication) getApplication();
    }

    private ViewBinder createViewBinder() {
        BinderFactory binderFactory = getGlobalApplication().getBinderFactory();
        return binderFactory.createViewBinder(this);
    }

    @Override
    public ActionBar getActionBar() {
        if (actionBar == null) {
            actionBar = super.getActionBar();
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.color_099fde));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        return actionBar;
    }
}
