package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.Field;
import model.MoveableObject;

public class FieldPanel extends JPanel {

	private static final long serialVersionUID = -3227137700291520353L;
	private Field field;
	
	private boolean activated;

	public FieldPanel(Field field) {
		this.field = field;
		setBackground(Color.BLACK);
		setFocusable(true);
	}
	
	public boolean isActive() {
		return activated;
	}
	
	public void setActive(boolean activated) {
		this.activated = activated;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		for (MoveableObject asteroid : field.getAsteroids()) {
			asteroid.draw(g);
		}

		g.setColor(Color.RED);
		for (MoveableObject bullet : field.getBullets()) {
			bullet.draw(g);
		}

		g.setColor(Color.GREEN);
		field.getShip().draw(g);
	}
}
