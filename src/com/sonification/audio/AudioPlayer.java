package com.sonification.audio;

//Lesson1
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfugue.Pattern;
import org.jfugue.Player;

import com.sonification.data.Data;
import com.sonification.data.DataConverter;

public class AudioPlayer {
	
	//Keys to use
	private final int STARTKEY = 28;
	private final int ENDKEY = 64;
	private final int KEYRANGE = ENDKEY-STARTKEY;
	private final float AVERAGELENGTH = .125f; //1 = whole note, .25 = quarter note
	private final float MAXLENGTH = .75f;
	
	private String[] tracks;
	private Map<Float,String> map;
	
	/**
	 * An audio player that parses the provided data set and creates audio tracks from them.
	 * @param data
	 * @param xAndY
	 */
	public AudioPlayer(Data data, boolean xAndY)
	{	
		//Map with all the frequencies maped to their respective notes
		//LinkedHashMap to keep them in order
		map = new LinkedHashMap<Float,String>();
		
		map.put(130.813f, "C3"); //Key 28
		map.put(138.591f, "C#3");
		map.put(146.832f, "D3");
		map.put(155.563f, "D#3");
		map.put(164.814f, "E3");
		map.put(174.614f, "F3");
		map.put(184.997f, "F#3");
		map.put(195.998f, "G3");
		map.put(207.652f, "G#3");
		map.put(220.000f, "A3");
		map.put(233.082f, "A#3");
		map.put(246.942f, "B3");
		map.put(261.626f, "C4");
		map.put(277.183f, "C#4");
		map.put(293.665f, "D4");
		map.put(311.127f, "D#4");
		map.put(329.628f, "E4");
		map.put(349.228f, "F4");
		map.put(369.994f, "F#4");
		map.put(391.995f, "G4");
		map.put(415.305f, "G#4");
		map.put(440.000f, "A4");
		map.put(466.164f, "A#4");
		map.put(493.883f, "B4");
		map.put(523.251f, "C5");
		map.put(554.365f, "C#5");
		map.put(587.330f, "D5");
		map.put(622.254f, "D#5");
		map.put(659.255f, "E5");
		map.put(698.456f, "F5");
		map.put(739.989f, "F#5");
		map.put(783.991f, "G5");
		map.put(830.609f, "G#5");
		map.put(880.000f, "A5");
		map.put(932.328f, "A#5");
		map.put(987.767f, "B5");
		map.put(1046.50f, "C6"); //Key 64
		
		//Generate the tracks based on the data
		tracks = noteStringFromData(data, xAndY);
	}
	
	/**
	 * Play a graph
	 * @param graph
	 */
	public void start(int graph)
	{
		Player player = new Player();
		Pattern pattern = new Pattern(tracks[graph]);
		player.play(pattern);
	}
	
	/**
	 * Generate an array of Strings based on the data. 
	 * These strings can then be fed into a pattern to create a audio track.
	 * @param data
	 * @return array of Strings
	 */
	public String[] noteStringFromData(Data data, boolean xAndY)
	{
		float[][] parsedData = ParseData(data);
		
		//The number of columns or different lines that will be represented on the graph
		float ranges[][] = FindRange(parsedData);
		float num, value;
		
		int i;
		String[] noteArray;
		float[] timeArray = new float[parsedData[0].length];
		//If the graph has both dimentions defined then assume the first table represents 
		//the X value that is shared between all subsiquent tables
		if(xAndY)
		{
			i = 1;
			noteArray = new String[parsedData.length-1];
			timeArray = xToTime(parsedData[0], ranges[0][1]);
		}
		else
		{
			i = 0;
			noteArray = new String[parsedData.length];
		}
		
		int u = 0;
		System.out.println("Split");
		//Iterate over collumns
		while(i < parsedData.length)
		{
			noteArray[u] = "";
			//Iterate through rows
			for(int n = 0; n < parsedData[i].length; n++)
			{
				value = parsedData[i][n];
				//Piano freq equation
				num = (KEYRANGE*(value-ranges[i][0]))/ranges[i][2] + STARTKEY;
				num = (float) (Math.pow(2, (num-49)/12)* 440); 
				
				//Does this graph use X and Y coordinates?
				if(xAndY)
				{
					//find closest key to the frequency and the time corisponding with this index and add key to the track's string
					noteArray[u] += closestKey(num) + "/" + timeArray[n] + " ";
					System.out.println(timeArray[n]);
				}
				else
				{
					noteArray[u] += closestKey(num) + "/" + AVERAGELENGTH + " ";
				}
			}
			u++;
			i++;
		}
		return noteArray;
	}
	
	/**
	 * Calculates an array of timing values to be used for the notes
	 * @param xValues
	 * @param max X value
	 * @return
	 */
	public float[] xToTime(float[] xValues, float max)
	{
		float totalX = 0;
		int numPoints = xValues.length;
		
		float[] differences = new float[xValues.length-1];
		
		for(int i = 0; i < xValues.length-1; i++)
		{
			differences[i] = Math.abs(xValues[i+1] - xValues[i]);
		}
		/* Atempting to deal with outliers
		float firstQuartile = quartile(differences, .25f);
		float thirdQuartile = quartile(differences, .75f);
		float IQR = thirdQuartile - firstQuartile; 
		float outlierRange = IQR*1.5f;
		
		for(int i = differences.length-1; i > 0; i--)
		{
			if(differences[i] > outlierRange + thirdQuartile || differences[i] < firstQuartile - outlierRange)
			{
			
			}
		}
		*/
		for(float val : differences)
		{
			totalX += val;
		}
		
		float mean = totalX/numPoints;
		
		for(int i = 0; i < xValues.length-1; i++)
		{
			float diff = xValues[i+1] - xValues[i];
			if(diff < mean)
			{
				xValues[i] = (diff*AVERAGELENGTH)/mean;
				System.out.println((diff*AVERAGELENGTH)/mean);
			}
			else
			{
				xValues[i] = AVERAGELENGTH+((diff-mean)*AVERAGELENGTH*3)/(max - mean);
				System.out.println(AVERAGELENGTH+((diff-mean)*MAXLENGTH-AVERAGELENGTH)/(max - mean));
			}
		}
		xValues[xValues.length-1] = 1;
		for(float val : xValues)
		{
			System.out.println("Value: " + val);
		}
		return xValues;
	}
	
	/**
	 * Finds the ranges of values within a set of data. 
	 * @param data
	 * @param collumns
	 * @param rows
	 * @return A 2D array of the ranges. the first index referes to the column. The second is min, max, and range in that order
	 */
	public float[][] FindRange(float[][] data)
	{
		float[][] ranges = new float[data.length][3];
		float tempRange, temp, min, max;
		for(int i = 0; i < data.length; i++)
		{
			tempRange = 0;
			min = Float.POSITIVE_INFINITY;
			max = Float.NEGATIVE_INFINITY;
			for(int n = 0; n < data[i].length; n++)
			{
					temp = data[i][n];
					//System.out.println("Value: " + temp);
					if(temp < min)
					{
						min = temp;
					}
					if(temp > max)
					{
						max = temp;
					}
				
			}
			tempRange = max - min;
			min -= tempRange/10;
			max += tempRange/10;
			tempRange = max - min;
			ranges[i][0] = min;
			ranges[i][1] = max;
			ranges[i][2] = tempRange;
			//System.out.println("Range: " + tempRange + " Min: " + min + " Max: " + max);
		}
		return ranges;
	}
	
	/**
	 * Retrive the quartile value from an array
	 * Taken From: http://www.java2s.com/Code/Java/Collections-Data-Structure/Retrivethequartilevaluefromanarray.htm
     * @param values THe array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     *      for the upper-quartile use 75
     * @return
     */
    public float quartile(float[] values, float lowerPercent) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }

        // Rank order the values
        float[] v = new float[values.length];
        System.arraycopy(values, 0, v, 0, values.length);
        Arrays.sort(v);

        int n = (int) Math.round(v.length * lowerPercent / 100);
        
        return v[n];

    }
	
	/**
	 * Generates a 2D array of floats from the given data. 
	 * The array contains all columns and their valid values. 
	 * A column is considered invalid if over 25% cannot be parsed to a float.
	 * @param data
	 * @return
	 */
	public float[][] ParseData(Data data)
	{
		int numColumns = data.getTitles().length;
		List<float[]> myList = new ArrayList<float[]>();
		
		for(int i = 0; i < numColumns; ++i)
		{
			String[] tempColumn = data.get(i);
			float[] column = new float[tempColumn.length];
			
			int numInvalid = 0;
			float oneFourth = tempColumn.length * .25f;
			for(int n = 0; n < tempColumn.length; ++n)
			{
				try
				{
					column[n-numInvalid] = Float.parseFloat(tempColumn[n]);
					//System.out.println("Data: " + column[n]);
				}
				catch(Exception e)
				{
					System.err.println("Failed to parse! " + tempColumn[n]);
					numInvalid++;
				}
			}
			
			//If over 25% of the column is invalid then skip the column
			if(numInvalid < oneFourth)
			{
				float[] temp = new float[column.length-numInvalid];
				temp = Arrays.copyOf(column, temp.length);
				myList.add(temp);
			}
		}
		
		float[][] array = new float[myList.size()][];
		for(int i = 0; i < myList.size(); i++)
		{
			array[i] = myList.get(i);
			for(int n = 0; n < myList.get(i).length; n++)
			{
				array[i][n] = myList.get(i)[n];
				//System.out.println("DATA PRINTING: " + array[i][n]);
			}
		}
		
		return array;
	}
	/**
	 * Find the key that the provided frequency most closely aligns with.
	 * @param frequency
	 * @return
	 */
	public String closestKey(Float frequency)
	{
		if(map.containsKey(frequency))
			return map.get(frequency);
		
		Float lastKey = null;
		
		for(Map.Entry<Float, String> entry : map.entrySet())
		{
			if(entry.getKey() > frequency)
			{
				float diff = entry.getKey() - frequency;
				if(lastKey == null || diff < frequency - lastKey)
				{
					return entry.getValue();
				}
				return map.get(lastKey);
			}
			lastKey = entry.getKey();
		}
		
		return "";
	}

	public static void main(String[] args)
	{
		AudioPlayer ap = new AudioPlayer(new DataConverter(new File("/Users/Student/Downloads/testingcsv.csv")).readCSVFile(), true);
		ap.start(0);
	}
}