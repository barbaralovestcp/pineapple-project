package org.pineapple;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserManager {
	
	public static boolean checkUserExists(@NotNull String username) {
		//TODO : tester si mailbox presente

//		HashMap<String, String> userPass = read();
//		return userPass.containsKey(username);

		ArrayList<String> users = new ArrayList<>();
		users.add("elie");
		users.add("valentin");
		users.add("lea");
		users.add("philippine");
		return users.contains(username);
//		return new File("res/" + username).exists();
	}
	
	public static boolean checkPassword(@NotNull String username, @NotNull String password, boolean isPasswordEncrypted) {
		if (!isPasswordEncrypted)
			password = MD5.hash(password);
		
		HashMap<String, String> userPass = read();
		
		if (!userPass.containsKey(username))
			return false;
		
		return userPass.getOrDefault(username, "").equals(password);
	}
	
	public static void checkPasswordOrRaise(@NotNull String username, @NotNull String password, boolean isPasswordEncrypted)
			throws InvalidPasswordException{
		if (!checkPassword(username, password, isPasswordEncrypted))
			throw new InvalidPasswordException();
	}
	
	public static void put(@NotNull String username, @NotNull String password, boolean isPasswordEncrypted) {
		if (!isPasswordEncrypted)
			password = MD5.hash(password);
		
		HashMap<String, String> map = new HashMap<>(1);
		map.put(username, password);
		write(map, false);
	}
	
	/**
	 * Return the password (encrypted) associated to the user `username`.
	 * @param username The username.
	 * @return Return the encrypted password.
	 */
	@Nullable
	public static String getEncryptedPassword(@NotNull String username) {
		HashMap<String, String> userPass = read();
		return userPass.getOrDefault(username, null);
	}
	
	@NotNull
	private static HashMap<String, String> read() {
		File f_passwords = getFile();
		String content = FUtils.readRaw(f_passwords);
		
		HashMap<String, String> userPass = new HashMap<>();
		
		String[] lines = content.split("\r\n|\n");
		
		for (String line : lines) {
			String[] parts = line.split("\\s*:\\s*");
			String[] pure = line.split(":");
			
			if (parts.length >= 2)
				userPass.put(parts[0], String.join(":", Arrays.copyOfRange(pure, 1, pure.length)));
		}
		
		return userPass;
	}
	
	private static void write(@NotNull HashMap<String, String> userPass) {
		File f_passwords = getFile();
		
		StringBuilder content = new StringBuilder();
		
		for (String key : userPass.keySet())
			content.append(key).append(':').append(userPass.get(key)).append("\n");
		
		FUtils.writeRaw(f_passwords, content.toString());
	}
	
	private static void write(@NotNull HashMap<String, String> userPass, boolean overwrite) {
		if (overwrite)
			write(userPass);
		else {
			HashMap<String, String> map = read();
			map.putAll(userPass);
			write(map);
		}
	}
	
	private static File getFile() {
		File f_passwords = new File("res/passwords.ini");
		try {
			//noinspection ResultOfMethodCallIgnored
			f_passwords.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f_passwords;
	}
}
