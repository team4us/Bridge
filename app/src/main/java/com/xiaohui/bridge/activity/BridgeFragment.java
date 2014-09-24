package com.xiaohui.bridge.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.viewmodel.BridgeViewModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeFragment extends AbstractFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BridgeFragment newInstance(String name) {
        BridgeFragment fragment = new BridgeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, name);
        fragment.setArguments(args);
        return fragment;
    }

    public BridgeFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected Object getViewModel() {
        return new BridgeViewModel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getString(ARG_SECTION_NUMBER));
    }
}
