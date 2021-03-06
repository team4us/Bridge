package com.xiaohui.bridge.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaohui.bridge.XhApplication;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.Store;

import org.robobinding.MenuBinder;
import org.robobinding.ViewBinder;

/**
 * Created by xiaohui on 14-9-24.
 */
public abstract class AbstractFragment extends Fragment {

    protected ViewBinder createViewBinder() {
        return getGlobalApplication().getBinderFactory().createViewBinder(getActivity());
    }

    protected XhApplication getGlobalApplication() {
        return (XhApplication) getActivity().getApplicationContext();
    }

    protected Cookie getCookie() {
        return getGlobalApplication().getCookie();
    }

    protected Store getStore() {
        return getGlobalApplication().getStore();
    }

    protected MenuBinder createMenuBinder(Menu menu, MenuInflater menuInflater) {
        return getGlobalApplication().getBinderFactory().createMenuBinder(menu, menuInflater, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createViewBinder().inflateAndBind(getLayoutId(), getViewModel());
    }

    protected ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    protected abstract int getLayoutId();

    protected abstract Object getViewModel();

    protected void setTitle(String title) {
        getActionBar().setTitle(title);
    }
}
