package backend;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Square extends JComponent {
	private static final long serialVersionUID = -9108246705892449960L;
	
	private SquareState state = SquareState.EMPTY;
	private int squareSize;
	private int live = 0;

	public Square() {
		setSquareSize(40);
	}

	public int getSquareSize() {
		return squareSize;
	}

	public void setSquareSize(int squareSize) {
		this.setSize(squareSize, squareSize);
		this.squareSize = squareSize;
	}

	public SquareState getState() {
		return state;
	}

	public void setState(SquareState state) {
		this.state = state;
	}

	public BufferedImage getBodyImage() {
		return ImageLoader.getBodyImage();
	}

	public BufferedImage getHeadImage() {
		return ImageLoader.getHeadImage();
	}

	public BufferedImage getWallImage() {
		return ImageLoader.getWallImage();
	}
	
	public BufferedImage getBigFoodImage() {
		return ImageLoader.getBigFoodImage();
	}
	
	public int getLive() {
		return live;
	}

	public void setLive(int live) {
		if(live >= 0) {
			this.live = live;
		}
		else {
			this.live = 0;
		}
	}
	
	public BufferedImage getSmallFoodImage() {
		return ImageLoader.getSmallFoodImage();
	}
	
	public BufferedImage getShrinkerImage() {
		return ImageLoader.getShrinkerImage();
	}

	public void decLive() {
		if(live > 0) {
			live--;
		}
	}
	
	public void incLive() {
		live++;
	}

	public BufferedImage getImage() {
		switch(getState()) {
		case EMPTY:
			return null;
		case SNAKE_BODY:
			return getBodyImage();
		case SNAKE_HEAD:
			return getHeadImage();
		case WALL:
			return getWallImage();
		case BIG_FOOD:
			return getBigFoodImage();
		case SMALL_FOOD:
			return getSmallFoodImage();
		case SHRINKER:
			return getShrinkerImage();
		default:
			return null;
		}
	}
	
	public final void paintComponent(Graphics g) {
		if(state != SquareState.EMPTY && state != SquareState.WALL && live <= 0) {
			setState(SquareState.EMPTY);
		}

		Graphics2D rend = (Graphics2D) g;
		rend.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if(getImage() != null) {
			rend.drawImage(getImage(), null, 0, 0);
		}
	}
}
