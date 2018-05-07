package wealth_distribution;

import java.util.List;

public class Map {
	private List<Patch> patches;
	private List<Turtle> turtles;
	private int max_grain;
	private int max_life;
	private int min_life;
	private int max_metabolism;
	private int max_vision;
	public Map(){
		setUpTurtles(100);
		setUpPatches(100);
	}
	
	public void go() {
		
	}
	
	
	private void setUpTurtles(int size) {
		// TODO Auto-generated method stub
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				Turtle t = new Turtle(i, j, min_life, max_life, max_metabolism, max_vision);
				turtles.add(t);
			}
		}
		
	}
	private void setUpPatches(int size) {
		// TODO Auto-generated method stub
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				Patch p = new Patch(i, j, max_grain);
				patches.add(p);
			}
		}
	}
}
