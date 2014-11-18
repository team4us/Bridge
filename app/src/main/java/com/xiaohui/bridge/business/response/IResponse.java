package com.xiaohui.bridge.business.response;

import java.io.Serializable;

/**
 * Created by xiaohui on 13-12-19.
 */
public interface IResponse extends Serializable {

    //用于校验请求是否符合规则，由各个请求自己实现
    //返回值：Error
    public Error verify();

    //处理一些解析后的数据整理
    public void onParseComplete();

}
