package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HighScores {
	private List<Score> scores;

	public HighScores() {
		scores = new ArrayList<>();
	}

	// insert the a new score into the scores list in
	// ordered by value from highest to lowest
	public void addScore(String playerName, int value) {
		Score score = new Score(playerName, value);

		// find the location to place the new score
		int i = 0;
		while (i < scores.size()) {
			if (value > scores.get(i).value) {
				break;
			}
			++i;
		}

		// add the new score
		if (i < scores.size()) {
			scores.add(i, score); // add within the list
		} else {
			scores.add(score); // add at the end
		}
	}

	public void clear() {
		scores.clear();
	}
	
	public boolean load(String filename) {
		boolean success = true;
		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(filename));

			// we assume the line format is <string> : <int> \n
			scores.clear();
			while (scanner.hasNext()) {
				String[] elements = scanner.nextLine().split(":");

				String playerName = elements[0].trim();
				int value = Integer.parseInt(elements[1].trim());
				scores.add(new Score(playerName, value));
			}

		} catch (Exception e) {
			success = false; // invalid filename or invalid data in file
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return success;
	}

	public boolean save(String filename) {
		boolean success = true;
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(new File(filename));

			for (Score score : scores) {
				writer.printf("%s\n", score);
			}

		} catch (FileNotFoundException e) {
			success = false;
		} finally {
			writer.close();
		}

		return success;
	}

	public List<Score> getTopScores(int number) {
		if (number > scores.size()) {
			return scores;
		}

		return scores.subList(0, number);
	}
}
