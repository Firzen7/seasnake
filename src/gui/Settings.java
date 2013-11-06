package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import conf.Configuration;
import exceptions.ConfigFileError;

public class Settings extends JDialog {
	private static final long serialVersionUID = 4755471415460680864L;

	private JPanel mainPanel = new JPanel();
	private Dimension spinnerMaxSize = new Dimension(700, 25);
	private JLabel speed = new JLabel("Rychlost hry: ");
	private JLabel startLength = new JLabel("Počáteční délka hada: ");
	private static String frequencyToolTip = "Nastavuje frekvenci výskytu " +
			"bonusu v procentech.";
	private static String durationToolTip = "Nastavuje dobu, po kterou bude bonus" +
			"zobrazen.";
	
	JPanel upperPanel = new JPanel();
	JSpinner speedSpinner;
	JSpinner startLengthSpinner;
	JSpinner turFreq = new JSpinner();
	JSpinner turDur = new JSpinner();
	JSpinner starFreq = new JSpinner();
	JSpinner starDur = new JSpinner();
	JCheckBox tur;
	JCheckBox star;
	
	
	public Settings(JFrame parentFrame) throws ConfigFileError {
		super(parentFrame);
		
		setLayout();
		createUpperComponents();
		createLowerComponents();
		createButtons();
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(300, 220));
		this.setTitle("Nastavení hry");
//		this.setResizable(false);
		
		pack();

		this.setLocation(parentFrame.getWidth() / 2 - this.getWidth() / 2,
				parentFrame.getHeight() / 2 - this.getHeight() / 2);
	}
	
	private void setLayout() {
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		this.add(mainPanel);
		
		BoxLayout layout2 = new BoxLayout(upperPanel, BoxLayout.Y_AXIS);
		upperPanel.setLayout(layout2);
		mainPanel.add(upperPanel, BorderLayout.CENTER);
	}
	
	private void createUpperComponents() throws ConfigFileError {
		JPanel upper = new JPanel();
		
		GroupLayout layout = new GroupLayout(upper);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		upper.setLayout(layout);
		
		int speedValue = Configuration.SPEED.toInteger();
		int lengthValue = Configuration.START_LENGTH.toInteger();
		
		SpinnerNumberModel model1 = new SpinnerNumberModel(speedValue, 1, 30, 1);
		speedSpinner = new JSpinner(model1);
		
		SpinnerNumberModel model2 = new SpinnerNumberModel(lengthValue, 3, 600, 1);
		startLengthSpinner = new JSpinner(model2);
		
		speedSpinner.setMaximumSize(spinnerMaxSize);
		startLengthSpinner.setMaximumSize(spinnerMaxSize);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(speed)
						.addComponent(speedSpinner))
						.addGroup(layout.createParallelGroup()
								.addComponent(startLength)
								.addComponent(startLengthSpinner)));
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(speed)
						.addComponent(startLength))
						.addGroup(layout.createParallelGroup()
								.addComponent(speedSpinner)
								.addComponent(startLengthSpinner)));
		upperPanel.add(upper);
	}
	
	private void createLowerComponents() throws ConfigFileError {
		GroupBox lower = new GroupBox();
		GroupLayout layout = new GroupLayout(lower);
		layout.setAutoCreateGaps(true);
		lower.setLayout(layout);
		lower.setTitle("Nastavení bonusů");
		
		int initTurtleFreq = Configuration.TURTLE_FREQUENCY.toInteger();
		int initTurtleDur = Configuration.TURTLE_DURATION.toInteger();
		int initStarFreq = Configuration.STARFISH_FREQUENCY.toInteger();
		int initStarDur = Configuration.STARFISH_DURATION.toInteger();
		
		boolean turEnabled = Configuration.TURTLES_ENABLED.toInteger() == 1;
		boolean starEnabled = Configuration.STARFISH_ENABLED.toInteger() == 1;
		
		tur = new JCheckBox();
		tur.setText("Želvy");
		tur.setSelected(turEnabled);
		SpinnerNumberModel model1
			= new SpinnerNumberModel(initTurtleFreq, 1, 100, 1);
		turFreq.setModel(model1);
		turFreq.setMaximumSize(spinnerMaxSize);
		turFreq.setToolTipText(frequencyToolTip);

		SpinnerNumberModel model2
			= new SpinnerNumberModel(initTurtleDur, 1, 200, 1);
		turDur.setModel(model2);
		turDur.setMaximumSize(spinnerMaxSize);
		turDur.setToolTipText(durationToolTip);

		star = new JCheckBox();
		star.setText("Hvězdice");
		star.setSelected(starEnabled);

		SpinnerNumberModel model3
			= new SpinnerNumberModel(initStarFreq, 1, 100, 1);
		starFreq.setModel(model3);
		starFreq.setMaximumSize(spinnerMaxSize);
		starFreq.setToolTipText(frequencyToolTip);
		
		SpinnerNumberModel model4
			= new SpinnerNumberModel(initStarDur, 1, 200, 1);
		starDur.setModel(model4);
		starDur.setMaximumSize(spinnerMaxSize);
		starDur.setToolTipText(durationToolTip);
		
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(tur)
						.addComponent(turFreq)
						.addComponent(turDur))
				.addGroup(layout.createParallelGroup()
						.addComponent(star)
						.addComponent(starFreq)
						.addComponent(starDur)));
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(tur)
						.addComponent(star))
				.addGroup(layout.createParallelGroup()
						.addComponent(turFreq)
						.addComponent(starFreq))
				.addGroup(layout.createParallelGroup()
						.addComponent(turDur)
						.addComponent(starDur)));
		
		upperPanel.add(lower);
	}
	
	private void createButtons() {
		JPanel buttons = new JPanel();
		BoxLayout layout = new BoxLayout(buttons, BoxLayout.X_AXIS);
		buttons.setLayout(layout);
		
		JPanel spacer = new JPanel();
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Storno");
		ok.setSelected(true);
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Configuration.SPEED.saveValue(speedSpinner.getValue());
					Configuration.START_LENGTH.saveValue(startLengthSpinner.getValue());
					Configuration.TURTLE_DURATION.saveValue(turDur.getValue());
					Configuration.TURTLE_FREQUENCY.saveValue(turFreq.getValue());
					Configuration.STARFISH_DURATION.saveValue(starDur.getValue());
					Configuration.STARFISH_FREQUENCY.saveValue(starFreq.getValue());
					Configuration.TURTLES_ENABLED.saveValue(tur.isSelected() ? 1 : 0);
					Configuration.STARFISH_ENABLED.saveValue(star.isSelected() ? 1 : 0);
					dispose();
				} catch (ConfigFileError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		buttons.add(spacer);
		buttons.add(cancel);
		buttons.add(ok);
		
		mainPanel.add(buttons, BorderLayout.SOUTH);
	}
}
