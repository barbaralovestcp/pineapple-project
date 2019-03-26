package org.pineapple;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class UserManager {
	
	public static boolean checkUserExists(@NotNull String username) {
		return new File("res/" + username).exists();
	}
	
	public static boolean checkPassword(@NotNull String username, @NotNull String password, boolean isPasswordEncrypted) {
		if (!isPasswordEncrypted)
			password = MD5.hash(password);
		
		HashMap<String, String> userPass = read();
		
		return userPass.getOrDefault(username, "").equals(password);
	}
	
	@NotNull
	private static HashMap<String, String> read() {
		File f_passwords = new File("res/passwords.txt");
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
		File f_passwords = new File("res/passwords.txt");
		
		StringBuilder content = new StringBuilder();
		
		for (String key : userPass.keySet())
			continue;
		
		// TODO continue
	}
}
