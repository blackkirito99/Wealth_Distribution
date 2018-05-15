package wealth_distribution;
/**
 * Simulator which has the main function written in
 * Simulator will run the java model and prints out important data out
 * on the command line and into the csv file
 * @author runze
 *
 */

public class Simulator {
	
	public static void main(String args[]) {
		World w = new World();
		w.setUp();
		CsvFileWriter writer = new CsvFileWriter(Params.FILENAME);
		for(int i = 0;  i < 200; i++) {
			w.go();
			writer.writeCsvFile(w);
		}
		
		writer.closeCsvFile();
	}
	
	

}
