package com.xiaohui.bridge.storage;

import com.xiaohui.bridge.util.EncryptDesUtil;

/**
 * <pre>
 *     Created by xiaohui on 14-6-6.
 *     使用Des加密编码
 * </pre>
 */
public class DesEncrypt implements IEncrypt {

    private String key;

    public DesEncrypt(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String originString) {
        try {
            return EncryptDesUtil.encrypt(originString, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originString;
    }

    @Override
    public String decrypt(String fakeString) {
        try {
            return EncryptDesUtil.decrypt(fakeString, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fakeString;
    }
}
