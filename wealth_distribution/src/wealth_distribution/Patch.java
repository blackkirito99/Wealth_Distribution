package wealth_distribution;

public class Patch {
	private int max_grain;
	private double current_grain; // use double for initialisation, but always return integer to outside
	private int regrow_amount;  
	private int regrow_intervel;
	private int turtle_on_patch;
	private boolean isBestLand; // boolean if patch is best land where can hold maximum grain possible
	
	public Patch(int regrow_amount, int regrow_intervel) {
		this.current_grain = 0;
		this.regrow_amount = regrow_amount;
		this.regrow_intervel = regrow_intervel;
		this.turtle_on_patch = 0;
	}
	public void setGrains(int amount) {
		this.current_grain = amount;
	}
	public void addGrains(int amount) {
		this.current_grain += amount;
	}
	public void finalGrainsInitilization(int maximum) {
		//round to integer number of grain
		this.current_grain = (int)(this.current_grain);
		this.max_grain = this.isBestLand? maximum : this.getCurrentGrains();
	}
	public int harvetGrains() {
		int harvested_grain = this.getCurrentGrains();
		this.current_grain = 0;
		return harvested_grain;
	}
	
	// regrow grains if tick mode regrow_intervel is 0 which means it is time to regrow
	public void growGrains(int tick) {
		if(tick % this.regrow_intervel == 0) {
			this.current_grain += this.regrow_amount;
			if(this.current_grain > this.max_grain) {
				this.current_grain = this.max_grain;
			}
		}
	}
	
	public int getCurrentGrains() {
		return (int)(this.current_grain);
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
	
	
}
