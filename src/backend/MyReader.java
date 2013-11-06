package backend;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Obsahuje metody pro čtení ze souboru.
 * @author firzen
 *
 */
public class MyReader {
	
	/**
	 * Přečte obsah souboru.
	 * @param path String Jméno souboru.
	 * @return String Obsah souboru.
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {		
		InputStreamReader fr = new InputStreamReader(new FileInputStream(path));

		StringBuilder buffer = new StringBuilder();
		
	    int ch;
	    do {
	    	ch = fr.read();
	    	if (ch != -1)
	    		buffer.append((char)ch);
	    } while (ch != -1);
	    fr.close();
	    
	    return buffer.toString();
	}
}