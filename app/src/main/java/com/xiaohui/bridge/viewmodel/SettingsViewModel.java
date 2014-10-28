package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.ISettingsView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class SettingsViewModel implements HasPresentationModelChangeSupport {

    private final List<Settings> settings = Arrays.asList(new Settings("下载数据", ""), new Settings("上传数据", ""), new Settings("清理缓存", ""));
    private final PresentationModelChangeSupport changeSupport = new PresentationModelChangeSupport(this);
    private ISettingsView view;
    private Cookie cookie;
    private DatabaseHelper helper;
    private UserModel userModel;

    public SettingsViewModel(ISettingsView view, Cookie cookie, DatabaseHelper helper) {
        this.view = view;
        this.cookie = cookie;
        this.helper = helper;
        this.userModel = (UserModel) cookie.get(Keys.USER);
    }

    public String getUser() {
        return "当前用户：" + userModel.getUserName();
    }

    @ItemPresentationModel(SettingsItemViewModel.class)
    public List<Settings> getSettings() {
        return settings;
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        if (pos == 0) {
            view.onDownload();
        } else if (pos == 1) {
            upload();
        } else if (pos == 2) {
            clear();
        }
    }

    public void logout() {
        view.onLogout();
    }

    public void downloadStart() {
        settings.get(0).setSubtitle("正在下载");
        changeSupport.firePropertyChange("settings");
    }

    public void downloadSuccess() {
        settings.get(0).setSubtitle("下载成功");
        changeSupport.firePropertyChange("settings");
    }

    public void upload() {
        view.onUpload();
    }

    public void clear() {
        view.onClear();
    }

    public static class Settings {
        private String title;
        private String subtitle;

        private Settings(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
    }
}
