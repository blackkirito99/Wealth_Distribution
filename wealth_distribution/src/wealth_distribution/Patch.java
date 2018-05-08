package wealth_distribution;

public class Patch {
	private int max_grain;
	private int current_grain;
	private int regrow_amount; 
	private int regrow_intervel;
	private int turtle_on_patch;
	private boolean isBestLand;
	public Patch(int regrow_amount, int regrow_intervel) {
		//this.max_grain = max_grain;
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
	public void maxInitialisation(int maximum) {
		this.max_grain = this.isBestLand? maximum :this.current_grain;
	}
	public int harvetGrains() {
		int harvested_grain = this.current_grain;
		this.current_grain = 0;
		return harvested_grain;
	}
	
	public void growGrains(int tick) {
		if(tick % this.regrow_intervel == 0) {
			this.current_grain += this.regrow_amount;
			if(this.current_grain > this.max_grain) {
				this.current_grain = this.max_grain;
			}
		}
	}
	
	public int getCurrentGrains() {
		return this.current_grain;
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
