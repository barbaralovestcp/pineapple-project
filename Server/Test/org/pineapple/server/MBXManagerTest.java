package org.pineapple.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pineapple.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MBXManagerTest {
	
	MBXManager mbxManager;
	
	@BeforeEach
	void setup() {
		mbxManager = new MBXManager("Test");
	}
	
	@Test
	void getPath() {
		String path = MBXManager.getPath();
		System.out.println(path);
		if (path.endsWith("\\"))
			path = path.substring(0, path.length()-2);
		String[] parts = path.split("\\\\");
		assertEquals(parts[parts.length-1], "res");
	}
	
	@Test
	void getFilePath() {
		String path = mbxManager.getFilePath();
		System.out.println(path);
		if (path.endsWith("\\"))
			path = path.substring(0, path.length()-2);
		String[] parts = path.split("\\\\");
		assertEquals(parts[parts.length-1], "Test.mbx");
	}
	
	@Test
	void convertMessagesToString() {
		ArrayList<Message> messages = new ArrayList<>();
		
		messages.add(new Message()
				.setDate("Tue, 9 Sep 2008 09:33:17 +0200") // Epoch: 1220952797
				.setSender("Stephane Bortzmeyer <stephane@sources.org>")
				.setReceiver("ubuntu-fr@lists.ubuntu.com")
				.setSubject("[Son] Silence complet sur mon ESS ES1978 Maestro 2E")
				.setMessageId("<20080909073317.GA23390@sources.org>")
				.setMessage("Le son, avec Linux, c'est vraiment pénible.\r\n" +
						"\r\n" +
						"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet."));
		
		messages.add(new Message()
				.setDate("Tue, 9 Sep 2008 09:33:17 +0200")
				.setSender("Stephane Bortzmeyer <stephane@sources.org>")
				.setReceiver("ubuntu-fr@lists.ubuntu.com")
				.setSubject("[Son] Silence complet sur mon ESS ES1978 Maestro 2E")
				.setMessageId("<20080909073317.GA23390@sources.org>")
				.setMessage("Le son, avec Linux, c'est vraiment pénible.\r\n" +
						"\r\n" +
						"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet."));
		
		assertEquals("From: Stephane Bortzmeyer <stephane@sources.org>\r\n" +
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
						"******\r\n",
				MBXManager.convertMessagesToString(messages));
	}
}