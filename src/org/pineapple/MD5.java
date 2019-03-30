package org.pineapple;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Function;
import java.util.function.IntFunction;

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
	
	/**
	 * Set a cipher suite for the given SSL (Server) Socket `socket`.
	 * @param socket The socket to set the cipher suite. It is either of type `SSLSocket` or `SSLServerSocket`.
	 * @param cipherNameContains Optional parameter. If given, only the cipher that contains the string
	 *                           `cipherNameContains` will be added in the cipher suite for `socket`. By default, the
	 *                           cipher "TLS_RSA_WITH_AES_256_CBC_SHA" is chosen.
	 * @return Return the cipher list given to `socket`.
	 * @throws InvalidCipherSuiteException Thrown if no cipher has been found.
	 * @throws ClassCastException Thrown if `socket` is not of type `SSLSocket` or `SSLServerSocket`
	 */
	@NotNull
	private static String[] setCipherSuiteOnObject(@NotNull Object socket, @NotNull String cipherNameContains) throws InvalidCipherSuiteException, ClassCastException {
		if (!(socket instanceof SSLSocket) && !(socket instanceof SSLServerSocket))
			throw new ClassCastException("Only 'SSLSocket' and 'SSLServerSocket' are authorized.");
		
		ArrayList<String> cipherSuite = new ArrayList<>();
		String[] authCipherSuites = null;
		
		if (socket instanceof SSLSocket)
			authCipherSuites = ((SSLSocket) socket).getSupportedCipherSuites();
		else
			authCipherSuites = ((SSLServerSocket) socket).getSupportedCipherSuites();
		
		for (String authCipherSuite : authCipherSuites)
			if (authCipherSuite.toLowerCase().contains(cipherNameContains.toLowerCase()))
				cipherSuite.add(authCipherSuite);
		
		if(cipherSuite.size() == 0)
			throw new InvalidCipherSuiteException("The size of the cipher suite cannot be 0.");
		
		String[] cipherSuiteStringArray = Arrays.stream(cipherSuite.toArray()).map(o -> (String) o).toArray(String[]::new);
		
		if (socket instanceof SSLSocket)
			((SSLSocket) socket).setEnabledCipherSuites(cipherSuiteStringArray);
		else
			((SSLServerSocket) socket).setEnabledCipherSuites(cipherSuiteStringArray);
		
		return cipherSuiteStringArray;
	}
	/**
	 * Set a cipher suite for the given SSL (Server) Socket `socket`. By default, the cipher
	 * "TLS_RSA_WITH_AES_256_CBC_SHA" is chosen.
	 * @param socket The socket to set the cipher suite. It is either of type `SSLSocket` or `SSLServerSocket`.
	 * @return Return the cipher list given to `socket`.
	 * @throws InvalidCipherSuiteException Thrown if no cipher has been found.
	 * @throws ClassCastException Thrown if `socket` is not of type `SSLSocket` or `SSLServerSocket`
	 */
	@NotNull
	private static String[] setCipherSuiteOnObject(@NotNull Object socket) throws InvalidCipherSuiteException, ClassCastException {
		return setCipherSuiteOnObject(socket, "TLS_RSA_WITH_AES_256_CBC_SHA");
	}
	
	/**
	 * Set a cipher suite for the given SSL Socket `socket`.
	 * @param socket The socket to set the cipher suite. It is necessary to generate the list of supported ciphers.
	 * @param cipherNameContains Optional parameter. If given, only the cipher that contains the string
	 *                           `cipherNameContains` will be added in the cipher suite for `socket`. By default, the
	 *                           cipher "TLS_RSA_WITH_AES_256_CBC_SHA" is chosen.
	 * @return Return the cipher list given to `socket`.
	 * @throws InvalidCipherSuiteException Thrown if no cipher has been found.
	 */
	@NotNull
	public static String[] setCipherSuite(@NotNull SSLSocket socket, @NotNull String cipherNameContains) throws InvalidCipherSuiteException {
		return setCipherSuiteOnObject(socket, cipherNameContains);
	}
	/**
	 * Set a cipher suite for the given SSL Socket `socket`.
	 * @param socket The socket to set the cipher suite. It is necessary to generate the list of supported ciphers.
	 * @param cipherNameContains Optional parameter. If given, only the cipher that contains the string
	 *                           `cipherNameContains` will be added in the cipher suite for `socket`. By default, the
	 *                           cipher "TLS_RSA_WITH_AES_256_CBC_SHA" is chosen.
	 * @return Return the cipher list given to `socket`.
	 * @throws InvalidCipherSuiteException Thrown if no cipher has been found.
	 */
	@NotNull
	public static String[] setCipherSuite(@NotNull SSLServerSocket socket, @NotNull String cipherNameContains) throws InvalidCipherSuiteException {
		return setCipherSuiteOnObject(socket, cipherNameContains);
	}
	/**
	 * Set a cipher suite for the given SSL Socket `socket`. By default, the cipher "TLS_RSA_WITH_AES_256_CBC_SHA" is
	 * chosen.
	 * @param socket The socket to set the cipher suite. It is necessary to generate the list of supported ciphers.
	 * @throws InvalidCipherSuiteException Thrown if no cipher has been found.
	 */
	@NotNull
	public static String[] setCipherSuite(@NotNull SSLSocket socket) throws InvalidCipherSuiteException {
		return setCipherSuiteOnObject(socket);
	}
	/**
	 * Set a cipher suite for the given SSL Socket `socket`. By default, the cipher "TLS_RSA_WITH_AES_256_CBC_SHA" is
	 * chosen.
	 * @param socket The socket to set the cipher suite. It is necessary to generate the list of supported ciphers.
	 * @throws InvalidCipherSuiteException Thrown if no cipher has been found.
	 */
	@NotNull
	public static String[] setCipherSuite(@NotNull SSLServerSocket socket) throws InvalidCipherSuiteException {
		return setCipherSuiteOnObject(socket);
	}
}
