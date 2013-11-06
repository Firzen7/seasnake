package backend;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;
	
	public boolean isOpposite(Direction d) {
		return (this == UP && d == DOWN) || (this == DOWN && d == UP)
				|| (this == LEFT && d == RIGHT) || (this == RIGHT && d == LEFT);
	}
	
	public Direction getRandomDirection() {
		int num = (int) (Math.random() * 4);
		
		switch(num) {
		case 0: return Direction.UP;
		case 1: return Direction.LEFT;
		case 2: return Direction.RIGHT;
		default: return Direction.DOWN;
		}
	}
}
