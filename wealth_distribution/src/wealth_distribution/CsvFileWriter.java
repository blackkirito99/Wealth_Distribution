package wealth_distribution;

import java.io.FileWriter;
import java.io.IOException;

public class CsvFileWriter {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "tick,low,medium,high,gini";
	private FileWriter fileWriter;
	
	public CsvFileWriter(String fileName) {
		
		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void writeCsvFile(World world) {
		
		int tick = world.getTick();
		int low = world.getWorkingClass();
		int medium = world.getMiddleClass();
		int high = world.getUpperClass();
		double gini = world.getGiniIndex();
		
		try {
			
			fileWriter.append(Integer.toString(tick));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(Integer.toString(low));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(Integer.toString(medium));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(Integer.toString(high));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(Double.toString(gini));
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.flush();
			//fileWriter.close();
			
		}
		catch (Exception e) {
			
			System.out.println("Error in CsvFileWriter !!!");
		
			e.printStackTrace();

		}
		

	}
	
	public void closeCsvFile() {
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}