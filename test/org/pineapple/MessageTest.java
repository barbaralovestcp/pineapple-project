package org.pineapple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
	
	@BeforeEach
	void setUp() {
	}
	
	@Test
	void parse() {
		String raw = "From: Stephane Bortzmeyer <stephane@sources.org>\r\n" +
				"To: ubuntu-fr@lists.ubuntu.com\r\n" +
				"Subject: [Son] Silence complet sur mon ESS ES1978 Maestro 2E\r\n" +
				"Date: Tue, 9 Sep 2008 09:33:17 +0200\r\n" +
				"Message-ID: <20080909073317.GA23390@sources.org>\r\n" +
				"\r\n" +
				"Le son, avec Linux, c'est vraiment pénible.\r\n" +
				"\r\n" +
				"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet.\r\n";
		
		Message message = new Message()
				.setDate("Tue, 9 Sep 2008 09:33:17 +0200") // Epoch: 1220952797
				.setSender("Stephane Bortzmeyer <stephane@sources.org>")
				.setReceiver("ubuntu-fr@lists.ubuntu.com")
				.setSubject("[Son] Silence complet sur mon ESS ES1978 Maestro 2E")
				.setMessageId("<20080909073317.GA23390@sources.org>")
				.setMessage("Le son, avec Linux, c'est vraiment pénible.\r\n" +
						"\r\n" +
						"J'ai un Dell Inspiron 7500 dont le son ne marche pas du tout. Silence complet.");
		
		assertEquals(message, Message.parse(raw));
	}
}