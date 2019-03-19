package org.pineapple.server;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.MalformedMessageException;
import org.pineapple.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MBXManager is a class that manage the ".mbx" files, that contains a mailbox associated to one user.
 */
public class MBXManager {
	
	@NotNull
	private String username;
	@NotNull
	private File file;
	
	/**
	 * Constructor that try to create the file.
	 * @param username The username associated to the user.
	 */
	public MBXManager(@NotNull String username) {
		setUsername(username);
		file = new File(getFilePath());
		try {
			createFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* METHODS */
	
	/**
	 * Create the file associated to the instance of this class. If the file already exists, do nothing.
	 * @throws IOException Throw when the file cannot be created.
	 */
	private void createFile() throws IOException {
		//noinspection ResultOfMethodCallIgnored
		file.createNewFile();
	}
	
	@NotNull
	@Contract(pure = true)
	public static String getPath() {
		return new File("res/").getAbsolutePath();
	}
	
	@NotNull
	@Contract(pure = true)
	public static String getFileExtension(boolean withDot) {
		return (withDot ? "." : "") + "mbx";
	}
	
	@NotNull
	@Contract(pure = true)
	public String getFilePath() {
		String path = getPath();
		if (!path.endsWith("/"))
			path += "/";
		return new File(path + getUsername() + getFileExtension(true)).getAbsolutePath();
	}
	
	/**
	 * Convert a list of messages to their string representation using the POP3 standards.
	 * @param messages The list of messages. The list can be empty.
	 * @return Return the representation of the messages as string.
	 */
	@NotNull
	public static String convertMessagesToString(@NotNull List<Message> messages) {
		StringBuilder content = new StringBuilder();
		
		for (Message m : messages)
			content.append(m.toString()).append("******\r\n");
		
		return content.toString();
	}
	
	/**
	 * Read the content of the file as a string.
	 * @return The content of the file.
	 */
	@NotNull
	private String readRaw() {
		FileReader fr = null;
		BufferedReader br = null;
		
		StringBuilder content = new StringBuilder();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String line;
			for ((line = br.readLine()) != null)
				content.append(line);
			
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
	 * @param content The string to write in the file.
	 */
	@NotNull
	private void writeRaw(@NotNull String content) {
		try (PrintStream out = new PrintStream(new FileOutputStream(file, false))) {
			out.print(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the messages in the file.
	 * @return The messages
	 * @throws MalformedMessageException Throw when cannot parsed the file.
	 */
	@NotNull
	public ArrayList<Message> read() throws MalformedMessageException {
		String raw = readRaw();
		return null;
	}
	
	/* GETTER & SETTER */
	
	@NotNull
	@Contract(pure = true)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(@NotNull String username) {
		this.username = username;
	}
	
	@NotNull
	public File getFile() {
		return file;
	}
	
	/* OVERRIDES */
	
	@Override
	@Contract(value = "null -> false", pure = true)
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MBXManager)) return false;
		MBXManager that = (MBXManager) o;
		return getUsername().equals(that.getUsername()) &&
				getFile().equals(that.getFile());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUsername(), getFile());
	}
	
	@Override
	public String toString() {
		return "MBXManager{" +
				"username='" + username + '\'' +
				", file=" + file.getAbsolutePath() +
				'}';
	}
}
