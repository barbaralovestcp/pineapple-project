package org.pineapple.server;

import org.junit.jupiter.api.*;
import org.pineapple.MBXManager;
import org.pineapple.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MBXManagerTest {
	
	MBXManager mbxManager;
	Message message;
	String messageContent;
	
	@BeforeEach
	void setup() {
		mbxManager = new MBXManager("Test", "test", false);
		message = new Message()
				.setDate("Tue, 9 Sep 2008 09:33:17 +0200") // Epoch: 1220952797
				.setSender("Stephane Bortzmeyer <stephane@sources.org>")
				.setReceiver("ubuntu-fr@lists.ubuntu.com")
				.setSubject("[Son] Silence complet sur mon ESS ES1978 Maestro 2E")
				.setMessageId("<20080909073317.GA23390@sources.org>")
				.setMessage("Le son, avec Linux, c'est vraiment pénible.\r\n" +
						"\r\n" +
						"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet.");
		messageContent = "From: Stephane Bortzmeyer <stephane@sources.org>\r\n" +
				"To: ubuntu-fr@lists.ubuntu.com\r\n" +
				"Subject: [Son] Silence complet sur mon ESS ES1978 Maestro 2E\r\n" +
				"Date: Tue, 9 Sep 2008 09:33:17 +0200\r\n" +
				"Message-ID: <20080909073317.GA23390@sources.org>\r\n" +
				"\r\n" +
				"Le son, avec Linux, c'est vraiment pénible.\r\n" +
				"\r\n" +
				"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet.\r\n" +
				"******\r\n" +
				"From: Stephane Bortzmeyer <stephane@sources.org>\r\n" +
				"To: ubuntu-fr@lists.ubuntu.com\r\n" +
				"Subject: [Son] Silence complet sur mon ESS ES1978 Maestro 2E\r\n" +
				"Date: Tue, 9 Sep 2008 09:33:17 +0200\r\n" +
				"Message-ID: <20080909073317.GA23390@sources.org>\r\n" +
				"\r\n" +
				"Le son, avec Linux, c'est vraiment pénible.\r\n" +
				"\r\n" +
				"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet.\r\n" +
				"******\r\n";
	}
	
	@Test
	@Order(1)
	void getPath() {
		String path = MBXManager.getResPath();
		System.out.println(path);
		if (path.endsWith("\\"))
			path = path.substring(0, path.length()-2);
		String[] parts = path.split("\\\\");
		assertEquals(parts[parts.length-1], "res");
	}
	
	@Test
	@Order(2)
	void getFilePath() {
		String path = mbxManager.getFilePath();
		System.out.println(path);
		if (path.endsWith("\\"))
			path = path.substring(0, path.length()-2);
		String[] parts = path.split("\\\\");
		assertEquals(parts[parts.length-1], "Test.mbx");
	}
	
	@Test
	@Order(3)
	void convertMessagesToString() {
		ArrayList<Message> messages = new ArrayList<>();
		
		messages.add(message);
		
		messages.add(message);
		
		assertEquals(messageContent,
				MBXManager.convertMessagesToString(messages));
	}
	
	@Test
	@Order(4)
	void readRaw() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		// Get the private method using Java Reflection.
		Method m_readRaw = MBXManager.class.getDeclaredMethod("readRaw");
		m_readRaw.setAccessible(true);
		
		// Invoke the private method and compare the result
		String output = (String) m_readRaw.invoke(mbxManager);
		
		assertEquals(messageContent, output);
	}
	
	@Test
	@Order(5)
	void read() {
		ArrayList<Message> messages = mbxManager.read();
		
		assertEquals(2, messages.size());
		assertEquals(messages.get(0), message);
		assertEquals(messages.get(1), message);
	}
}