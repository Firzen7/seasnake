package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class GroupBox extends JPanel {
	private static final long serialVersionUID = -3388427427138420719L;
	private String title = new String();
	
	public GroupBox() {
		setTitle("title");
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Border etched = BorderFactory.createLineBorder(Color.BLACK);
		TitledBorder border = BorderFactory.createTitledBorder(etched, title);
		this.setBorder(border);
		this.title = title;
	}
}
