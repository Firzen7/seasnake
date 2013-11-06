package backend;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class LevelFilter extends FileFilter {

	public boolean accept(File f) {
	    return f.getName().toLowerCase().endsWith(".lev");
	}

	public String getDescription() {
        return "Mapy (*.lev)";
    }
}

