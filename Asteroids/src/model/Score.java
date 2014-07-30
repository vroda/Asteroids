package model;
public class Score {
	protected String playerName;
	protected int value;

	public Score(String playerName, int value) {
		this.playerName = playerName;
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s:%d", playerName, value);
	}
}
