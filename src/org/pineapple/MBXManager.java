package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// TODO: Add encryption/decryption of the file using encrypted password.

/**
 * MBXManager is a class that manage the ".mbx" files, that contains a mailbox associated to one user.
 */
public class MBXManager {
	
	/**
	 * The user's name, also used as a filename.
	 */
	@NotNull
	private String username;
	
	/**
	 * The encrypted password (using MD5 algorithm).
	 */
	@NotNull
	private String password;
	
	/**
	 * The file to the user's mailbox file.
	 */
	@NotNull
	private File file;
	
	/**
	 * Constructor that try to create the file.
	 * @param username The username associated to the user.
	 */
	public MBXManager(@NotNull String username, @NotNull String password, boolean passwordEncrypted) {
		setUsername(username);
		setPassword(password, passwordEncrypted);
		file = new File(getFilePath());
		try {
			createFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Filepath : " + file.getAbsolutePath());
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
	 * Return the number of bytes of the file.
	 * @return The number of bytes of the file associated to the user mailbox.
	 */
	public long getFileSize() {
		return file.length();
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
		String encrypted = FUtils.readRaw(file);
		return MD5.decrypt(getPassword(), encrypted);
	}
	
	/**
	 * Write on the file the raw string. It is in overwrite mode.
	 * @param content The string to write in the file.
	 */
	@NotNull
	private void writeRaw(@NotNull String content) {
		String encrypted = MD5.encrypt(getPassword(), content);
		FUtils.writeRaw(file, encrypted);
	}
	
	/**
	 * Read the messages in the file.
	 * @return The messages
	 */
	@NotNull
	public ArrayList<Message> read() {
		String raw;
		try {
			raw = readRaw();
		} catch (IllegalArgumentException e) {
			throw new UserException("User \"" + getUsername() + "\" does not exist.", e);
		}
		
		// Split according to the line "******" that seperate two messages
		String[] emails = raw.split(Message.SEPARATOR_REGEX);
		ArrayList<Message> messages = new ArrayList<>(emails.length);
		
		for (String email : emails)
			if (!email.trim().replaceAll("\\r\\n", "").isEmpty())
				messages.add(Message.parse(email));
		
		return messages;
	}
	
	/**
	 * Write the given list of messages in the list.
	 * @param newMessages The list of messages to write in the file associated to the user.
	 * @param overwrite If `true`, delete the old messages to save only the new ones in `newMessages`. If `false`,
	 *                  append the new messages in the file. By default, the value is `false`.
	 */
	public void write(@NotNull List<Message> newMessages, boolean overwrite) {
		// Get the old messages
		ArrayList<Message> oldMessages;
		if (!overwrite)
			oldMessages = read();
		else
			oldMessages = new ArrayList<>();
		
		// Create the list containing the old and the new messages
		ArrayList<Message> messages = new ArrayList<>();
		messages.addAll(oldMessages);
		messages.addAll(newMessages);
		
		// Compute the representation as string, with "******" as a separator
		StringBuilder content = new StringBuilder();
		
		for (Message m : messages)
			content.append(m.buildMessage()).append(Message.SEPARATOR).append("\r\n");
		
		// Write it in the file
		writeRaw(content.toString());
	}
	
	/**
	 * Write the given list of messages in the list by appending them (the old messages are conserved in the file).
	 * @param newMessages The list of messages to write in the file associated to the user.
	 */
	public void write(@NotNull List<Message> newMessages) {
		write(newMessages, false);
	}
	
	/**
	 * Delete the given messages from the file. Keep the others in the file.
	 * <b>Example:</b>
	 * <pre>
	 *     mbx.delete(m1, m2);
	 *     mbx.delete(m3);
	 * </pre>
	 * @param messages The list of messages to delete.
	 */
	public void delete(@NotNull Message... messages) {
		ArrayList<Message> oldMessages = read();
		oldMessages.removeAll(Arrays.asList(messages));
		write(oldMessages, true);
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
	
	/**
	 * Return the encrypted password.
	 * @return The encrypted password.
	 */
	@NotNull
	public String getPassword() {
		return password;
	}
	
	/**
	 * Set the encrypted password
	 * @param password Encrypted password
	 */
	public void setPassword(@NotNull String password) {
		this.password = password;
	}
	
	/**
	 * Set the password to the new value. Encrypt the password using MD5 algorithm if `isPasswordEncrypted` is false.
	 * @param password The password to set. It can be encrypted or not.
	 * @param isPasswordEncrypted If `true`, set the password to the new value. Otherwise, encrypt the password using
	 *                            MD5 algorithm and set the password to the computed value.
	 */
	public void setPassword(@NotNull String password, boolean isPasswordEncrypted) {
		if (isPasswordEncrypted)
			setPassword(password);
		else
			setPassword(MD5.hash(password));
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
