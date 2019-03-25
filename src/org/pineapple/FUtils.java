package org.pineapple;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class FUtils {
	
	/**
	 * Read the content of the given file as a string.
	 * @param file The file.
	 * @return The content of the file.
	 */
	@NotNull
	public static String readRaw(@NotNull File file) {
		FileReader fr = null;
		BufferedReader br = null;
		
		StringBuilder content = new StringBuilder();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				content.append(line).append("\r\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ignored) {}
			}
			
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException ignored) {}
			}
		}
		
		return content.toString();
	}
	
	/**
	 * Write on the file the raw string. It is in overwrite mode.
	 * @param file The file.
	 * @param content The string to write in the file.
	 */
	@NotNull
	public static void writeRaw(@NotNull File file, @NotNull String content) {
		try (PrintStream out = new PrintStream(new FileOutputStream(file, false))) {
			out.print(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
