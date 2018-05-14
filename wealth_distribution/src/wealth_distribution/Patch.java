package wealth_distribution;

import java.util.Random;

public class Patch {
	private int max_grain;
	// use double for initialization, but always return integer to outside
	private double current_grain = Params.NOGRAIN; 
	private int regrow_amount = Params.GRWON_NUM;  
	private int regrow_intervel = Params.INTERVAL;
	private int turtle_on_patch = 0;
	private double diffuse = Params.DIFFUSE;
	private double remain = 1 - diffuse;
	// boolean if patch is best land where can hold maximum grain possible
	private boolean isBestLand = false; 
	
	public Patch() {
		if(Params.isBestLand()) {
			current_grain = Params.GRAIN_MAX;
			max_grain = Params.GRAIN_MAX;
			isBestLand = true;
		}
		
	}
	public void setGrains(double amount) {
		current_grain = amount;
	}
	public void addGrains(double amount) {
		current_grain += amount;
	}
	public void finalGrainsInitilization() {
		current_grain = Math.floor(current_grain);
		max_grain = (int)(current_grain);
	}
	public int harvested() {
		int harvested_grain = (int)(current_grain/turtle_on_patch);
		return harvested_grain;
	}
	
	// regrow grains if tick mode regrow_intervel is 0 
	//which means it is time to regrow
	public void growGrains(int tick) {
		if(tick % regrow_intervel == 0) {
			current_grain += regrow_amount;
			if((int)current_grain > max_grain) {
				current_grain = max_grain;
			}
		}
	}
	
	public void grainDiffuse() {
		current_grain = current_grain * remain;
	}
	
	
	public double getCurrentGrains() {
		return current_grain;
	}
	
	
	public double getDiffuseAmount() {
		double amount = current_grain * diffuse / 8.0;
		return amount;
	}
	
	public int getMaxGrain() {
		return max_grain;
	}
	
	public void setBestLand() {
		this.isBestLand = true;
	}
	public boolean isBestLand() {
		return this.isBestLand;
	}
	
	public void turtleLeave() {
		this.turtle_on_patch--;
	}
	public void turtleEnter() {
		this.turtle_on_patch++;
	}
	
	public int getTurtleCount() {
		return this.turtle_on_patch;
	}
	
	public void clear() {
		current_grain = 0;
	}
	
	
}
