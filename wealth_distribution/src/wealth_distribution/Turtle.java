package wealth_distribution;


public class Turtle {
	private int held_grains;
	private int age;
	private int vision;
	private int metabolism;
	private int life_expectancty;
	private int heading; // 0 top 1 left 2 bottom 3 right
	private int wealthClass; // 0 for low, 1 for medium, 2 for high
	
	private boolean moved;
	
	
	public Turtle(int life_expectancty, int vision, int metabolism, int heading, int held_grains, int age) {
		this.age = age;
		this.life_expectancty = life_expectancty;
		this.vision = vision;
		this.metabolism = metabolism;
		this.heading = heading;
		this.held_grains = held_grains;
		this.moved = false;
	}
	
	public int getCurrentGrain() {
		return this.held_grains;
	}
	public void gainGrains(int harvestedGrain) {
		this.held_grains += harvestedGrain;
	}
	public void eatGrains() {
		this.held_grains -= this.metabolism;
	}
	
	public void growUp() {
		this.age++;
	}
	
	public boolean checkSurvive() {
		if(this.held_grains < 0 || this.age >= this.life_expectancty) {
			return false;
		}
		return true;
	}
	
	public void updateClass(int richest_amount) {
		if(this.getCurrentGrain() > richest_amount*2.0/3.0) {
			this.wealthClass = 2;
		}else if(this.getCurrentGrain() > richest_amount*1.0/3.0) {
			this.wealthClass = 1;
		}else if(this.getCurrentGrain() <= richest_amount*1.0/3.0) {
			this.wealthClass = 0;
	
		}
	}
	
	public int getHeading() {
		this.moved = true;
		return heading;
	}
	
	public void resetMoved() {
		this.moved = false;
	}
	
	public boolean hasMoved() {
		return this.moved;
	}
	public int getVision() {
		return this.vision;
	}
	
	public int getWealthClass() {
		return this.wealthClass;
	}
	
	public void updateHeading(int heading) {
		this.heading = heading;
	}
}
