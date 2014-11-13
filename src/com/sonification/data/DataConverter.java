package com.sonification.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataConverter {

	public enum FileType {
		CSV
	}
	
	private File file;
	
	public DataConverter(File file){
		this.file = file;
	}
	
	public FileType getFileType(){
		// TODO get file type
		return FileType.CSV;
	}
	
	public Data readCSVFile() {
		String[] titles = {""};
		ArrayList<String[]> data = new ArrayList<String[]>();
		BufferedReader br = null;
		try {
			String line = "";
			br = new BufferedReader(new FileReader(file));
			titles = br.readLine().split(",");
			while ((line = br.readLine()) != null){
				data.add(line.split(","));
			}
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
		} catch (IOException e) {
			System.err.println("File Could Not be Read");
		} finally {
			try {
				if (br != null) { br.close(); }
			} catch (IOException e) {
				System.err.println("Error when closing file");
			}
		}
		return new Data(titles, data);
		
	}
	
	public static void main(String[] args) {
		Data data = new DataConverter(new File("/Users/Student/Downloads/download.csv")).readCSVFile();
		System.out.println(data.getTitlesStr());
		System.out.println(data.getDataStr());
		System.out.println("\n");
		for (String d: data.get("\"name\"")){
			System.out.println(d);
		}
	}

}
