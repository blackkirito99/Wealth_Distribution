package wealth_distribution;

import java.util.Random;


public class Params {

  public static int NUMPEOPLE = 250;
  
  public static int VISION_MAX = 5;
  
  public static int METABOLISM_MAX = 15;
  
  public static int LIFE_MIN = 1;
  
  public static int LIFE_MAX = 83;
  
  public static int BEST_LAND = 100;
  
  public static int PERCENT_BEST_LAND = 10;
  
  public static int INTERVAL = 1;
  
  public static int GRWON_NUM= 4;
  
  public static int INITIAL_WEALTH = 50;
  
  public static int GRAIN_MAX = 50;
  
  public static int NOGRAIN = 0;
  
  public static int MAP_SIZE = 20;
  
  public static double DIFFUSE = 0.25;

  

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
}

