import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import model.Field;
import model.HighScores;
import model.MoveableObject;
import view.FieldPanel;
import view.GameFrame;

public class App {

	private static final String PLAY = "Play";
	private static final String EXIT = "Exit";
	private static final String EASY = "Easy";
	private static final String NORMAL = "Normal";
	private static final String HARD = "Hard";
	private static final String CLEAR_SCORES = "Clear Scores";

	// the main objects
	private static Field field;
	private static HighScores highScores;
	private static FieldPanel fieldPanel;
	private static GameFrame gameFrame;

	public static void main(String[] args) {

		// setup the main objects
		field = new Field();
		highScores = new HighScores();
		fieldPanel = new FieldPanel(field);
		gameFrame = new GameFrame(fieldPanel);

		// setup the menu
		JMenuBar menubar = setupMenu(menuListener);
		gameFrame.setJMenuBar(menubar);

		// setup the frame-animation
		Timer timer = new Timer(40, animationListener);
		timer.start();

		// setup the ship motion key event handler
		fieldPanel.addKeyListener(keyListener);

		// load the highscores and update the gameFrame display
		highScores.load("highscores.txt");
		gameFrame.updateHighscores(highScores.getTopScores(20));

		// show the gameFrame in the center of the desktop screen
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
	}

	private static JMenuBar setupMenu(ActionListener listener) {

		// create the necessary menu items and menus
		JMenuBar menubar = new JMenuBar();

		JMenu gameMenu = new JMenu("Game");
		JMenuItem playItem = new JMenuItem(PLAY);
		JMenuItem exitItem = new JMenuItem(EXIT);

		gameMenu.add(playItem);
		gameMenu.add(exitItem);

		JMenu settingsMenu = new JMenu("Settings");

		JMenu difficultyMenu = new JMenu("Difficulty Level");
		ButtonGroup levelGroup = new ButtonGroup();
		JCheckBoxMenuItem easyDifficultyItem = new JCheckBoxMenuItem(EASY);
		JCheckBoxMenuItem normalDifficultyItem = new JCheckBoxMenuItem(NORMAL);
		JCheckBoxMenuItem hardDifficultyItem = new JCheckBoxMenuItem(HARD);

		normalDifficultyItem.setSelected(true); // the default setting

		// this button group ensures that only one check box is selected
		levelGroup.add(easyDifficultyItem);
		levelGroup.add(normalDifficultyItem);
		levelGroup.add(hardDifficultyItem);

		difficultyMenu.add(easyDifficultyItem);
		difficultyMenu.add(normalDifficultyItem);
		difficultyMenu.add(hardDifficultyItem);

		JMenuItem clearScoresItem = new JMenuItem(CLEAR_SCORES);

		settingsMenu.add(difficultyMenu);
		settingsMenu.add(clearScoresItem);

		menubar.add(gameMenu);
		menubar.add(settingsMenu);

		// use a single listener to handle all menu item selections
		for (int i = 0; i < menubar.getMenuCount(); ++i) {
			for (JMenuItem item : getMenuItems(menubar.getMenu(i))) {
				item.addActionListener(listener);
			}
		}

		// setup shortcuts
		playItem.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));

		return menubar;
	}

	// this recursion works because JMenu is a subclass of JMenuItem!
	private static List<JMenuItem> getMenuItems(JMenuItem item) {
		List<JMenuItem> items = new ArrayList<>();

		if (item instanceof JMenu) {
			JMenu menu = (JMenu) item;
			for (int i = 0; i < menu.getItemCount(); ++i) {
				items.addAll(getMenuItems(menu.getItem(i)));
			}
		} else {
			items.add(item);
		}

		return items;
	}

	// listener for the menu items
	private static ActionListener menuListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case PLAY:
				if (fieldPanel.isActive())
					return;

				field.setup(fieldPanel.getSize());
				fieldPanel.setActive(true);

				gameFrame.resetScore();
				gameFrame.updateStatus("good luck pilot...");
				break;

			case EXIT:
				fieldPanel.setActive(false);
				highScores.save("highscores.txt");
				System.exit(0);
				break;

			case EASY:
				field.setDifficultyFactor(Field.EASY);
				field.setup(fieldPanel.getSize());
				gameFrame.updateStatus("easy mode");
				break;

			case NORMAL:
				field.setDifficultyFactor(Field.NORMAL);
				field.setup(fieldPanel.getSize());
				gameFrame.updateStatus("normal mode");
				break;

			case HARD:
				field.setDifficultyFactor(Field.HARD);
				field.setup(fieldPanel.getSize());
				gameFrame.updateStatus("hard mode");
				break;

			case CLEAR_SCORES:
				File file = new File("highscores.txt");
				file.delete();

				highScores.clear();
				gameFrame.updateHighscores(highScores.getTopScores(20));
				break;
			}
		}
	};

	// listener for the timer animation
	private static ActionListener animationListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (fieldPanel.isActive()) {

				field.update(fieldPanel.getSize());
				gameFrame.updateScore(field.getScore());
				fieldPanel.repaint();

				if (field.shipHitAsteroid()) {
					fieldPanel.setActive(false);

					gameFrame.updateStatus("game over!");

					if (field.getScore() == 0) {
						return;
					}

					String playerName = JOptionPane
							.showInputDialog("Player name? ");
					if (playerName == null) {
						return;
					}

					playerName = playerName.trim();
					if (playerName.length() == 0) {
						return;
					}

					highScores.addScore(playerName, field.getScore());
					gameFrame.updateHighscores(highScores.getTopScores(20));
				}
			}
		}
	};

	// listener for the FieldPanel key presses
	private static KeyListener keyListener = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			if (!fieldPanel.isActive())
				return;

			MoveableObject ship = field.getShip();
			switch (KeyEvent.getKeyText(e.getKeyCode())) {
			case "Up":
				ship.push(0, -1);
				break;

			case "Down":
				ship.push(0, 1);
				break;

			case "Left":
				ship.push(-1, 0);
				break;

			case "Right":
				ship.push(1, 0);
				break;

			case "Space":
				field.addBullets();
				break;
			}
		}
	};
}
