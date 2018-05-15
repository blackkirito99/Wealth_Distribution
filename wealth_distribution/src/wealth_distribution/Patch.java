package wealth_distribution;
/**
 * A tile on the map with grains grown on it
 * Grains can re-grow over a period of time
 * @author runze
 *
 */


public class Patch {
	//maximum number of grain allowed to grow on the tile
	private int max_grain;
	//current grains on the tile
	private double current_grain = Params.NOGRAIN; 
	//number of grains re-grow
	private int regrow_amount = Params.GRWON_NUM;
	//period of time for grains to grow
	private int regrow_intervel = Params.INTERVAL;
	//total number of turtles current on the tile
	private int turtle_on_patch = 0;
	//percentage of grains that will be spread to neighbors
	private double diffuse = Params.DIFFUSE;
	//percentage of grains remaining after spreading
	private double remain = 1 - diffuse;
	// boolean if patch is best land where can hold maximum grain possible
	private boolean isBestLand = false; 
	
	//constructor
	public Patch() {
		// tiles being a best land or not is totally random
		if(Params.isBestLand()) {
			current_grain = Params.GRAIN_MAX;
			max_grain = Params.GRAIN_MAX;
			isBestLand = true;
		}
		
	}
	
	//setter for grain
	public void setGrains(double amount) {
		current_grain = amount;
	}
	
	//add grains on the tile 
	public void addGrains(double amount) {
		current_grain += amount;
	}
	
	//round the number of grain for simplicity
	//also make the current model consistent with the original model in netlogo
	public void finalGrainsInitilization() {
		current_grain = Math.floor(current_grain);
		max_grain = (int)(current_grain);
	}
	
	//return the number of grain each turtle will get from harvesting
	public int harvested() {
		int harvested_grain = (int)(current_grain/turtle_on_patch);
		return harvested_grain;
	}
	
	// re-grow grains if timing is right
	public void growGrains(int tick) {
		if(tick % regrow_intervel == 0) {
			current_grain += regrow_amount;
			if((int)current_grain > max_grain) {
				current_grain = max_grain;
			}
		}
	}
	
	//number of grains drops after spreading grains to neighbors
	public void grainDiffuse() {
		current_grain = current_grain * remain;
	}
	
	
	public double getCurrentGrains() {
		return current_grain;
	}
	
	//return number of grains that will be spread to neighbors
	public double getDiffuseAmount() {
		double amount = current_grain * diffuse / 8.0;
		return amount;
	}
	
	public int getMaxGrain() {
		return max_grain;
	}
	
	public void setBestLand() {
		isBestLand = true;
	}
	public boolean isBestLand() {
		return isBestLand;
	}
	
	//a turtle leave the tile
	public void turtleLeave() {
		turtle_on_patch--;
	}
	
	//a turtle enters the tile
	public void turtleEnter() {
		turtle_on_patch++;
	}
	
	public int getTurtleCount() {
		return this.turtle_on_patch;
	}
	
	//grains exhausted after being harvested
	public void clear() {
		current_grain = 0;
	}
	
	//spread grains to the tile's neighbors
	//diffuse function also appeared in the netlogo model of wealth distribution
	public void diffuse(int x, int y, Patch[][] patches) {
		patches[x][y].grainDiffuse();
		double amount = patches[x][y].getDiffuseAmount();
		int size = Params.MAP_SIZE;
		
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
	
	

	
	
}
