package wealth_distribution;

import java.io.FileWriter;
import java.io.IOException;

public class CsvFileWriter {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "tick";

	
	public void writeCsvFile(String fileName, int tick) {
		
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(Integer.toString(tick));
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.flush();
			fileWriter.close();
			
		}
		catch (Exception e) {
			
			System.out.println("Error in CsvFileWriter !!!");
		
			e.printStackTrace();

		}
		

	}
}