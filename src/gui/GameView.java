package gui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import backend.Bonuses;
import backend.Direction;
import backend.ImageLoader;
import backend.Square;
import backend.SquareState;
import exceptions.ThreadClose;

public class GameView extends JComponent {
	private static final long serialVersionUID = 8090527708514083315L;
	
	public static final String SCORE = "score";
	public static final String ENDGAME = "endgame";

	private boolean gameRunning;		// určuje, zda hra běží
	private boolean gamePaused;			// určuje, zda se má had hýbat
	private int height;					// počet políček na výšku
	private int width;					// počet políček na šířku
	private int snakeLength;			// délka hada
	private int snakeSpeed;				// rychlost hada
	private int score;					// skóre
	private Direction lastSnakeDirection;		// směr hada
	// pole, která jsou součástí těla hada
	private List<Square> snakeBody;
	// objekty na hrací ploše - jídlo, bonusy, atd..
	private List<Square> objects;
	private Square fields[][];		// políčka hrací plochy
	// obrázek s pozadím
	private static Image background = ImageLoader.loadImage("res/background.jpg");
	private Refresher snakeController;	// vlákno zajišťující pohyb hada
	// poslední pozice hlavy hada - pro pauzu a pokračování hry
	private Point lastHeadPosition;
	// udržuje posloupnost příkazů hráče pro lepší odezvy hada
	private Deque<Direction> ordersQueue;
	// nápis "Pauza"
	private static BufferedImage pauseImage;
	
	public GameView() {
		initVariables();
		init();
	}
	
	// Inicializace
	// ----------------------------------------------------------------------
	
	public void initVariables() {
		gameRunning = false;
		gamePaused = false;
		height = 20;
		width = 30;
		snakeLength = 5;
		snakeSpeed = 6;
		score = -1;
		lastSnakeDirection = Direction.UP.getRandomDirection();
		ordersQueue = new LinkedList<Direction>(); 
		snakeBody = new ArrayList<Square>();
		objects = new ArrayList<Square>();
		lastHeadPosition = new Point();
		pauseImage = getTextImage("Pauza", 70, Color.white);
	}
	
	public void init() {		
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridLayout layout = new GridLayout(height, width);
		layout.setHgap(0);
		layout.setVgap(0);
		this.setLayout(layout);
		
		fields = new Square[height][width];
		
		generateGameBoard();
		addControls();
	}
	
	private void generateGameBoard() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				Square square = new Square();
				fields[i][j] = square;
				this.add(square);
			}
		}
	}
	
	private void addControls() {
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		
		getActionMap().put("up", new AbstractAction() {
			private static final long serialVersionUID = 4935340432810649261L;
			public void actionPerformed(ActionEvent arg0) {
				if(!gamePaused) setSnakeDirection(Direction.UP);
			}
		});
		
		getActionMap().put("down", new AbstractAction() {
			private static final long serialVersionUID = 2259032743627548695L;
			public void actionPerformed(ActionEvent arg0) {
				if(!gamePaused) setSnakeDirection(Direction.DOWN);
			}
		});
		
		getActionMap().put("left", new AbstractAction() {
			private static final long serialVersionUID = 4139609469336122376L;
			public void actionPerformed(ActionEvent arg0) {
				if(!gamePaused) setSnakeDirection(Direction.LEFT);
			}
		});
		
		getActionMap().put("right", new AbstractAction() {
			private static final long serialVersionUID = -7457375618287351283L;
			public void actionPerformed(ActionEvent arg0) {
				if(!gamePaused) setSnakeDirection(Direction.RIGHT);
			}
		});
	}
	
	// Sloty
	// ----------------------------------------------------------------------

	public Direction getSnakeDirection() {
		if(ordersQueue.size() > 0) {
			lastSnakeDirection = ordersQueue.peek();
			return ordersQueue.poll();
		}
		else {
			return lastSnakeDirection;
		}
	}

	public void setSnakeDirection(Direction snakeDirection) {
		if(ordersQueue.size() < 3) {
			if(ordersQueue.size() == 0) {
				if(!snakeDirection.isOpposite(lastSnakeDirection)
						&& snakeDirection != lastSnakeDirection) {
					ordersQueue.add(snakeDirection);
				}
			}
			else {
				if(!snakeDirection.isOpposite(ordersQueue.peekLast())
						&& snakeDirection != ordersQueue.peekLast()) {
					ordersQueue.add(snakeDirection);
				}				
			}
		}
	}
	
	private List<Square> getSnakeBody() {
		return snakeBody;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getSnakeLength() {
		return snakeLength;
	}

	public void setSnakeLength(int snakeLength) {
		this.snakeLength = snakeLength;
	}
		
	public boolean isGamePaused() {
		return gamePaused;
	}
	
	public Point getLastHeadPosition() {
		return lastHeadPosition;
	}

	public void setLastHeadPosition(Point lastHeadPosition) {
		this.lastHeadPosition = lastHeadPosition;
	}
	
	public void setWalls(List<Point> walls) {
		eraseWalls();
		for(Point el : walls) {
			fields[(int) el.getY()][(int) el.getX()].setState(SquareState.WALL);
		}
		repaint();
	}
	
	public void setObjects(List<Square> objects) {
		this.objects = objects;
	}

	public List<Square> getObjects() {
		return objects;
	}
	
	public void setSnakeSpeed(int snakeSpeed) {
		this.snakeSpeed = snakeSpeed;
	}

	public int getSnakeSpeed() {
		return snakeSpeed;
	}
	
	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}
	
	// Manipulace s během hry
	// ----------------------------------------------------------------------
	
	public void startGame() {
		setGameRunning(true);
		changeScore(0);
		gamePaused = false;
		snakeController = new Refresher(10, 15);
		snakeController.start();
	}
	
	public void endGame() throws InterruptedException {
		changeScore(0);
		gamePaused = true;
		snakeController.join();

		initVariables();
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				fields[i][j].setState(SquareState.EMPTY);
			}
		}

		repaint();
		setGameRunning(false);
	}
	
	public void setGamePaused(boolean gamePaused)
			throws InterruptedException {
		this.gamePaused = gamePaused;

		if(gamePaused) {
			snakeController.join();			
			setLastHeadPosition(snakeController.getHeadPosition());
		}
		else {
			Point last = getLastHeadPosition();
			snakeController = new Refresher((int)last.getX(), (int)last.getY());
			snakeController.start();
		}		
	}
	
	// Práce s tělem a hlavou hada
	// ----------------------------------------------------------------------

	private Point nextPosition(int x, int y) {
		switch(getSnakeDirection()) {
		case RIGHT:
			if(y < width - 1) y += 1;
			else y = 0;
			break;
		case UP:
			if(x > 0) x -= 1;
			else x = height - 1;
			break;
		case DOWN:
			if(x < height - 1) x += 1;
			else x = 0;
			break;
		case LEFT:
			if(y > 0) y -= 1;
			else y = width - 1;
			break;
		default:
			break;
		}
		
		return new Point(x, y);
	}
	
	private void addToSnakeBody(Square square, int live) {
		square.setLive(live);
		square.setState(SquareState.SNAKE_BODY);
		snakeBody.add(square);
	}

	private void removeSnakeBodyGarbage() {
		int size = getSnakeBody().size();
		for(int i = 0; i < size; i++) {
			Square actual = getSnakeBody().get(i); 
			if(actual.getLive() == 0) {
				snakeBody.remove(actual);
				size--;
			}
		}
	}

	private void refreshSnakeBody() {
		for(Square el : getSnakeBody()) {
			el.decLive();
		}
		removeSnakeBodyGarbage();
	}

	private void changeSnakeLength(int change) {
		setSnakeLength(getSnakeLength() + change);
		for(Square el: getSnakeBody()) {
			el.setLive(el.getLive() + change);
		}
		removeSnakeBodyGarbage();
	}

	// Práce s objekty na herní ploše
	// ----------------------------------------------------------------------
	
	private void eraseWalls() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(fields[i][j].getState() == SquareState.WALL) {
					fields[i][j].setState(SquareState.EMPTY);
				}
			}
		}		
	}
	
	private int getObjectCount(SquareState type) {
		int count = 0;
		for(Square el : getObjects()) {
			if(el.getState() == type) {
				count++;
			}
		}
		return count;
	}
	
	private void removeObject(Square obj) {
		objects.remove(obj);
	}
	
	private void removeObjectsGarbage() {
		int size = getObjects().size();
		for(int i = 0; i < size; i++) {
			Square actual = getObjects().get(i); 
			if(actual.getLive() == 0) {
				removeObject(actual);
				size--;
			}
		}
	}
	
	private void refreshObjects() {
		for(Square el : getObjects()) {
			el.decLive();
		}
		removeObjectsGarbage();
	}
	
	private void dropRandomThing() {
		Square s = getRandomField();
		Square template = Bonuses.getRandomBonus();
		
		if(template != null && (s.getState() == SquareState.EMPTY)
				&& getObjectCount(template.getState()) == 0) {
			s.setState(template.getState());
			s.setLive(template.getLive());
			objects.add(s);
		}
	}
	
	private void handleCollision(Square object) throws ThreadClose {
		SquareState objectState = object.getState();
		if(objectState == SquareState.EMPTY) {

		}
		else if(objectState == SquareState.SMALL_FOOD) {
			removeObject(object);
			changeSnakeLength(4);
			changeScore(5);
		}
		else if(objectState == SquareState.BIG_FOOD) {
			removeObject(object);
			changeSnakeLength(12);
			changeScore(20);
		}		
		else if(objectState == SquareState.SHRINKER) {
			removeObject(object);
			if(getSnakeLength() > 12) {
				changeSnakeLength(-8);
				changeScore(-2);
			}
		}
		else {
			throw new ThreadClose();
		}
	}
	
	private Square getRandomField() {
		int randomX = (int) (Math.random() * height);
		int randomY = (int) (Math.random() * width);
		return fields[randomX][randomY];
	}
	

	// Ostatní metody
	// ----------------------------------------------------------------------
	
	public void changeScore(int amount) {
		int old = getScore();
		
		if(score + amount >= 0) {
			score += amount;
		}
		else {
			score = 0;
		}
		
		firePropertyChange(SCORE, old, getScore());
	}
	
	private BufferedImage getTextImage(String text, int size, Color color) {
        Font font = new Font("Arial black", Font.PLAIN, size);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle r = layout.getPixelBounds(null, 0, 0);
        BufferedImage bi = new BufferedImage(
            r.width + 2, r.height + 1,
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        g2d.setColor(color);
        layout.draw(g2d, 0, -r.y);
        g2d.dispose();
        return bi;
    }
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if(gamePaused) {
			g2.drawImage(pauseImage, null,
					getWidth() / 2 - pauseImage.getWidth() / 2,
					getHeight() / 2 - pauseImage.getHeight() / 2);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
	public void setBounds(int x, int y, int width, int height) {
	    super.setBounds(x, y, width, height);
	    ImageLoader.scaleImages(width / this.width, height / this.height);
	}



	private class Refresher extends Thread {
		private int x;
		private int y;
		
		public Refresher(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Point getHeadPosition() {
			return new Point(x, y);
		}
		
		public void run() {
			while(!isGamePaused()) {
				dropRandomThing();

				try {
					Thread.sleep(1000 / getSnakeSpeed());
				} catch (InterruptedException e) {

				}
				
				Point next = nextPosition(x, y);
				x = (int)next.getX();
				y = (int)next.getY();
				
				Square nextObject = fields[x][y];
				
				try {
					handleCollision(nextObject);
				} catch (ThreadClose e) {
					int score = getScore();
					
					initVariables();
					for(int i = 0; i < height; i++) {
						for(int j = 0; j < width; j++) {
							fields[i][j].setState(SquareState.EMPTY);
						}
					}

					repaint();
					
					firePropertyChange(ENDGAME, -1, score);
					break;
				}
				
				addToSnakeBody(fields[x][y], getSnakeLength());
				refreshSnakeBody();
				refreshObjects();
				repaint();
			}
		}
	}
}
