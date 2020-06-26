package com.technomedialab.battelysurvey;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {
    // アルゴリズム
    private static final String ALGORITHM = "Blowfish";
    private static final String MODE = "Blowfish/CBC/PKCS5Padding";
    private static final String IV = "abcdefgh"; // Blowfishの場合は64ビット（8バイト）なので

    /**
     * 引数の文字列を暗号化する(Base64対応)
     *
     * @param value     暗号化対象文字列
     * @param secretKey 暗号化キー
     * @return String 暗号化済み文字列
     */
    private String encrypt(String value, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        byte[] values = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(values, Base64.DEFAULT);
    }

    /**
     * 引数のBase64された文字列を復号化する
     *
     * @param value     復号化対象文字列
     * @param secretKey 復号化キー
     * @return String 復号化済み文字列
     */
    private String decrypt(String value, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] values = Base64.decode(value, Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        return new String(cipher.doFinal(values));
    }

    /**
     * 文字列保存
     *
     * @param key   キー
     * @param value 値
     * @throws Exception 例外
     */
    public String saveString(String key, String value) throws Exception {
        if (key == null || key.length() == 0) {
            throw new Exception("キーが空です。");
        }
        if (value == null) {
            throw new Exception("値が空です。");
        }
        // 暗号化
        String encValue = encrypt(value, key);
        if (encValue == null) {
            throw new Exception("暗号化に失敗しました。");
        }

        Log.d("暗号化",encValue);
        return encValue;
        // 保存

    }
    /**
     * 復号化
     *
     * @param key キー
     * @return String キーに紐づいた値(無ければnullを返す)
     * @throws Exception 例外
     */
    public String getString(String key,String value) throws Exception {
        // 値取得
        if (key == null || key.length() == 0) {
            throw new Exception("キーが空です。");
        }

        if (value == null) {
            return null;
        }

        // 復号
        String decValue = decrypt(value, key);
        if (decValue == null) {
            throw new Exception("復号に失敗しました。");
        }
        return decValue;
    }

    /**
     * 暗号化
     *
     * @param key キー
     * @param value 暗号化したい値
     * @return String 暗号化したキー
     * @throws Exception 例外
     */
    public String setString(String key,String value) throws Exception {
        String encValue;
        try {
            CryptUtil cryptUtil = new CryptUtil();
            encValue = cryptUtil.saveString(key, value); // 暗号化
            Log.e("暗号化", encValue);
        } catch (Exception e) {
            Log.e("暗号化エラー", e.getMessage(), e);
            return null;
        }
        return encValue;
    }

}
