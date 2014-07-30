package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Score;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -6399886456682347905L;
	private JPanel centerPanel, bottomPanel;
	private JLabel statusLabel, scoreLabel;
	private JTextArea highscoresList;

	public GameFrame(JPanel centerPanel) {
		super("Asteroids");
		this.centerPanel = centerPanel;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
	}

	private void setup() {
		bottomPanel = new JPanel();
		statusLabel = new JLabel("Status: ready...");
		scoreLabel = new JLabel("Score: ?");
		highscoresList = new JTextArea(0,20);
		highscoresList.setEditable(false);
		highscoresList.setFocusable(false);

		centerPanel.setPreferredSize(new Dimension(600, 400));
		highscoresList.setPreferredSize(new Dimension(100, 400));
		bottomPanel.setBackground(Color.LIGHT_GRAY);
		bottomPanel.setLayout(new BorderLayout());

		bottomPanel.add(statusLabel, BorderLayout.WEST);
		bottomPanel.add(scoreLabel, BorderLayout.EAST);

		add(centerPanel);
		add(bottomPanel, BorderLayout.SOUTH);
		add(highscoresList, BorderLayout.EAST);
		pack();
	}

	public void updateHighscores(List<Score> topScores) {
		highscoresList.setText("");
		for (Score score : topScores) {
			highscoresList.append(score.toString() + "\n");
		}
	}
	
	public void updateStatus(String message) {
		statusLabel.setText("Status: " + message);
	}

	public void updateScore(int value) {
		updateScoreLabel(value);
	}
	
	public void resetScore() {
		updateScoreLabel(0);
	}

	private void updateScoreLabel(int scoreValue) {
		scoreLabel.setText("Score: " + scoreValue);
	}
}
