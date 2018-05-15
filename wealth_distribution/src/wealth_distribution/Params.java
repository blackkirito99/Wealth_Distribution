package wealth_distribution;

import java.util.Random;


public class Params {
  public final static int WORKING_CLASS = 0;
  
  public final static int MIDDLE_CLASS = 1;
  
  public final static int UPPER_CLASS = 2;

  public final static int NUMPEOPLE = 250;
  
  public final static int VISION_MAX = 5;
  
  public final static int METABOLISM_MAX = 15;
  
  public final static int LIFE_MIN = 1;
  
  public final static int LIFE_MAX = 83;
  
  public final static int BEST_LAND = 100;
  
  public final static int PERCENT_BEST_LAND = 10;
  
  public final static int INTERVAL = 1;
  
  public final static int GRWON_NUM= 4;
  
  public final static int INITIAL_WEALTH = 50;
  
  public final static int GRAIN_MAX = 50;
  
  public final static int NOGRAIN = 0;
  
  public final static int MAP_SIZE = 100;
  
  public final static double DIFFUSE = 0.25;

  //offspring will inherit 80% the wealth of the parent
  public final static double INHERIT_PERCENT = 0.5;
  

  public static int randomLifeExpectancy() {
    Random random = new Random();
    int age = random.nextInt(LIFE_MAX - LIFE_MIN + 1) + LIFE_MIN;
    return age;
  }

  public static int randomVision() {
    Random random = new Random();
    int vision = random.nextInt(VISION_MAX) + 1;
    return vision;
  }

  public static int randomWealth() {
    Random random = new Random();
    int wealth = random.nextInt(INITIAL_WEALTH);
    return wealth;
  }
  public static int randomLocation() {
	Random random = new Random();
	int location = random.nextInt(MAP_SIZE);
	return location;
  }
  public static int randomMetabolism() {
	Random random = new Random();
	int meta = random.nextInt(METABOLISM_MAX) + 1;
	return meta;
  }
  
  public static boolean isBestLand() {
	  Random random = new Random();
	  int prob = random.nextInt(BEST_LAND);
	  boolean isBestLand;
	  if(prob < PERCENT_BEST_LAND) {
		  isBestLand = true;
	  }
	  else {
		  isBestLand = false;
	  }
	  return isBestLand;
  }
  public static int randomAge(int life) {
	  Random random = new Random();
	  int age = random.nextInt(life);
	  return age;
  }
}

