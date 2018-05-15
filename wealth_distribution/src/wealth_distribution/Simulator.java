package wealth_distribution;

public class Simulator {
	
	public static void main(String args[]) {
		World w = new World();
		w.setUp();
		String filename = "OUTPUT.csv";
		CsvFileWriter writer = new CsvFileWriter(filename);
		for(int i = 0;  i < 100; i++) {
			w.go();
			writer.writeCsvFile(w);
		}
		
		
	}
	
	

}
