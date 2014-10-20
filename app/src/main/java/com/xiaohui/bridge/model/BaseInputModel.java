package com.xiaohui.bridge.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/10/18.
 */
public class BaseInputModel implements Serializable{
    protected String imageNumber;

    protected String more;

    public void setImageNumber(String imageNumber){
        this.imageNumber = imageNumber;
    };

    public String getImageNumber(){
        return imageNumber;
    };

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
