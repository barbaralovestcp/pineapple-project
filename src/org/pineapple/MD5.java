package org.pineapple;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

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
			
			StringBuilder sb = new StringBuilder();
			for (byte datum : data)
				sb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Encrypt the string `content` by using the md5 hash string `key`.
	 * @param key The MD5 hash to encrypt `content`.
	 * @param content The string to encrypt using `key`.
	 * @return The encrypted string.
	 */
	@NotNull
	public static String encrypt(@NotNull byte[] key, @NotNull String content) {
		// TODO: See https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc-not-mobile-plat/32583766
		try {
			key = Arrays.copyOf(key, 16);
			Key k = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, k);
			
			return Base64.getMimeEncoder().encodeToString(cipher.doFinal(content.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Encrypt the string `content` by using the md5 hash string `key`.
	 * @param key The MD5 hash to encrypt `content`.
	 * @param content The string to encrypt using `key`.
	 * @return The encrypted string.
	 */
	@NotNull
	public static String encrypt(@NotNull String key, @NotNull String content) {
		return encrypt(key.getBytes(StandardCharsets.UTF_8), content);
	}
	
	/**
	 * Decrypt the string `encrypted` by using the md5 hash string `key`.
	 * @param key The MD5 hash to encrypt `content`.
	 * @param encrypted The encrypted string to decrypt.
	 * @return The decrypted string.
	 */
	@NotNull
	public static String decrypt(@NotNull byte[] key, @NotNull String encrypted) {
		try {
			key = Arrays.copyOf(key, 16);
			Key k = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, k);
			
			byte[] content = cipher.doFinal(Base64.getMimeDecoder().decode(encrypted.trim().getBytes()));
			
			return new String(content);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Decrypt the string `encrypted` by using the md5 hash string `key`.
	 * @param key The MD5 hash to encrypt `content`.
	 * @param encrypted The encrypted string to decrypt.
	 * @return The decrypted string.
	 */
	@NotNull
	public static String decrypt(@NotNull String key, @NotNull String encrypted) {
		return decrypt(key.getBytes(StandardCharsets.UTF_8), encrypted);
	}
}
