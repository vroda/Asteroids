package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

// replaced width/height with RotatablePolygon
public class MoveableObject {

	protected int xOffset, yOffset;
	private static final int MAX_SPEED = 10;
	protected Point position;
	protected int width, height;

	public MoveableObject() {
		position = new Point();
		width = height = 1;
	}

	public void draw(Graphics g) {
		g.fillRect(position.x - width / 2, position.y - height / 2, width,
				height);
	}

	public void push(int impulseX, int impulseY) {
		xOffset += impulseX;
		yOffset += impulseY;
	}

	public void limitMaximumSpeed() {
		// prevent speed from getting to fast
		if (xOffset > MAX_SPEED)
			xOffset = MAX_SPEED;

		if (xOffset < -MAX_SPEED)
			xOffset = -MAX_SPEED;

		if (yOffset > MAX_SPEED)
			yOffset = MAX_SPEED;

		if (yOffset < -MAX_SPEED)
			yOffset = -MAX_SPEED;
	}

	public void stopMoving() {
		xOffset = yOffset = 0;
	}

	void move() {
		position.x += xOffset;
		position.y += yOffset;
	}

	public boolean moving() {
		return xOffset != 0 || yOffset != 0;
	}

	void bounce(Dimension size) {
		if ((position.x <= 0 & xOffset < 0)
				|| (position.x >= size.width & xOffset > 0)) {
			xOffset *= -1;
		}

		if ((position.y <= 0 & yOffset < 0)
				|| (position.y >= size.height & yOffset > 0)) {
			yOffset *= -1;
		}
	}

	public boolean contains(Point point) {
		double xDiff = Math.abs(position.x - point.x);
		double yDiff = Math.abs(position.y - point.y);

		return xDiff < width / 2 && yDiff < height / 2;
	}

	@Override
	public String toString() {
		String xMotion = xOffset < 0 ? "Left" : xOffset == 0 ? "Stop" : "Right";
		String yMotion = yOffset < 0 ? "Up" : yOffset == 0 ? "Stop" : "Down";

		String positionStr = String.format("(%d,%d)", position.x, position.y);
		String motionStr = String.format("(%s,%s)", xMotion, yMotion);
		String sizeStr = String.format("[%d,%d]", width, height);
		return String.format("%s %s %s", positionStr, motionStr, sizeStr);
	}
}
