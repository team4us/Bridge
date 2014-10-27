package com.xiaohui.bridge.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by xiaohui on 14-1-2.
 */
public class EncryptDesUtil {

    private static final String KEY = "kEHrDooxWHCWtfeSxvDvgqZq";
    private static final String ALGORITHM = "DES";

    public final static String decrypt(String data, String seed) throws Exception {
        return new String(decrypt(hex2byte(data.getBytes()), (KEY + seed).getBytes()));
    }

    public final static String encrypt(String data, String seed) throws Exception {
        return byte2hex(encrypt(data.getBytes(), (KEY + seed).getBytes()));
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        return cipher.doFinal(data);
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String sTmp;
        for (int n = 0; n < b.length; n++) {
            sTmp = (Integer.toHexString(b[n] & 0XFF));
            if (sTmp.length() == 1)
                hs = hs + "0" + sTmp;
            else
                hs = hs + sTmp;
        }
        return hs.toUpperCase();
    }
}
