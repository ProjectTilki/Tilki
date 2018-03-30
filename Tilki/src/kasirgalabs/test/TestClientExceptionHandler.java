package kasirgalabs.test;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;
import kasirgalabs.ClientExceptionHandler;

public class TestClientExceptionHandler {

	
	@Test
	public void testlogAnException() {
		Exception e = new Exception("Exception Test");
		ClientExceptionHandler.logAnException(e);
		File f = new File("error.log");
		assertEquals(f.exists(),true);
        f.delete();
	}
	
	
}
