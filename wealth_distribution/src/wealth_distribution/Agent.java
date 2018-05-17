package wealth_distribution;

/**
 * An abstract class
 * the base class for different types of agents to derive from
 * in case more agents join the model in the future
 * @author runze
 *
 */

public abstract class Agent {
	
	public abstract void updateLocation(int x_loc, int y_loc);
	
	public abstract void findOptimalPath(Patch[][] map);
	
	
}
