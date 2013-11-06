package backend;

import java.io.File;

public class Useful {
	public static String getActualPath() {
		String path = new String();
		path = new File("").getAbsolutePath();

		return path;
	}
}
