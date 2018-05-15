package wealth_distribution;

/**
 * World where turtles interact with the entire map
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class World {
	//two dimensional array of patches, the map of the world
	private Patch[][] patches;
	//list of turtles which are the agents of the world
	private List<Turtle> turtles;
	//size of the map
	private int size = Params.MAP_SIZE;
	//time
	private int tick = 0;
	//number of turtles that belongs to working class
	private int workingClass;
	//number of turtles that belongs to middle class
	private int middleClass;
	//number of turtles that belongs to upper class
	private int upperClass;
	

	public World(){
		patches= new Patch[size][size];
		turtles = new ArrayList<Turtle>();
		
	}
	
	// set up the world
	public void setUp() {
		setUpPatches();
		setUpTurtles();
		Collections.sort(this.turtles);
		//sanityCheck();
	}
	
	//create the map
	public void setUpPatches() {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				Patch p = new Patch();
				patches[x][y] = p;
			}
		}
		// Spread that grain around the window a little and put a little back
		// into the patches that are the "best land" found above
		for(int i = 0; i < 5; i++) {
			for(int x = 0; x < size; x++) {
				for(int y = 0; y < size; y++) {
					if(patches[x][y].isBestLand()) {
						int maxGrain = patches[x][y].getMaxGrain();
						patches[x][y].setGrains(maxGrain);
					}
				}
			}
			
			for(int x = 0; x < size; x++) {
				for(int y = 0; y < size; y++) {
					patches[x][y].diffuse(x, y, patches);
				}
			}
			
		}
		//Spread the grain around some more
		for(int i = 0; i < 10; i++) {
			for(int x = 0; x < size; x++) {
				for(int y = 0; y < size; y++) {
					patches[x][y].diffuse(x, y, patches);
				}
			}
		}
		//initialize location maximum of grains on each tile
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				patches[x][y].finalGrainsInitilization();
			}
		}
		//printArray();
		
	}
	
	// randomly seed turtles to the world
	public void setUpTurtles() {
		for(int i = 0; i < Params.NUMPEOPLE; i++) {
			Turtle new_turtle = new Turtle();
			turtles.add(new_turtle);
			int xAxis = new_turtle.getX();
			int yAxis = new_turtle.getY();
			patches[xAxis][yAxis].turtleEnter();
		}
	
		updateTurtleClass(findMaxWealth());
		classInfo();
	}
	//find the wealthiest turtle and returns the wealth he/she has
	public int findMaxWealth() {
		int max = 0;
		for(Turtle people : turtles) {
			if(people.getCurrentGrains() > max) {
				max = people.getCurrentGrains();
			}
		}
		return max;
	}
	
	//group turtles in different classes(working/middle/upper)
	public void updateTurtleClass(int wealth) {
		for(Turtle t : turtles) {
			t.updateClass(wealth);
		}
		
	}
	
	//run everything in the world
	public void go() {
		tick++;
		updateDirection();
		harvestGrains();
		move();
		survive();
		patchesRegrow();
		updateTurtleClass(findMaxWealth());
		Collections.sort(turtles);
		//sanityCheck();	
		classInfo();

	}
	
	//shows number of turtles categorized in the three classes
	public void classInfo() {
		int low_count = 0;
		int medium_count = 0;
		int high_count = 0;
		for(int i = 0; i < turtles.size(); i++) {
			switch (turtles.get(i).getWealthClass()){
				case Params.WORKING_CLASS:
					low_count++;
					break;
				case Params.MIDDLE_CLASS:
					medium_count++;
					break;
				case Params.UPPER_CLASS:
					high_count++;
					break;
				default: 
					break;
			}
		}
		workingClass = low_count;
		middleClass = medium_count;
		upperClass = high_count;
		System.out.println("low: " + low_count + "; med: " + medium_count + "; high: " + high_count);
	}


	

	
	//every turtle find the best direction to march
	//using the same algorithm given in the original model
	public void updateDirection() {
		for(Turtle t : turtles) {
			t.findOptimalPath(patches);
			
		}
	}
	
	//every turtle harvest grains
	public void harvestGrains() {
		for(Turtle t : turtles) {
			t.harvest(patches);
		}
		clearPatches();
	}
	
	// set grain on patch to 0 if patches have been harvested
	public void clearPatches() {
		for(Patch[] rows : patches) {
			for(Patch p : rows) {
				if(p.getTurtleCount() > 0) {
					p.clear();
				}
			}
		}
	}
	
	//turtles move
	public void move() {
		for(Turtle t : turtles) {
			t.move(patches);
		}
		
	}
	
	//check if turtle could survive this tick
	//if not, turtles would generate an offspring 
	private void survive() {
		for(int i = 0; i < turtles.size(); i++) {
			Turtle t = turtles.get(i);
			t.eatGrains();
			t.growUp();
			if(!t.checkSurvive()) {
				// if die, produce an offspring
				patches[t.getX()][t.getY()].turtleLeave();
				Turtle offspring = new Turtle(t.getCurrentGrains());
				turtles.set(i, offspring);
				patches[offspring.getX()][offspring.getY()].turtleEnter();
				
				
			}
		}
	}
	
	// re-grow grains on each patch
	public void patchesRegrow() {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				patches[x][y].growGrains(tick);
			}
		}
		
	}

	
	public void sanityCheck() {
		// check total turtle count in patches remain unchanged
		int turtles_count = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				turtles_count += patches[x][y].getTurtleCount();
			}
		}
		System.out.println("Current Tick: " + tick+ "\nTurtles count: "+ turtles_count);
		for(int i = 0; i < this.turtles.size();i++) {
			System.out.println(i + ": " + this.turtles.get(i).getCurrentGrains());
		}
		
	}
	
	public int getWorkingClass() {
		return workingClass;
	}
	public int getMiddleClass() {
		return middleClass;
	}
	public int getUpperClass() {
		return upperClass;
	}
	public int getTick() {
		return tick;
	}
	
	//get the sum of grains held by each turtle
	public int totalWealth() {
		int total = 0;
		for(Turtle t : turtles) {
			total = total + t.getCurrentGrains();
		}
		return total;
	}
	
	//return gini index
	public double getGiniIndex() {
		int index = 0;
		int wealthSoFar = 0;
		int total = totalWealth();
		double gini = 0;
		while(index < turtles.size()) {
			wealthSoFar += turtles.get(index).getCurrentGrains();
			index++;
			gini += (double)index/turtles.size()-(double)wealthSoFar/total;
			
		}
		gini =  (gini/this.turtles.size())/0.5;
		System.out.println(gini);
		return gini;
		
	}
	

	public void printArray() {
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
	public void printTurtle() {
		int[] arrayDouble = new int[Params.NUMPEOPLE];
		for(int x = 0; x < Params.NUMPEOPLE; x++) {
				arrayDouble[x] = turtles.get(x).getCurrentGrains();
			
		}
			System.out.println("GRAINS OF TURTLE");  
			System.out.println(Arrays.toString(arrayDouble));  
		
			
	}
	
	

}
