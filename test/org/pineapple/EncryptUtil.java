package org.pineapple;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.FontUIResource;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptUtil {
	
	@Test
	public void encryptTestFile() throws IOException {
		encryptFile(new File("Server/res/Test.txt"), new File("Server/res/Test.mbx"), "test", false);
	}
	
	@Test
	public void encryptLeaFile() throws IOException {
		encryptFile(new File("Server/res/lea.txt"), new File("Server/res/lea.mbx"), "lea", false);
	}
	
	@Test
	public void encryptPhilippineFile() throws IOException {
		encryptFile(new File("Server/res/philippine.txt"), new File("Server/res/philippine.mbx"), "philippine", false);
	}
	
	@Test
	public void encryptElieFile() throws IOException {
		encryptFile(new File("Server/res/elie.txt"), new File("Server/res/elie.mbx"), "elie", false);
	}
	
	@Test
	public void encryptValentinFile() throws IOException {
		encryptFile(new File("Server/res/valentin.txt"), new File("Server/res/valentin.mbx"), "valentin", false);
	}
	
	public void encryptFile(@NotNull File f_txt, @NotNull File f_mbx, @NotNull String password, boolean isPasswordHashed) throws IOException {
		if (!f_txt.exists())
			throw new IOException("File doesn't exist.");
		
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
}
