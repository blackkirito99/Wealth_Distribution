package wealth_distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	private Patch[][] patches;
	private List<Turtle>[][] turtles;
	private int max_grain;
	private int regrow_amount;
	private int regrow_intervel;
	private int max_life;
	private int min_life;
	private int max_metabolism;
	private int max_vision;
	private int size = 100;
	private int tick;
	public World(){
		this.patches= new Patch[size][size];
		//this.turtles= new ArrayList<Turtle> [size][size];
		setUp();
	}
	
	public void setUp() {
		setUpTurtles(100);
		setUpPatches();
		this.tick = 1;
		this.max_grain = 50;
	}
	
	public void go() {
		turnTowardsGrains();
		int richest_amount = harvestGrains();
		moveEatAgeDie();
		updateTurtleClass(richest_amount);
	}
	
	
	private void updateTurtleClass(int richest_amount) {
		// TODO Auto-generated method stub
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y] != null && this.turtles[x][y].size() > 0) {
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
		
		
		
		int richest_amount = 0;
		//eat & age & die
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y] != null && this.turtles[x][y].size() > 0) {
					for(Turtle t: this.turtles[x][y]) {
						t.eatGrains();
						t.growUp();
						
						// die, generates an offspring
						if(!t.checkSurvive()) {
							int grains_amount = randomTurtle(x, y);
							richest_amount = (richest_amount > grains_amount) ? richest_amount : grains_amount;
						}
					}
				}
			}
		}
		return richest_amount;
	}

	private int harvestGrains() {
		// TODO Auto-generated method stub
		int richest_amount = 0;
		for(int x = 0; x < this.size; x++) {
			for(int y = 0; y < this.size; y++) {
				if(this.turtles[x][y] != null && this.turtles[x][y].size() > 0) {
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
		
	}

	private void setUpTurtles(int count) {
		
		// randomly seed turtles to the world
		int max_initial_wealth = -1;
		for(int i = 0; i < count; i++) {
			// generate totally random turtle
			int held_grains = randomTurtle(-1, -1);
			max_initial_wealth = (max_initial_wealth > held_grains)? max_initial_wealth : held_grains;
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
			x += x_offset;
			y += x_offset;
		}
		int life_expectancty = rand.nextInt(max_life-min_life + 1) + min_life;
		int age = rand.nextInt(life_expectancty);
		int metabolism = 1 + rand.nextInt(max_metabolism);
		int held_grains = metabolism + rand.nextInt(50);
		int vision = 1 + rand.nextInt(max_vision);
		int heading = rand.nextInt(4);
		Turtle t = new Turtle(life_expectancty, vision, metabolism, heading, held_grains, age);
		if(this.turtles[x][y] == null) {
			ArrayList<Turtle> turtles_at_place = new ArrayList<Turtle>();
			turtles_at_place.add(t);
			this.turtles[x][y] = turtles_at_place;
		}else {
			this.turtles[x][y].add(t);
		}
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
	}
}
