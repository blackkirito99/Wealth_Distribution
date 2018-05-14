package wealth_distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class World {
	private Patch[][] patches;
	private List<Turtle> turtles;
	private int size = Params.MAP_SIZE;
	private int tick;

	public World(){
		patches= new Patch[size][size];
		turtles = new ArrayList<Turtle>();
		
	}
	
	// set up the world
	public void setUp() {
		setUpPatches();
		setUpTurtles();
		sanityCheck();
	}
	
	public void setUpPatches() {
		// Create all patches and make some of them best-land
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				Patch p = new Patch();
				patches[x][y] = p;
			}
		}
		// Spread that grain around the window a little and put a little back
		// into the patches that are the "best land" found above
		for(int i = 0; i < 5; i++) {
			for(int x = 0; x < this.size; x++) {
				for(int y = 0; y < this.size; y++) {
					if(this.patches[x][y].isBestLand()) {
						int maxGrain = patches[x][y].getMaxGrain();
						patches[x][y].setGrains(maxGrain);
					}
				}
			}
			
			for(int x = 0; x < this.size; x++) {
				for(int y = 0; y < this.size; y++) {
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
				this.patches[x][y].finalGrainsInitilization();
			}
		}
		printArray();
		
	}
	
	// randomly seed turtles to the world
	public void setUpTurtles() {
		// to find the richest amount after initialization
		for(int i = 0; i < Params.NUMPEOPLE; i++) {
			// generate totally random turtle
			Turtle new_turtle = new Turtle();
			//max_initial_wealth = (max_initial_wealth > new_turtle.getCurrentGrains())? max_initial_wealth : new_turtle.getCurrentGrains();
			//max_initial_wealth = Math.max(max_initial_wealth, new_turtle.getCurrentGrains());
			turtles.add(new_turtle);
			// the patch has new turtle enter, increase count
			int xAxis = new_turtle.getX();
			int yAxis = new_turtle.getY();
			patches[xAxis][yAxis].turtleEnter();
		}
		int maxWealth = findMaxWealth();
		
		// update each turtle wealth class based on initial held grains
		updateTurtleClass(maxWealth);
	}
	
	public int findMaxWealth() {
		int max = 0;
		for(Turtle people : turtles) {
			if(people.getCurrentGrains() > max) {
				max = people.getCurrentGrains();
			}
		}
		return max;
	}
	
	public void updateTurtleClass(int wealth) {
		for(Turtle t : turtles) {
			t.updateClass(wealth);
		}
		
	}
	
	// go one tick forward
	public void go() {
		tick++;
		//turnTowardsGrains();
		harvestGrains();
		//int richest_amount = moveEatAgeDie();
		//updateTurtleClass(richest_amount);
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


	

	
	// each turtle turns to the direction with most number of grains within its vision
	private void updateDirection() {
		for(Turtle t : turtles) {
			// get number of grains within vision in all four directions
			t.findOptimalPath(patches);
			
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
			patches[x][y].harvetGrains();	
		}
	}
	
	/*private int moveEatAgeDie() {
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
	}*/
	
	// update each turtles' wealth class based on its amount of current held grains

	
	// regrow grains in each patches
	private void patchesRegrow() {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				patches[x][y].growGrains(tick);
			}
		}
		
	}




	
	
	/*private Turtle randomTurtle(int x, int y) {
		if(x < 0 || y < 0) {
			// random location when set up
			x = rand.nextInt(size);
			y = rand.nextInt(size);
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
	}*/
	
	private void diffuse(int x, int y) {
		// Calculate amount to spread to each neighbour
		patches[x][y].grainDiffuse();
		double amount = patches[x][y].getDiffuseAmount();
		
		// Spread grains to surrounding 8 patches equally
		patches[(x-1+size)%size][(y-1+size)%size].addGrains(amount);
		patches[x][(y-1+size)%size].addGrains(amount);
		patches[(x+1)%size][(y-1+size)%size].addGrains(amount);
		patches[(x-1+size)%size][y].addGrains(amount);
		patches[(x+1)%size][y].addGrains(amount);
		patches[(x-1+size)%size][(y+1)%size].addGrains(amount);
		patches[x][(y+1)%size].addGrains(amount);
		patches[(x+1)%size][(y+1)%size].addGrains(amount);
	}
	
	private void sanityCheck() {
		// check total turtle count in patches remain unchanged
		int turtles_count = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				turtles_count += patches[x][y].getTurtleCount();
			}
		}
		System.out.println("Current Tick: " + tick+ "\nTurtles count: "+ turtles_count);
	}
	

	private void printArray() {
		double[][] arrayDouble = new double[size][size];
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				arrayDouble[x][y] = patches[x][y].getCurrentGrains();
			}
		}
		for(double[] array : arrayDouble) {
			System.out.println(Arrays.toString(array));  
		}
			
	}
	
	public static void main(String args[]) {
		World w = new World();
		w.setUp();
		//for(int i = 0;  i < 10000; i++) {
			//w.go();
		//}	
	}
}
