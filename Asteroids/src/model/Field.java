package model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
	private MoveableObject ship;
	private List<MoveableObject> asteroids;
	private List<TimedObject> bullets;

	private int difficultyFactor;
	public static final int EASY = 1;
	public static final int NORMAL = 2;
	public static final int HARD = 3;

	private int asteroidsCount;
	private int scoreValue;
	
	private static Random random;
	static {
		random = new Random();
		// random.setSeed(0);
	}

	public Field() {
		ship = new MoveableObject();
		bullets = new ArrayList<>();
		asteroids = new ArrayList<>();
		ship.width = ship.height = 10;
		difficultyFactor = NORMAL;
	}
	
	public void setDifficultyFactor(int level) {
		switch (level) {
		case EASY:
			difficultyFactor = EASY;
			break;
		case NORMAL:
			difficultyFactor = NORMAL;
			break;
		case HARD:
			difficultyFactor = HARD;
			break;
		default:
			difficultyFactor = NORMAL;
		}
	}

	public void setup(Dimension size) {
		ship.position.x = size.width / 2;
		ship.position.y = size.height / 2;
		ship.stopMoving();

		scoreValue = 0;
		asteroidsCount = 5;
		createAsteroids(size);

		bullets = new ArrayList<>();
	}

	private void createAsteroids(Dimension size) {
		// the center region is used to restrict asteroid positions
		// they can not be added to the center region
		// because that is where the ship is located
		MoveableObject noGoRegion = new MoveableObject();
		noGoRegion.width = size.width / (difficultyFactor + 1);
		noGoRegion.height = size.height / (difficultyFactor + 1);
		noGoRegion.position.x = ship.position.x;
		noGoRegion.position.y = ship.position.y;

		asteroids = new ArrayList<>();
		for (int i = 0; i < asteroidsCount; ++i) {
			MoveableObject asteroid = new MoveableObject();
			do {
				asteroid.position.x = random.nextInt(size.width);
				asteroid.position.y = random.nextInt(size.height);
			} while (noGoRegion.contains(asteroid.position));
			asteroids.add(asteroid);

			// an asteroid is a random rectangle
			asteroid.width = (10 + random.nextInt(10)) * difficultyFactor;
			asteroid.height = (10 + random.nextInt(10)) * difficultyFactor;

			int impulseX = (1 + random.nextInt(4)) * difficultyFactor;
			int impulseY = (1 + random.nextInt(4)) * difficultyFactor;

			// force the asteroid to be initially moving away from the ship
			if (asteroid.position.x < ship.position.x) {
				impulseX *= -1;
			}
			if (asteroid.position.y < ship.position.y) {
				impulseY *= -1;
			}

			asteroid.push(impulseX, impulseY);
		}
	}

	public void addBullets() {
		if (! ship.moving()) return;
		
		for (int i = 1; i < 4; ++i) {
			TimedObject bullet = new TimedObject(
					random.nextInt(50 / difficultyFactor));

			bullet.position.x = ship.position.x;
			bullet.position.y = ship.position.y;

			bullet.width = 5;
			bullet.height = 5;

			int speed = 2 * i;
			bullet.push(speed * ship.xOffset, speed * ship.yOffset);

			bullets.add(bullet);
		}
	}

	public void update(Dimension size) {
		ship.limitMaximumSpeed();
		ship.move();
		ship.bounce(size);

		for (int i = 0; i < bullets.size(); ++i) {
			TimedObject bullet = bullets.get(i);

			if (bullet.isTimedOut()) {
				bullets.remove(i);
			} else {
				bullet.move();
				bullet.bounce(size);
			}
		}

		for (int i = 0; i < asteroids.size(); ++i) {
			MoveableObject asteroid = asteroids.get(i);

			for (int j = 0; j < bullets.size(); ++j) {
				TimedObject bullet = bullets.get(j);

				if (asteroid.contains(bullet.position)) {
					asteroids.remove(i);
					bullets.remove(j);

					if (difficultyFactor == 1) {
						scoreValue += 10;
					} else if (difficultyFactor < 1) {
						scoreValue += 5;
					} else {
						scoreValue += 15;
					}

					break;
				}
			}
		}

		if (asteroids.size() == 0) {
			asteroidsCount += 5;
			createAsteroids(size);
		}

		for (MoveableObject asteroid : asteroids) {
			asteroid.limitMaximumSpeed();
			asteroid.move();
			asteroid.bounce(size);
		}
	}

	public MoveableObject getShip() {
		return ship;
	}

	public List<MoveableObject> getAsteroids() {
		return asteroids;
	}

	public List<TimedObject> getBullets() {
		return bullets;
	}

	public boolean shipHitAsteroid() {
		for (MoveableObject asteroid : asteroids) {
			if (asteroid.contains(ship.position)) {
				return true;
			}
		}
		return false;
	}

	public int getScore() {
		return scoreValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(String.format("%11s:\t%s\n", "Ship", ship));
		int id = 1;
		for (MoveableObject asteroid : asteroids) {
			builder.append(String.format("Asteroid[%d]:\t%s\n", id++, asteroid));
		}

		return builder.toString();
	}
}
