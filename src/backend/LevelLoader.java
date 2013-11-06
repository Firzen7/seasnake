package backend;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
	public static List<Point> loadFile(String filename) throws IOException {
		List<Point> output = new ArrayList<Point>();
		
		String content = MyReader.readFile(filename);
		int size = content.length();
		
		int x = 0;
		int y = 0;
		
		for(int i = 0; i < size; i++) {
			Character actual = content.charAt(i);

			if(actual == '1') {
				output.add(new Point(x, y));
			}

			if(actual == '\n') {
				y++;
				x = 0;
			}
			else {
				x++;
			}			
		}
		
		return output;
	}
}
