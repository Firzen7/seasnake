package backend;

import java.util.ArrayList;
import java.util.List;

public class Bonuses {
	private static List<Bonus> bonuses = new ArrayList<Bonus>();
	
	public static void reset() {
		bonuses = new ArrayList<Bonus>();
	}
	
	public static void addBonus(SquareState type, int freq, int dur) {
		bonuses.add(new Bonus(type, freq, dur));
	}
	
	public static Square getRandomBonus() {
		Square output = new Square();
		
		if(bonuses.size() > 0) {
			Bonus candidate = bonuses.get((int) (Math.random() * bonuses.size()));
			
			if(Math.random() >= (1 - (double)(candidate.getFrequency() / (double)100))) {
				output.setState(candidate.getType());
				output.setLive(candidate.getDuration());
				return output;
			}
		}
			
		return null;
	}
	
	public static class Bonus {
		SquareState type;
		int frequency;
		int duration;
		
		public Bonus(SquareState type, int freq, int dur) {
			this.type = type;
			frequency = freq;
			duration = dur;
		}

		public SquareState getType() {
			return type;
		}

		public int getFrequency() {
			return frequency;
		}

		public int getDuration() {
			return duration;
		}
	}
}
