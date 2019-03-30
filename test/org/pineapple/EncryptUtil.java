package org.pineapple;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.plaf.FontUIResource;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptUtil {
	
	@Test
	public void encryptTestFile() throws IOException {
		encryptFile(new File("res/Test.txt"), new File("res/Test.mbx"), "test", false);
	}
	
	@Test
	public void encryptLeaFile() throws IOException {
		encryptFile(new File("res/lea.txt"), new File("res/lea.mbx"), "lea", false);
	}
	
	@Test
	public void encryptPhilippineFile() throws IOException {
		encryptFile(new File("res/philippine.txt"), new File("res/philippine.mbx"), "philippine", false);
	}
	
	@Test
	public void encryptElieFile() throws IOException {
		encryptFile(new File("res/elie.txt"), new File("res/elie.mbx"), "elie", false);
	}
	
	@Test
	public void encryptValentinFile() throws IOException {
		encryptFile(new File("res/valentin.txt"), new File("res/valentin.mbx"), "valentin", false);
	}
	
	public void encryptFile(@NotNull File f_txt, @NotNull File f_mbx, @NotNull String password, boolean isPasswordHashed) throws IOException {
		if (!f_txt.exists())
			throw new IOException("File doesn't exist: " + f_txt.getAbsolutePath());
		
		if (!isPasswordHashed)
			password = MD5.hash(password);
		
		// Read content (decrypted)
		String content = FUtils.readRaw(f_txt);
		
		// Encrypt content
		String encrypted = MD5.encrypt(password, content);
		
		// Write content (encrypted)
		FUtils.writeRaw(f_mbx, encrypted);
		
		// Decrypt the encrypted content and compare it to the old one.
		assertEquals(content, MD5.decrypt(password, FUtils.readRaw(f_mbx)));
	}
	
	@Test
	public void addUsers() {
		UserManager.put("elie", "elie", false);
		UserManager.put("lea", "lea", false);
		UserManager.put("philippine", "philippine", false);
		UserManager.put("valentin", "valentin", false);
		
		assertTrue(UserManager.checkPassword("elie", "elie", false));
		assertTrue(UserManager.checkPassword("lea", "lea", false));
		assertTrue(UserManager.checkPassword("philippine", "philippine", false));
		assertTrue(UserManager.checkPassword("valentin", "valentin", false));
		assertFalse(UserManager.checkPassword("valentin", "lea", false));
		assertFalse(UserManager.checkPassword("philippine", "elie", false));
		assertFalse(UserManager.checkPassword("ceciestuntest", "blabla", false));
	}
	
	@Test
	public void printSupportedCiphers() throws IOException {
		String[] ciphers = ((SSLSocket) SSLSocketFactory.getDefault().createSocket()).getSupportedCipherSuites();
		
		for (String cipher : ciphers)
			System.out.println(cipher);
	}
}
