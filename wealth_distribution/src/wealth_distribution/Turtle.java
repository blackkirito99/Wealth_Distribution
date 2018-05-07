package wealth_distribution;

import java.util.Random;

public class Turtle {
	private int held_grains;
	private int age;
	private int vision;
	private int metabolism;
	private int life_expectancty;
	private int x;
	private int y;
	private int heading;
	
	public Turtle(int x, int y, int max_life, int min_life, int max_metabolism, int max_vision) {
		Random rand = new Random();
		this.x = x;
		this.y = y;
		this.age = 1;
		this.life_expectancty = rand.nextInt(max_life-min_life + 1) + min_life;
		this.metabolism = 1 + rand.nextInt(max_metabolism);
		this.held_grains = this.metabolism + rand.nextInt(50);
		this.vision = 1 + rand.nextInt(max_vision);
		this.heading = rand.nextInt(4);
	}
}
