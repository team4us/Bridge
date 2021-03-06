package com.xiaohui.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.XhApplication;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.Store;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;

/**
 * Created by xhChen on 14/9/22.
 */
public abstract class AbstractActivity extends FragmentActivity {

    private ActionBar actionBar;

    public void setContentView(int layoutId, Object presentationModel) {
        setContentView(inflateView(layoutId, presentationModel));
    }

    public View inflateView(int layoutId, Object presentationModel) {
        ViewBinder viewBinder = createViewBinder();
        return viewBinder.inflateAndBind(layoutId, presentationModel);
    }

    protected XhApplication getGlobalApplication() {
        return (XhApplication) getApplication();
    }

    protected Cookie getCookie() {
        return getGlobalApplication().getCookie();
    }

    protected Store getStore() {
        return getGlobalApplication().getStore();
    }

    private ViewBinder createViewBinder() {
        BinderFactory binderFactory = getGlobalApplication().getBinderFactory();
        return binderFactory.createViewBinder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public ActionBar getActionBar() {
        if (actionBar == null) {
            actionBar = super.getActionBar();
            actionBar.setLogo(getResources().getDrawable(R.drawable.icon_company));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        return actionBar;
    }

    protected void setTitle(String text) {
        if (getActionBar() != null) {
            getActionBar().setTitle(text);
        }
    }
}
