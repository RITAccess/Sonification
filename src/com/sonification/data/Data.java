package com.sonification.data;

import java.util.ArrayList;

public class Data {
	private String[] titles;
	private String[][] data;
	
	/**
	 * Construct a Data object with titles and data
	 * @param titles - an array of titles 
	 * @param data - an arraylist of arrays of data
	 */
	public Data(String[] titles, ArrayList<String[]> data){
		this.titles = titles;
		this.data = ArrayListToArray(data);
	}
	
	/**
	 * Construct a Data object with no titles
	 * @param data - an arraylist of arrays of data
	 */
	public Data(ArrayList<String[]> data){
		this.data = ArrayListToArray(data);
	}
	
	private String[][] ArrayListToArray(ArrayList<String[]> data){
		String[][] arrData = new String[data.size()][];
		for (int i = 0; i < data.size(); i++){
			arrData[i] = data.get(i);
		}
		return arrData;
	}
	
	public String[] get(String title){
		int titleIndex = 0;
		for (String t: this.titles){
			if (t.equalsIgnoreCase(title)){
				return this.get(titleIndex);
			} else {
				titleIndex++;
			}
		}
		System.err.println("title not in data");
		return new String[]{};
	}
	
	public String[] get(int index){
		String[] valArr = new String[this.data.length]; 
		for (int i= 0; i < this.data.length; i++){
			valArr[i] = this.data[i][index];
		}
		return valArr;
	}
	
	public String[] getTitles(){
		return this.titles;
	}
	
	public String[][] getData(){
		return this.data;
	}
	
	public String getTitlesStr(){
		StringBuilder sb = new StringBuilder();
		for (String t: this.titles){
			sb.append(t + ", ");
		}
		sb.replace(sb.length() - 2, sb.length(), "");
		return sb.toString();
	}
	
	public String getDataStr(){
		StringBuilder sb = new StringBuilder();
		for (String[] row: this.data){
			for (String d: row){
				sb.append(d + ", ");
			}
			sb.replace(sb.length() - 2, sb.length(), "");
			sb.append("\n");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}
}
