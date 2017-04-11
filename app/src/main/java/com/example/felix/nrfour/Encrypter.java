package com.example.felix.nrfour;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Felix on 07/04/2017.
 */

public class Encrypter {

    private final static String encryptionScheme = "Blowfish/CBC/PKCS5Padding";

    public static String encrypter (String key, String plaintext, String IV) {
        byte[] KEY = new byte[0];
        try {
            KEY = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SecretKeySpec keySpec = new SecretKeySpec(KEY, "Blowfish");
        Cipher cipher = null;
        String ciphertext = null;
        try {
            cipher = Cipher.getInstance(encryptionScheme);
            System.out.println("IV: "+IV);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(hexToBytes(IV)));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));
            ciphertext = bytesToHex(encrypted);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ciphertext;
    }

    public static String decrypter (String key, String ciphertext, String IV) {
        byte[] KEY = new byte[0];
        try {
            KEY = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String plainText = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(KEY, "Blowfish");
            Cipher cipher = Cipher.getInstance(encryptionScheme);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(hexToBytes(IV)));
            byte[] decrypted = cipher.doFinal(hexToBytes(ciphertext));
            plainText = new String(decrypted);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return plainText;
    }

    public static String generateIV() {
        SecureRandom sr = new SecureRandom();
        byte[] byteIV = new byte[8];
        sr.nextBytes(byteIV);
        String IV = bytesToHex(byteIV);
        return IV;
    }

    /**
     * Need to find the author
     * @param data
     * @return
     */
    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        String str = "";

        for (int i = 0; i < data.length; i++) {
            if((data[i] & 0xFF) < 16) {
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            }
            else {
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
            }


        }
        return str;
    }

    /**
     * Need to find the author
     * @param str
     * @return
     */
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }

    }

}
