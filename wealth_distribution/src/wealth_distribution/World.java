package wealth_distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	private Patch[][] patches;
	private List<Turtle>[][] turtles;
	private int max_grain = 100;
	private int regrow_amount = 20;
	private int regrow_intervel = 1;
	private int max_life = 100;
	private int min_life = 20;
	private int max_metabolism = 10;
	private int max_vision = 5;
	private int size = 100;
	private int tick;
	public World(){
		this.patches= new Patch[size][size];
		this.turtles= new List[size][size];
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				this.turtles[x][y] = new ArrayList<Turtle>();
			}
		}
	}
	
	public void setUp() {
		setUpTurtles(100);
		setUpPatches();
		this.tick = 1;
		this.max_grain = 50;
	}
	
	public void go() {
		tick++;
		turnTowardsGrains();
		int richest_amount = harvestGrains();
		moveEatAgeDie();
		updateTurtleClass(richest_amount);
		sanityCheck();
	}
	
	
	private void updateTurtleClass(int richest_amount) {
		// TODO Auto-generated method stub
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y].size() > 0) {
					for(Turtle t : turtles[x][y]) {
						t.updateClass(richest_amount);
					}
				}
			}
		}
		
	}

	private int moveEatAgeDie() {
		// TODO Auto-generated method stub
		//move
		resetAllMoved();
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y].size() > 0) {
					List<Turtle> moved_turtles = new ArrayList<Turtle>();
					for(Turtle t: this.turtles[x][y]) {
						// if havn't move this tick
						if(!t.hasMoved()) {
							moved_turtles.add(t);
							
							// move forward based on direction
							switch (t.getHeading()) {
								case 0:
									this.turtles[x][(y-1+this.size)%this.size].add(t); 
									break;
								case 1:
									this.turtles[(x-1+this.size)%this.size][y].add(t); 
									break;
								case 2:
									this.turtles[x][(y+1)%this.size].add(t); 
									break;
								case 3:
									this.turtles[(x+1)%this.size][y].add(t); 
									break;
								default:
									break;
							}			
						}
					}
					// remove moved turtles on  this location
					this.turtles[x][y].removeAll(moved_turtles);
				}
			}
		}
		
		int richest_amount = 0;
		//eat & age & die
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y].size() > 0) {
					List<Turtle> die_turtles = new ArrayList<Turtle>();
					for(Turtle t: this.turtles[x][y]) {
						t.eatGrains();
						t.growUp();
						
						// die, generates an offspring
						if(!t.checkSurvive()) {
							die_turtles.add(t);
							int grains_amount = randomTurtle(x, y);
							richest_amount = (richest_amount > grains_amount) ? richest_amount : grains_amount;
						}
					}
					
					// remove die turtles
					this.turtles[x][y].removeAll(die_turtles);
				}
			}
		}
		return richest_amount;
	}

	// set all turtles as haven't move in this tick
	private void resetAllMoved() {
		// TODO Auto-generated method stub
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y].size() > 0) {
					for(Turtle t: this.turtles[x][y]) {
						t.resetMoved();
					}
				}
			}
		}
	}

	private int harvestGrains() {
		// TODO Auto-generated method stub
		int richest_amount = 0;
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y].size() > 0) {
					int harvested_grains = this.patches[x][y].harvetGrains();
					int turtles_count = this.turtles[x][y].size();
					for(Turtle t: this.turtles[x][y]) {
						t.gainGrains((int)Math.floor(harvested_grains*1.0/turtles_count));
						richest_amount = (richest_amount > t.getCurrentGrain()) ? richest_amount : t.getCurrentGrain();
					}
				}
				
			}
		}
		return richest_amount;
	}

	private void turnTowardsGrains() {
		// TODO Auto-generated method stub
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y].size() > 0) {
					
					// turn direction with most grains
					for(Turtle t: this.turtles[x][y]) {
						int grainsTop = grainsInVison(x,y,t.getVision(), 0, -1);
						int grainsBot = grainsInVison(x,y,t.getVision(), 0, 1);
						int grainsLeft = grainsInVison(x,y,t.getVision(), -1, 0);
						int grainsRight = grainsInVison(x,y,t.getVision(), 1, 0);
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
			}
		}
	}
	
	private int grainsInVison(int x, int y, int vision, int x_multiplier, int y_multiplier) {
		int total_grains = 0;
		for(int i = 1; i <= vision; i++) {
			int inspectX = (x+i*x_multiplier + this.size)%this.size;
			int inspectY = (x+i*x_multiplier + this.size)%this.size;
			total_grains += this.patches[inspectX][inspectY].getCurrentGrains();
		}
		return total_grains;
	}

	private void setUpTurtles(int count) {
		
		// randomly seed turtles to the world
		int max_initial_wealth = -1;
		for(int i = 0; i < count; i++) {
			// generate totally random turtle
			int held_grains = randomTurtle(-1, -1);
			max_initial_wealth = (max_initial_wealth > held_grains)? max_initial_wealth : held_grains;
			sanityCheck();

		}
		
		updateTurtleClass(max_initial_wealth);
	}
	
	private int randomTurtle(int x, int y) {
		Random rand = new Random();
		if(x < 0 || y < 0) {
			// random location when set up
			x = rand.nextInt(size);
			y = rand.nextInt(size);
		}else {
			// random neighbour location when leaving offspring
			int x_offset = 2*rand.nextInt(2)-1;
			int y_offset = 2*rand.nextInt(2)-1;
			x = (x + x_offset + this.size) % this.size;
			y = (y + y_offset + this.size) % this.size;
		}
		int life_expectancty = rand.nextInt(max_life-min_life + 1) + min_life;
		int age = rand.nextInt(life_expectancty);
		int metabolism = 1 + rand.nextInt(max_metabolism);
		int held_grains = metabolism + rand.nextInt(50);
		int vision = 1 + rand.nextInt(max_vision);
		int heading = rand.nextInt(4);
		Turtle t = new Turtle(life_expectancty, vision, metabolism, heading, held_grains, age);
		this.turtles[x][y].add(t);
		return held_grains;
	}
	private void setUpPatches() {
		// randomly put grains in the world
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				Patch p = new Patch(this.max_grain, this.regrow_amount, this.regrow_intervel);
				patches[x][y] = p;
			}
		}
		
		// haven't done yet
	}
	private void sanityCheck() {
		int count = 0;
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				count+= this.turtles[x][y].size();
			}
		}
		System.out.println("Current Tick: " + tick+ "\nTurtles count: "+count);
	}
	public static void main(String args[]) {
		World w = new World();
		w.setUp();
		for(int i = 0;  i < 100000; i++) {
			w.go();
		}	
	}
}
