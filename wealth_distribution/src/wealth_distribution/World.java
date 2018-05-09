package wealth_distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	private Patch[][] patches;
	private List<Turtle> turtles;
	private int max_grain = 100;
	private int regrow_amount = 20;
	private int regrow_intervel = 100;
	private int max_life = 100;
	private int min_life = 20;
	private int max_metabolism = 10;
	private int max_vision = 5;
	private int size = 100;
	private int tick;
	private double percent_best_land = 0.25;
	private Random rand;

	public World(){
		this.patches= new Patch[size][size];
		this.turtles = new ArrayList<Turtle>();
		rand = new Random();
	}
	
	// set up the world
	public void setUp() {
		setUpPatches();
		setUpTurtles(100);
		this.tick = 1;
		this.max_grain = 50;
		sanityCheck();
	}
	
	// go one tick forward
	public void go() {
		tick++;
		turnTowardsGrains();
		harvestGrains();
		int richest_amount = moveEatAgeDie();
		updateTurtleClass(richest_amount);
		patchesRegrow();
		sanityCheck();
		
		// need statistic methods
		classInfo();
	}
	
	// show class in current tick
	private void classInfo() {
		int low_count = 0;
		int medium_count = 0;
		int high_count = 0;
		for(int i = 0; i < this.turtles.size(); i++) {
			switch (this.turtles.get(i).getWealthClass()){
				case 0:
					low_count++;
					break;
				case 1:
					medium_count++;
					break;
				case 2:
					high_count++;
					break;
				default: 
					break;
			}
		}
		System.out.println("low: " + low_count + "; med: " + medium_count + "; high: " + high_count);
	}

	private void setUpPatches() {
		// Create all patches and make some of them best-land
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				Patch p = new Patch(this.regrow_amount, this.regrow_intervel);
				if(rand.nextInt(101) < this.percent_best_land*100) {
					p.setBestLand();
					p.setGrains(max_grain);
				}
				patches[x][y] = p;
			}
		}
		// Spread that grain around the window a little and put a little back
		// into the patches that are the "best land" found above
		for(int i = 0; i < 5; i++) {
			for(int x = 0; x < this.size; x++) {
				for(int y = 0; y < this.size; y++) {
					if(this.patches[x][y].isBestLand()) {
						this.patches[x][y].setGrains(max_grain);
					}
					diffuse(x, y);
				}
			}
		}
		//Spread the grain around some more
		for(int i = 0; i < 10; i++) {
			for(int x = 0; x < this.size; x++) {
				for(int y = 0; y < this.size; y++) {
					diffuse(x, y);
				}
			}
		}
		//initial grain level is also maximum
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				this.patches[x][y].finalGrainsInitilization(max_grain);
			}
		}
		
	}
	
	// randomly seed turtles to the world
	private void setUpTurtles(int count) {
		// to find the richest amount after initialisation
		int max_initial_wealth = -1;
		for(int i = 0; i < count; i++) {
			// generate totally random turtle
			Turtle new_turtle = randomTurtle(-1, -1);
			//max_initial_wealth = (max_initial_wealth > new_turtle.getCurrentGrains())? max_initial_wealth : new_turtle.getCurrentGrains();
			max_initial_wealth = Math.max(max_initial_wealth, new_turtle.getCurrentGrains());
			this.turtles.add(new_turtle);
			// the patch has new turtle enter, increase count
			this.patches[new_turtle.getX()][new_turtle.getY()].turtleEnter();
		}
		
		// update each turtle wealth class based on initial held grains
		updateTurtleClass(max_initial_wealth);
	}
	
	// each turtle turns to the direction with most number of grains within its vision
	private void turnTowardsGrains() {
		for(int i = 0; i < this.turtles.size(); i++) {
			Turtle t = this.turtles.get(i);
			int x = t.getX();
			int y = t.getY();
			// get number of grains within vision in all four directions
			int grainsTop = grainsInVison(x,y,t.getVision(), 0, -1);
			int grainsBot = grainsInVison(x,y,t.getVision(), 0, 1);
			int grainsLeft = grainsInVison(x,y,t.getVision(), -1, 0);
			int grainsRight = grainsInVison(x,y,t.getVision(), 1, 0);
			
			// find out and set the best direction for the turtle
			if(grainsTop >= grainsBot && grainsTop >= grainsLeft && grainsTop > grainsRight) {
				t.updateHeading(0);
			}else if(grainsBot >= grainsTop && grainsBot >= grainsLeft && grainsBot > grainsRight) {
				t.updateHeading(1);
			}else if(grainsLeft >= grainsBot && grainsLeft >= grainsTop && grainsLeft > grainsRight) {
				t.updateHeading(2);
			}else if(grainsRight >= grainsBot && grainsRight >= grainsLeft && grainsRight > grainsTop) {
				t.updateHeading(3);
			}
			
		}
	}
	
	private void harvestGrains() {
		// distribute grain evenly if several turtles on same patch
		for(int i = 0; i < this.turtles.size(); i++) {
			Turtle t = this.turtles.get(i);
			int x = t.getX();
			int y = t.getY();	
			if(this.patches[x][y].getCurrentGrains() > 0) {
				t.gainGrains((int)Math.floor(this.patches[x][y].getCurrentGrains()*1.0/this.patches[x][y].getTurtleCount()));	
			}
		}
		
		// harvet grains (set grains in patch to 0)
		for(int i = 0; i < this.turtles.size(); i++) {
			Turtle t = this.turtles.get(i);
			int x = t.getX();
			int y = t.getY();
			this.patches[x][y].harvetGrains();	
		}
	}
	
	private int moveEatAgeDie() {
		//move forward 1 step of heading
		for(int i = 0; i < this.turtles.size(); i++) {
			Turtle t = this.turtles.get(i);
			int x = t.getX();
			int y = t.getY();
			// turtle leave this patch, decrease count in this patch
			this.patches[x][y].turtleLeave();
			
			int new_x;
			int new_y;
			// turtle enter new patch based on heading, increase count in that patch
			switch (t.getHeading()) {
				case 0:
					new_y = (y-1+this.size)%this.size;
					t.updateLocation(x, new_y);
					this.patches[x][new_y].turtleEnter();
					break;
				case 1:
					new_x = (x-1+this.size)%this.size;
					t.updateLocation((x-1+this.size)%this.size, y);
					this.patches[new_x][y].turtleEnter();
					break;
				case 2:
					new_y = (y+1)%this.size;
					t.updateLocation(x, new_y);
					this.patches[x][new_y].turtleEnter();
					break;
				case 3:
					new_x = (x+1)%this.size;
					t.updateLocation(new_x, y);
					this.patches[new_x][y].turtleEnter();
					break;
				default:
					break;
			}
		}
		
		//eat & age & die
		int richest_amount = 0; // amount of grains held by richest turtle in the world
		for(int i = 0; i < this.turtles.size(); i++) {
			Turtle t = this.turtles.get(i);
			t.eatGrains();
			t.growUp();
			if(!t.checkSurvive()) {
				// if die, produce an offspring
				this.patches[t.getX()][t.getY()].turtleLeave();
				Turtle offspring = randomTurtle(t.getX(), t.getY());
				turtles.set(i, offspring);
				this.patches[offspring.getX()][offspring.getY()].turtleEnter();
				
				// check if this offspring held most number of grains
				richest_amount = Math.max(richest_amount, offspring.getCurrentGrains());
			}else {
				// check if this turtle held most number of grains
				richest_amount = Math.max(richest_amount, t.getCurrentGrains());
			}
		}
		// return for statistics usage
		return richest_amount;
	}
	
	// update each turtles' wealth class based on its amount of current held grains
	private void updateTurtleClass(int richest_amount) {
		for(int i = 0; i < this.turtles.size(); i++) {
			this.turtles.get(i).updateClass(richest_amount);
		}
		
	}
	
	// regrow grains in each patches
	private void patchesRegrow() {
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				this.patches[x][y].growGrains(tick);
			}
		}
		
	}

	// Calculate grains within vision in given direction (decided by x_multiplier and y_multiplier)
	private int grainsInVison(int x, int y, int vision, int x_multiplier, int y_multiplier) {
		int total_grains = 0;
		for(int i = 1; i <= vision; i++) {
			int inspectX = (x+i*x_multiplier + this.size)%this.size;
			int inspectY = (x+i*x_multiplier + this.size)%this.size;
			total_grains += this.patches[inspectX][inspectY].getCurrentGrains();
		}
		return total_grains;
	}

	
	
	private Turtle randomTurtle(int x, int y) {
		if(x < 0 || y < 0) {
			// random location when set up
			x = rand.nextInt(size);
			y = rand.nextInt(size);
		}else {
			// random neighbour location when leaving offspring
			// actually should be same location
			/*int x_offset = 2*rand.nextInt(2)-1;
			int y_offset = 2*rand.nextInt(2)-1;
			x = (x + x_offset + this.size) % this.size;
			y = (y + y_offset + this.size) % this.size;*/
		}
		
		// random everything
		int life_expectancty = rand.nextInt(max_life-min_life + 1) + min_life;
		int age = rand.nextInt(life_expectancty);
		int metabolism =  1 + rand.nextInt(max_metabolism);
		int held_grains =  metabolism + rand.nextInt(50);
		int vision = 1 + rand.nextInt(max_vision);
		int heading = rand.nextInt(4);
		
		// return the new random turtles
		return new Turtle(x, y, life_expectancty, vision, metabolism, heading, held_grains, age);
	}
	
	private void diffuse(int x, int y) {
		// Calculate amount to spread to each neighbour
		int grain_amount = (int) (this.patches[x][y].getCurrentGrains()*0.75/8.0);
		
		// Update grains after spread
		this.patches[x][y].setGrains((int) (this.patches[x][y].getCurrentGrains()*0.75));
		
		// Spread grains to surrounding 8 patches equally
		this.patches[(x-1+this.size)%this.size][(y-1+this.size)%this.size].addGrains(grain_amount);
		this.patches[x][(y-1+this.size)%this.size].addGrains(grain_amount);
		this.patches[(x+1)%this.size][(y-1+this.size)%this.size].addGrains(grain_amount);
		this.patches[(x-1+this.size)%this.size][y].addGrains(grain_amount);
		this.patches[(x+1)%this.size][y].addGrains(grain_amount);
		this.patches[(x-1+this.size)%this.size][(y+1)%this.size].addGrains(grain_amount);
		this.patches[x][(y+1)%this.size].addGrains(grain_amount);
		this.patches[(x+1)%this.size][(y+1)%this.size].addGrains(grain_amount);
	}
	
	private void sanityCheck() {
		// check total turtle count in patches remain unchanged
		int turtles_count = 0;
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				turtles_count += this.patches[x][y].getTurtleCount();
			}
		}
		System.out.println("Current Tick: " + tick+ "\nTurtles count: "+ turtles_count);
	}
	
	public static void main(String args[]) {
		World w = new World();
		w.setUp();
		for(int i = 0;  i < 10000; i++) {
			w.go();
		}	
	}
}
