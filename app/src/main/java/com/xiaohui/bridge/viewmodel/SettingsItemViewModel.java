package com.xiaohui.bridge.viewmodel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class SettingsItemViewModel implements ItemPresentationModel<SettingsViewModel.Settings> {

    private String title;
    private String subtitle;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public void updateData(SettingsViewModel.Settings settings, ItemContext itemContext) {
        this.title = settings.getTitle();
        this.subtitle = settings.getSubtitle();
    }
}
