package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import backend.Bonuses;
import backend.ImageLoader;
import backend.LevelFilter;
import backend.LevelLoader;
import backend.SquareState;
import backend.Useful;
import conf.Configuration;
import exceptions.ConfigFileError;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1903142717890981086L;
	private GameView view = new GameView();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("Soubor");
	private JMenu edit = new JMenu("Upravit");
	private JMenu help = new JMenu("Nápověda");
	private JLabel score = new JLabel();
	private static Settings preloadedSettingsDialog;
	private static JFileChooser preloadedMapChooser = new JFileChooser();
	
	private JMenuItem newGame = new JMenuItem("Nová hra");
	private JMenuItem loadMap = new JMenuItem("Načíst mapu");
	private JMenuItem pause = new JMenuItem("Pozastavit hru");
	private JMenuItem resume = new JMenuItem("Pokračovat ve hře");
	private JMenuItem settings = new JMenuItem("Nastavení");
	private JMenuItem endGame = new JMenuItem("Ukončit hru");
	private JMenuItem about = new JMenuItem("O Programu");
	
	private List<Point> lastMap = new ArrayList<Point>();
	
	
	public GameWindow() {
		this.setTitle("SeaSnake v0.1");
		
		ImageLoader.loadImages();
		
		preloadedMapChooser.setCurrentDirectory(new File(Useful.getActualPath()));
		LevelFilter filter = new LevelFilter();
		preloadedMapChooser.setFileFilter(filter);
		
		try {
			preloadedSettingsDialog = new Settings(this);
			preloadedSettingsDialog.setModal(true);
		} catch (ConfigFileError e) {
			JOptionPane.showMessageDialog(GameWindow.this,
					"Načítání dat z konfiguračního souboru selhalo!",
					"Upozornění", JOptionPane.ERROR_MESSAGE);
		}
		
		createMenu();
		initializeGui();
		
		this.setPreferredSize(new Dimension(700, 500));
		
		pack();
	}
	
	private void loadConfig(boolean gameRunning) {
		try {
			view.setSnakeSpeed(Configuration.SPEED.toInteger());
			if(!gameRunning) {
				view.setSnakeLength(Configuration.START_LENGTH.toInteger() + 1);
			}
			
			Bonuses.reset();
			Bonuses.addBonus(SquareState.SMALL_FOOD, 100, 60);

			if(Configuration.TURTLES_ENABLED.toInteger() == 1) {
				Bonuses.addBonus(SquareState.BIG_FOOD,
						Configuration.TURTLE_FREQUENCY.toInteger(),
						Configuration.TURTLE_DURATION.toInteger());
			}
			if(Configuration.STARFISH_ENABLED.toInteger() == 1) {
				Bonuses.addBonus(SquareState.SHRINKER,
						Configuration.STARFISH_FREQUENCY.toInteger(),
						Configuration.STARFISH_DURATION.toInteger());
			}
		}
		catch (ConfigFileError e) {
			JOptionPane.showMessageDialog(GameWindow.this,
					"Načítání dat z konfiguračního souboru selhalo!",
					"Upozornění", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void initializeGui() {
		this.setLayout(new BorderLayout());
		this.add(score, BorderLayout.SOUTH);
		this.add(view, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(600, 400));
		
		view.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getPropertyName().equals("score")) {
					setScore((Integer)arg0.getNewValue());
				}
			}
		});
		
		view.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getPropertyName().equals("endgame")) {
					JOptionPane.showMessageDialog(GameWindow.this,
							"Konec hry! Získal(a) jsi " + arg0.getNewValue() + " bodů.",
							"Upozornění", 1);
					endGame.setEnabled(false);
					newGame.setEnabled(true);
					resume.setEnabled(false);
					pause.setEnabled(false);
					loadMap.setEnabled(true);
					view.setWalls(lastMap);
				}
			}
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	
	private void setScore(int score) {
		this.score.setText("Skóre: " + ((Integer)score).toString());
	}
	
	private void createMenu() {
		this.setJMenuBar(menuBar);
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(help);
		
		file.setMnemonic(KeyEvent.VK_S);
		edit.setMnemonic(KeyEvent.VK_U);
		help.setMnemonic(KeyEvent.VK_N);		
		

		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		file.add(newGame);

		loadMap.setMnemonic(KeyEvent.VK_M);
		loadMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		file.add(loadMap);
		
		pause.setMnemonic(KeyEvent.VK_P);
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		file.add(pause);
		pause.setEnabled(false);
		

		resume.setMnemonic(KeyEvent.VK_R);
		resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		file.add(resume);
		resume.setEnabled(false);
		

		endGame.setMnemonic(KeyEvent.VK_U);
		endGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		file.add(endGame);
		endGame.setEnabled(false);
		

		settings.setMnemonic(KeyEvent.VK_N);
		edit.add(settings);
		
		
		about.setMnemonic(KeyEvent.VK_O);
		help.add(about);
		
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(GameWindow.this,
						"<html>Program vytvořil " +
						"Ondřej Bockschneider<br>jako závěrečný projekt<br>" +
						"předmětu Úvod do programování v Javě 4.</html>",
						"O programu", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		loadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				preloadedMapChooser.showOpenDialog(GameWindow.this);
				
				File file = preloadedMapChooser.getSelectedFile();
				
				if(file != null) {
					try {
						lastMap = LevelLoader.loadFile(file.getAbsolutePath());
						view.setWalls(lastMap);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(GameWindow.this,
								"Nepodařilo se načíst mapu.",
								"Upozornění", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pauseGame();
			}
		});
		
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resumeGame();
			}
		});
		
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadConfig(false);
				view.startGame();
				newGame.setEnabled(false);
				endGame.setEnabled(true);
				pause.setEnabled(true);
				loadMap.setEnabled(false);
			}
		});

		endGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					endGame();
				} catch (InterruptedException e) {
					JOptionPane.showMessageDialog(GameWindow.this,
							"Chyba běhu: " + e.toString(),
							"Upozornění", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(view.isGameRunning()) {
					pauseGame();
				}
				
				preloadedSettingsDialog.setVisible(true);
				
				loadConfig(true);
			}
		});
	}
	
	private void endGame() throws InterruptedException {
		view.endGame();
		endGame.setEnabled(false);
		newGame.setEnabled(true);
		resume.setEnabled(false);
		pause.setEnabled(false);
		loadMap.setEnabled(true);
		view.setWalls(lastMap);
	}
	
	private void pauseGame() {
		try {
			pause.setEnabled(false);
			resume.setEnabled(true);
			view.setGamePaused(true);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(GameWindow.this,
					"Chyba běhu: " + e.toString(),
					"Upozornění", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void resumeGame() {
		try {
			resume.setEnabled(false);
			pause.setEnabled(true);
			view.setGamePaused(false);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(GameWindow.this,
					"Chyba běhu: " + e.toString(),
					"Upozornění", JOptionPane.ERROR_MESSAGE);
		}

	}
}
