package wealth_distribution;

/**
 * Turtles travel on tiles, trying to collecting as many grains as possible
 * whilst consuming grains every tick
 * @author runze
 *
 */

public class Turtle extends Agent implements Comparable<Turtle>{
	//amount of grain a turtle currently possesses
	private int heldGrains;
	private int age;
	
	//tiles ahead a turtle could see 
	private int vision;
	//grains consumed each tick
	private int metabolism;
	//maximum number of tick a turtle could survive
	private int lifeExpectancy;  
	//the direction a turtle will march
	private int direction;
	//wealth class for turtles
	private int wealthClass; 
	//location, x coordinate
	private int x;
	//location, y coordinate
	private int y;
	
	//constructor
	public Turtle() {
		x = Params.randomLocation();
		y = Params.randomLocation();
		lifeExpectancy = Params.randomLifeExpectancy();
		vision = Params.randomVision();
		metabolism = Params.randomMetabolism();
		heldGrains = metabolism + Params.randomWealth();
		age = Params.randomAge(lifeExpectancy);
	}
	
	//constructor 
	public Turtle(int wealth) {
		x = Params.randomLocation();
		y = Params.randomLocation();
		lifeExpectancy = Params.randomLifeExpectancy();
		vision = Params.randomVision();
		metabolism = Params.randomMetabolism();
		heldGrains = metabolism + Params.randomWealth(); 
		if(wealth > 0) {
			heldGrains += (int)((double)wealth * Params.INHERIT_PERCENT);
		}
		age = 0;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public int getCurrentGrains() {
		return heldGrains;
	}
	
	//turtles gain grains
	public void gainGrains(int harvestedGrain) {
		heldGrains += harvestedGrain;
	}
	//turtles consume grain
	public void eatGrains() {
		heldGrains -= metabolism;
	}
	
	//turtles are aging
	public void growUp() {
		age++;
	}
	
	// check if the turtle can survive
	public boolean checkSurvive() {
		if(heldGrains < 0 || age >= lifeExpectancy) {
			return false;
		}
		return true;
	}
	
	//check the wealth class that a turtle belongs to
	//by comparing its grain with the highest number of grains
	public void updateClass(int richest) {
		if(getCurrentGrains() > richest*2.0/3.0) {
			wealthClass = Params.UPPER_CLASS;
		}else if(getCurrentGrains() > richest*1.0/3.0) {
			wealthClass = Params.MIDDLE_CLASS;
		}else if(getCurrentGrains() <= richest*1.0/3.0) {
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
	//updating the direction 
	public void updateHeading(int dir) {
		direction = dir;
	}
	//updating location
	public void updateLocation(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	//find the best direction that highest number of grains is detected 
	//in this direction
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
			updateHeading(Params.TOP);
		}
		if(left >= top && left >= bot && left >= right) {
			updateHeading(Params.LEFT);
		}
		if(bot >= top && bot >= right && bot >= left) {
			updateHeading(Params.BOTTOM);
		}
		else {
			updateHeading(Params.RIGHT);
		}
		
		
	}
	
	//turtles move in the map guided by the best direction provided
	//according to the original model, turtles do not get to move
	//out of the map; for instance
	//if a turtle goes out of the left boundary of the map
	//he/she will enter the map from the right border of the map again
	public void move(Patch[][] patches) {
		
		int size = Params.MAP_SIZE;
		patches[x][y].turtleLeave();
		
		int new_x;
		int new_y;
		// turtle enter new patch based on direction
		switch (direction) {
			case Params.TOP:
				new_y = (y+1)%size;
				updateLocation(x, new_y);
				patches[x][new_y].turtleEnter();
				break;
			case Params.LEFT:
				new_x = (x-1+size)%size;
				updateLocation(new_x, y);
				patches[new_x][y].turtleEnter();
				break;
			case Params.BOTTOM:
				new_y = (y-1+size)%size;
				updateLocation(x, new_y);
				patches[x][new_y].turtleEnter();
				break;
			case Params.RIGHT:
				new_x = (x+1)%size;
				updateLocation(new_x, y);
				patches[new_x][y].turtleEnter();
				break;
			default:
				break;
		}
	}
	//turtle harvest grain from tiles
	public void harvest(Patch[][] patches) {
		int amount = patches[x][y].harvested();
		gainGrains(amount);
	}

	@Override
	public int compareTo(Turtle arg0) {
		return this.getCurrentGrains() - arg0.getCurrentGrains();
	}

	
	
	
	
}
