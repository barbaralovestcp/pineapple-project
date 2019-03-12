package org.pineapple;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CodeTest {
	
	CodeOKServerReady codeOKServerReady;
	CodeERRPermissionDenied codeERRPermissionDenied;
	CodeERRNoMailbox codeERRNoMailbox;
	
	@BeforeEach
	public void setup() {
		codeOKServerReady = new CodeOKServerReady("test");
		codeERRPermissionDenied = new CodeERRPermissionDenied();
		codeERRNoMailbox = new CodeERRNoMailbox("myMailbox");
	}
	
	@Test
	public void test_toString() {
		System.out.println(codeOKServerReady.toString());
		assertEquals("+OK test POP3 server ready", codeOKServerReady.toString());
		
		System.out.println(codeERRPermissionDenied.toString());
		assertEquals("-ERR Permission denied", codeERRPermissionDenied.toString());
		
		System.out.println(codeERRNoMailbox.toString());
		assertEquals("-ERR Sorry no mailbox for myMailbox", codeERRNoMailbox.toString());
	}
	
	@Test
	public void test_raise() {
		try {
			codeERRPermissionDenied.raise();
			fail("The `raise()` method did not raised any exception.");
		} catch (CodeErrorException ignored) {
			// Test passed!
		}
		try {
			codeERRNoMailbox.raise();
			fail("The `raise()` method did not raised any exception.");
		} catch (CodeErrorException ignored) {
			// Test passed!
		}
	}
}