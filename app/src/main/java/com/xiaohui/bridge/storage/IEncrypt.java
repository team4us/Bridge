package com.xiaohui.bridge.storage;

/**
 * Created by xiaohui on 14-6-6.
 * 编解码接口
 */
public interface IEncrypt {

    public String encrypt(String originString);

    public String decrypt(String fakeString);
}
