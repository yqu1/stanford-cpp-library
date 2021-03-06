package stanford.spl;

import java.io.*;
import java.lang.reflect.*;
import acm.graphics.*;
import acm.util.*;

/**
 * 
 * @author Marty Stepp
 * @version 2015/08/11
 */
public class GBufferedImage_updateAllPixels extends JBECommand {
	// gbufferedimage.load("foobar.png");
	public void execute(TokenScanner paramTokenScanner, JavaBackEnd paramJavaBackEnd) {
		paramTokenScanner.verifyToken("(");
		String id = nextString(paramTokenScanner);
		paramTokenScanner.verifyToken(",");
		// String base64 = nextString(paramTokenScanner);
		String base64 = "";
		try {
			BufferedReader reader = new BufferedReader(getTokenScannerReader(paramTokenScanner));
			// throw away ", \"" char
			while (reader.read() != '"') {
				// empty
			}
			base64 = reader.readLine();
			
			// trim off "" from start/end and ) from end
			if (base64.endsWith("\")")) {
				base64 = base64.substring(0, base64.length() - 2);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		// paramTokenScanner.verifyToken(")");
		
		GObject gobj = paramJavaBackEnd.getGObject(id);
		if (gobj != null && gobj instanceof GBufferedImage) {
			GBufferedImage img = (GBufferedImage) gobj;
			try {
				img.fromStringBase64(base64);
				// img.fromStringCompressed(base64);
			} catch (java.io.IOException ioe) {
				System.out.println(ioe);
			}
		}
	}
	
	private Reader getTokenScannerReader(TokenScanner scanner) {
		try {
			Field inputField = TokenScanner.class.getDeclaredField("input");
			inputField.setAccessible(true);
			return (Reader) inputField.get(scanner);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
}
