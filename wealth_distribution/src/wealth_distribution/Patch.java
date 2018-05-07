package wealth_distribution;

public class Patch {
	private int max_grain;
	private int current_grain;
	private int x;
	private int y;
	public Patch(int x, int y, int max_grain) {
		this.x = x;
		this.y = y;
		this.max_grain = max_grain;
		this.current_grain = this.max_grain;
	}
}
