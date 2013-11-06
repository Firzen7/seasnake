package conf;

import exceptions.ConfigFileError;

/**
 * Globálně řeší různou konfiguraci programu.
 * Veškerá nastavení načítá z konfiguračního souboru.
 * @author firzen
 *
 */
public enum Configuration {
	START_LENGTH("snake", "start_length"),
	SPEED("game", "speed"),
	TURTLES_ENABLED("turtles", "enabled"),
	TURTLE_FREQUENCY("turtles", "frequency"),
	TURTLE_DURATION("turtles", "duration"),
	STARFISH_ENABLED("starfish", "enabled"),
	STARFISH_FREQUENCY("starfish", "frequency"),
	STARFISH_DURATION("starfish", "duration");
	
	String section;
	String tag;
	
	/**
	 * Konstruktor.
	 * @param sec String sekce
	 * @param tg String tag
	 */
	Configuration(String sec, String tg) {
		section = sec;
		tag = tg;
	}
	
	public void saveValue(Object value) throws ConfigFileError {
		ConfigManager.setConfigValue(section, tag, value);
	}
	
	/**
	 * Načte nastavení z konfiguračního souboru.
	 * @return String Hodnota proměnné.
	 */
	public String makeString() throws ConfigFileError {
		String s = new String();
		s = ConfigManager.getConfigValue(section, tag);
		return s;
	}
	
	/**
	 * Načte nastavení z konfiguračního souboru.
	 * @return Integer Hodnota proměnné.
	 * @throws ConfigFileError 
	 */
	public Integer toInteger() throws ConfigFileError {
		int output = 0;
		try {
			output = Integer.parseInt(this.makeString());
		} catch (NumberFormatException e) {
			throw new ConfigFileError();
		}
		return output;
	}
	
	/**
	 * Načte nastavení z konfiguračního souboru.
	 * @return Double Hodnota proměnné.
	 * @throws ConfigFileError 
	 */
	public Double toDouble() throws ConfigFileError {
		double output = 0;
		try {
			output = Double.parseDouble(this.makeString());
		} catch (NumberFormatException e) {
			throw new ConfigFileError();
		}
		return output;
	}
	
	/**
	 * Načte nastavení z konfiguračního souboru.
	 * @return Character Hodnota proměnné.
	 * @throws ConfigFileError 
	 */
	public Character toCharacter() throws ConfigFileError {
		Character output = '\0';
		try {
			output = (this.toString().charAt(0));
		} catch (IndexOutOfBoundsException e) {
			throw new ConfigFileError();
		}
		return output;
	}
}
