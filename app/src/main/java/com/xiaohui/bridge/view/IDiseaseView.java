package com.xiaohui.bridge.view;

/**
 * Created by xhChen on 14/9/27.
 */
public interface IDiseaseView {
    public void takePhoto();
    public void pickPictures();
    public void takeVoice();
    public void takeMovie();
    public void updateMethodView(boolean isOther, int method);
}
