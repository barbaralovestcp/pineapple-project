package org.pineapple;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	/**
	 * Hash a string to MD5.
	 * @param str The string to hash.
	 * @return The hash of `str`.
	 */
	@NotNull
	public static String hash(@NotNull String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			
			byte[] data = md.digest();
			
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < data.length; i++)
				sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Encrypt the string `content` by using the md5 string `key`.
	 * @param key The MD5 hash to encrypt `content`.
	 * @param content The string to encrypt using `key`.
	 * @return The encrypted string.
	 */
	public static String encrypt(@NotNull String key, @NotNull String content) {
		// TODO: See https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc-not-mobile-plat/32583766
		try {
			Cipher cipher = Cipher.getInstance("AES");
			Key k = new SecretKeySpec(key.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, k);
			
			byte[] encrypted = cipher.doFinal(content.getBytes());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
