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
 * This class is in charge of all encryption and decryption needed in this application,
 * as well as relevent processes.
 */

public class Encrypter {

    private final static String encryptionScheme = "Blowfish/CBC/PKCS5Padding";

    /**
     * Returns a string with is encrypted using the Blowfish/CBC/PKCS5Padding algorithm.
     * @param key
     * @param plaintext
     * @param IV
     * @return ciphertext
     */
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

    /**
     * Decrypts the inserted ciphertext, and returns a plaintext String. Decryption is done using
     * Blowfish/CBC/PKCS5Pattern
     * @param key
     * @param ciphertext
     * @param IV
     * @return plaintext
     */
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

    /**
     * Generates a pseudorandom IV consisting of 8 bytes.
     * Copied from aioobe's post from Dec 25 2010: http://stackoverflow.com/questions/4531799/generate-8-byte-number-in-java
     * @return IV
     */
    public static String generateIV() {
        SecureRandom sr = new SecureRandom();
        byte[] byteIV = new byte[8];
        sr.nextBytes(byteIV);
        String IV = bytesToHex(byteIV);
        return IV;
    }

    /**
     * Converts byte arrays to Hex Strings.
     * Source: http://dexxtr.com/post/57145943236/blowfish-encrypt-and-decrypt-in-java-android
     * @param data
     * @return str
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
     * Converts Hex Strings to byte arrays.
     * Sournce: http://stackoverflow.com/questions/15948662/decrypting-in-java-with-blowfish,
     * By Sanchit
     * @param str
     * @return byte[] buffer
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
