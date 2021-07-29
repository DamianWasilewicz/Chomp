package edu.brown.cs.student.Encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Initialize encryption handler and keypair, which will be used for encrypting and decrypting
 * passwords.
 */
public class CipherHandler {
  private static SecretKeySpec secretKey;
  private static byte[] key;
  private static final int KEYLENGTH = 16;

  /**
   * Method to set the encryption key.
   * @param myKey String used for encryption of user data.
   */
  public static void setKey(String myKey) {
    MessageDigest sha = null;
    try {
      key = myKey.getBytes("UTF-8");
      sha = MessageDigest.getInstance("SHA-1");
      key = sha.digest(key);
      key = Arrays.copyOf(key, KEYLENGTH);
      secretKey = new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to encrypt a string.
   * @param strToEncrypt The String password that needs to be encrypted.
   * @param secret The String used to encrypt the password.
   * @return The encrypted string.
   */
  public static String encrypt(String strToEncrypt, String secret) {
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    } catch (Exception e) {
      System.out.println("Error while encrypting: " + e.toString());
    }
    return null;
  }

  /**
   * Method to decrypt a string.
   * @param strToDecrypt The encrypted String that must be decrypted.
   * @param secret The String used to decrypt the encrypted String.
   * @return The decrypted String.
   */
  public static String decrypt(String strToDecrypt, String secret) {
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      System.out.println("Error while decrypting: " + e.toString());
    }
    return null;
  }
}

