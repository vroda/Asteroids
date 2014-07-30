package model;

public class TimedObject extends MoveableObject {

	protected int duration;
	
	public TimedObject(int duration) {
		this.duration = duration;
	}
	
	public boolean isTimedOut() {
		return duration == 0;
	}
	
	@Override
	void move() {
		if (!isTimedOut()) {
			--duration;
		}
		
		super.move();
	}
}
