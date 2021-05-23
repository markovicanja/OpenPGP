package etf.openpgp.ma170420ddv170455d;

import java.io.IOException;
import org.bouncycastle.openpgp.PGPException;

public class Main {

	public static void main(String[] args) {
		try {
			new GUI(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PGPException e) {
			e.printStackTrace();
		}
	}

}
