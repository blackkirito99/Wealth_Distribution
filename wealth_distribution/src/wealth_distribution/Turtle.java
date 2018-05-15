package wealth_distribution;

public class Turtle implements Comparable<Turtle>{
	private int heldGrains;
	private int age;
	private int vision;
	private int metabolism;  
	private int lifeExpectancy;  
	private int direction; 		// 0 top 1 left 2 bottom 3 right
	private int wealthClass; 	// 0 for low, 1 for medium, 2 for high
	private int x;
	private int y;
	
	
	public Turtle() {
		x = Params.randomLocation();
		y = Params.randomLocation();
		lifeExpectancy = Params.randomLifeExpectancy();
		vision = Params.randomVision();
		metabolism = Params.randomMetabolism();
		//this.heading = heading;
		heldGrains = metabolism + Params.randomWealth();
		age = Params.randomAge(lifeExpectancy);
	}
	
	public Turtle(int wealth) {
		x = Params.randomLocation();
		y = Params.randomLocation();
		lifeExpectancy = Params.randomLifeExpectancy();
		vision = Params.randomVision();
		metabolism = Params.randomMetabolism();
		heldGrains = (int)(metabolism + wealth * Params.INHERIT_PERCENT);
		//heldGrains = metabolism + Params.randomWealth();
		age = 0;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	public int getCurrentGrains() {
		return this.heldGrains;
	}
	public void gainGrains(int harvestedGrain) {
		this.heldGrains += harvestedGrain;
	}
	public void eatGrains() {
		this.heldGrains -= metabolism;
	}
	
	public void growUp() {
		this.age++;
	}
	
	// survive if age is less than life_expectancy and hold some grains
	public boolean checkSurvive() {
		if(this.heldGrains < 0 || this.age >= lifeExpectancy) {
			return false;
		}
		return true;
	}
	
	public void updateClass(int richest_amount) {
		if(getCurrentGrains() > richest_amount*2.0/3.0) {
			wealthClass = Params.UPPER_CLASS;
		}else if(getCurrentGrains() > richest_amount*1.0/3.0) {
			wealthClass = Params.MIDDLE_CLASS;
		}else if(getCurrentGrains() <= richest_amount*1.0/3.0) {
			wealthClass = Params.WORKING_CLASS;
	
		}
		//System.out.println(this.getCurrentGrains() + " " + richest_amount + " " + this.wealthClass);
	}
	
	public int getHeading() {
		return direction;
	}
	
	public int getVision() {
		return this.vision;
	}
	
	public int getWealthClass() {
		return this.wealthClass;
	}
	
	public void updateHeading(int direction) {
		this.direction = direction;
	}

	public void updateLocation(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	public void findOptimalPath(Patch[][] patches) {
		int top = 0;
		int left = 0;
		int bot = 0;
		int right = 0;
		int size = Params.MAP_SIZE;
		for(int i = 1; i <= vision; i++) {
			top += (int)patches[x][(y+1)%size].getCurrentGrains();
			bot += (int)patches[x][(y-1+size)%size].getCurrentGrains();
			left += (int)patches[(x-1+size)%size][y].getCurrentGrains();
			right += (int)patches[(x+1)%size][y].getCurrentGrains();
		}
		if(top >= bot && top >= left && top >= right) {
			updateHeading(0);
		}
		if(left >= top && left >= bot && left >= right) {
			updateHeading(1);
		}
		if(bot >= top && bot >= right && bot >= left) {
			updateHeading(2);
		}
		else {
			updateHeading(3);
		}
		
		
	}
	
	public void move(Patch[][] patches) {
		
		int size = Params.MAP_SIZE;
		patches[x][y].turtleLeave();
		
		int new_x;
		int new_y;
		// turtle enter new patch based on heading, increase count in that patch
		switch (direction) {
			case 0:
				new_y = (y+1)%size;
				updateLocation(x, new_y);
				patches[x][new_y].turtleEnter();
				break;
			case 1:
				new_x = (x-1+size)%size;
				updateLocation(new_x, y);
				patches[new_x][y].turtleEnter();
				break;
			case 2:
				new_y = (y-1+size)%size;
				updateLocation(x, new_y);
				patches[x][new_y].turtleEnter();
				break;
			case 3:
				new_x = (x+1)%size;
				updateLocation(new_x, y);
				patches[new_x][y].turtleEnter();
				break;
			default:
				break;
		}
	}
	
	public void havest(Patch[][] patches) {
		int amount = patches[x][y].harvested();
		gainGrains(amount);
	}

	@Override
	public int compareTo(Turtle arg0) {
		return this.getCurrentGrains() - arg0.getCurrentGrains();
	}
	
	
	
}
