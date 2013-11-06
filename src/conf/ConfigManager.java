package conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import exceptions.ConfigFileError;

/**
 * Načítá konfiguraci ze souboru pomocí knihovny ini4j.
 * @author firzen
 *
 */
public class ConfigManager {
	private static String configFile;

	static {
		String userHome = System.getProperty("user.home");
		String path = userHome + File.separator + ".config"
				+ File.separator + "SeaSnake";
		configFile = path + File.separator + "settings.ini";

		File p = new File(path);
		p.mkdirs();

		File p2 = new File(configFile);

		if(!p2.exists()) {
			String defaultConfig = "other" + File.separator + "std_config.ini";
			try {
				copyFile(defaultConfig, configFile);
			} catch (IOException e) {
				// zde je to ok, program zobrazí chybovou hlášku během načítání
				// konfigurace
			}
		}
	}
	
	private static void copyFile(String one, String two) throws IOException {
		File source = new File(one);
		File dest = new File(two);
		
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}

	/**
	 * Načte data z konfiguračního souboru.
	 * @param section String Hledaná sekce.
	 * @param tag String Hledaný tag.
	 * @return String Hodnota proměnné.
	 * @throws ConfigFileError 
	 */
	public static String getConfigValue(String section, String tag)
			throws ConfigFileError {
		Wini ini = new Wini();

		try {
			ini = new Wini(new File(configFile));
		} catch (Exception e) {
			throw new ConfigFileError();
		}

		return ini.get(section, tag);
	}

	public static void setConfigValue(String section, String tag, Object value)
			throws ConfigFileError {
		Wini ini;
		try {
			ini = new Wini(new File(configFile));
		} catch (InvalidFileFormatException e) {
			throw new ConfigFileError();
		} catch (IOException e) {
			throw new ConfigFileError();
		}

		ini.put(section, tag, value);

		try {
			ini.store();
		} catch (IOException e) {
			throw new ConfigFileError();
		}
	}
}
