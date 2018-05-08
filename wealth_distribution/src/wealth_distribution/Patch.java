package wealth_distribution;

public class Patch {
	private int max_grain;
	private int current_grain;
	private int regrow_amount; 
	private int regrow_intervel;
	private int x;
	private int y;
	public Patch(int max_grain, int regrow_amount, int regrow_intervel) {
		this.max_grain = max_grain;
		this.current_grain = this.max_grain;
		this.regrow_amount = regrow_amount;
		this.regrow_intervel = regrow_intervel;
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
	
	
}
