package com.xiaohui.bridge.storage;

/**
 * <pre>
 *     Created by xiaohui on 14-6-6.
 *     缺省编码方式为未做任何编码
 * </pre>
 */
public class DefaultEncrypt implements IEncrypt {
    @Override
    public String encrypt(String originString) {
        return originString;
    }

    @Override
    public String decrypt(String fakeString) {
        return fakeString;
    }
}
