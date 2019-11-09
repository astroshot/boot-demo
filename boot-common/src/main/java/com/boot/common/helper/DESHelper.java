package com.boot.common.helper;

import com.sun.crypto.provider.SunJCE;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

public class DESHelper {

    private static String DES = "DES";

    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;


    private DESHelper(String keyStr) throws Exception {
        Security.addProvider(new SunJCE());
        Key key = getKey(keyStr.getBytes());
        encryptCipher = Cipher.getInstance(DES);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance(DES);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    private Key getKey(byte[] arr) {
        // create an empty byte array of capacity 8
        byte[] arrB = new byte[8];
        // cut off raw byte array to 8
        for (int i = 0; i < arr.length && i < arrB.length; i++) {
            arrB[i] = arr[i];
        }
        // generate secret key
        return new SecretKeySpec(arrB, DES);
    }

    public static DESHelper getInstance(String keyStr) throws Exception {
        return new DESHelper(keyStr);
    }

    public static String byteArr2HexStr(byte[] arr) {
        int iLen = arr.length;
        StringBuilder buffer = new StringBuilder(iLen << 1);
        for (int b : arr) {
            int intTmp = b;
            // 把负数转换为正数
            // convert negative to positive
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                buffer.append("0");
            }
            buffer.append(Integer.toString(intTmp, 16));
        }
        return buffer.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // byte is present by two char
        byte[] arrOut = new byte[iLen >> 1];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * Encrypt byte array
     *
     * @param arr raw byte array
     * @return encrypted byte array
     * @throws Exception
     */
    public byte[] encrypt(byte[] arr) throws Exception {
        return encryptCipher.doFinal(arr);
    }

    /**
     * Encrypt String
     *
     * @param rawStr input String
     * @return encrypted String
     * @throws Exception
     */
    public String encrypt(String rawStr) throws Exception {
        return byteArr2HexStr(encrypt(rawStr.getBytes()));
    }

    public byte[] decrypt(byte[] arr) throws Exception {
        return decryptCipher.doFinal(arr);
    }

    public String decrypt(String rawStr) throws Exception {
        return new String(decrypt(hexStr2ByteArr(rawStr)));
    }
}
