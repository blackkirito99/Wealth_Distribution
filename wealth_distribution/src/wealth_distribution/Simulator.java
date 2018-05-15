package wealth_distribution;

public class Simulator {
	
	public static void main(String args[]) {
		World w = new World();
		w.setUp();
		for(int i = 0;  i < 100; i++) {
			w.go();
		}
		
		CsvFileWriter writer = new CsvFileWriter();
		String filename = "OUTPUT.csv";
		writer.writeCsvFile(filename, 100);
	}

}
