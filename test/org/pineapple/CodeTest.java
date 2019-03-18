package org.pineapple;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CodeTest {
	
	CodeOK okOK;
	CodeOK okSERVER_READY;
	CodeOK okMAILDROP_READY;
	CodeOK okVALID_MAILBOX;
	CodeOK okSCAN_LISTING;
	CodeOK okSTAT;
	CodeOK okRETRIEVE;
	CodeOK okDELETE;
	CodeERR errPERMISSION_DENIED;
	CodeERR errNO_MAILBOX;
	CodeERR errINVALID_PASSWORD;
	CodeERR errDEL_MESSAGE_NOT_REMOVED;
	
	@BeforeEach
	public void setup() {
		okOK = new CodeOK(CodeOK.CodeEnum.OK);
		okSERVER_READY = new CodeOK(CodeOK.CodeEnum.OK_SERVER_READY);
		okMAILDROP_READY = new CodeOK(CodeOK.CodeEnum.OK_MAILDROP_READY);
		okVALID_MAILBOX = new CodeOK(CodeOK.CodeEnum.OK_VALID_MAILBOX);
		okSCAN_LISTING = new CodeOK(CodeOK.CodeEnum.OK_SCAN_LISTING);
		okSTAT = new CodeOK(CodeOK.CodeEnum.OK_STAT);
		okRETRIEVE = new CodeOK(CodeOK.CodeEnum.OK_RETRIEVE);
		okDELETE = new CodeOK(CodeOK.CodeEnum.OK_DELETE);
		errPERMISSION_DENIED = new CodeERR(CodeERR.CodeEnum.ERR_PERMISSION_DENIED);
		errNO_MAILBOX = new CodeERR(CodeERR.CodeEnum.ERR_NO_MAILBOX);
		errINVALID_PASSWORD = new CodeERR(CodeERR.CodeEnum.ERR_INVALID_PASSWORD);
		errDEL_MESSAGE_NOT_REMOVED = new CodeERR(CodeERR.CodeEnum.ERR_DEL_MESSAGE_NOT_REMOVED);
	}
	
	@Test
	public void test_toString() {
		assertEquals("+OK", okOK.toString());
		assertEquals("+OK test POP3 server ready", okSERVER_READY.toString("test"));
		try {
			assertEquals("+OK test POP3 server ready", okSERVER_READY.toString());
			fail("Was supposed to fail.");
		} catch (NullPointerException ignored) {
		}
		
		assertEquals("+OK maildrop locked and ready", okMAILDROP_READY.toString());
		
		assertEquals("-ERR sorry no mailbox for myMailbox", errNO_MAILBOX.toString("myMailbox"));
	}
	
	@Test
	public void test_raise() {
		try {
			errPERMISSION_DENIED.raise();
			fail("The `raise()` method did not raised any exception.");
		} catch (CodeErrorException ignored) {
			// Test passed!
		}
	}
}