package com.sonification.audio;

//Lesson1
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sonification.data.Data;
import com.sonification.data.DataConverter;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.WavePlayer;
import net.beadsproject.beads.events.KillTrigger;

//Lesson 5
import net.beadsproject.beads.ugens.Clock;

public class AudioPlayer {
	
	private AudioContext audioContext;
	private WavePlayer[] players;
	private  final int interval = 500;
	public AudioPlayer(Data data)
	{
		//Audio Context is needed to produce sounds
		audioContext = new AudioContext();
		players = WavePlayerFromData(data);
	}
	
	public void Start(int graph)
	{	
		//The clock is used to make the notes play at intervals 
		//rather than constantly
		Clock clock = new Clock(audioContext, interval); 
		clock.addMessageListener(
				new Bead(){
					private WavePlayer wavePlayer;
					public void messageReceived(Bead message){
						Clock c = (Clock)message;
						if(c.isBeat()){
					          Gain g = new Gain(audioContext, 1, new Envelope(audioContext, 1f));
					          g.addInput(wavePlayer);
					          Panner p = new Panner(audioContext, 0);
					          p.addInput(g);
					          audioContext.out.addInput(p);
					          ((Envelope)g.getGainUGen()).addSegment(0, interval+interval/10, new KillTrigger(p));
					    }
						if(wavePlayer.getFrequency() < 0)
						{
							c.pause(true);
						}
					}
					public Bead setInterval(WavePlayer nWavePlayer){
						wavePlayer = nWavePlayer;
						return this;
					}
				}.setInterval(players[1]) //The players index determins which line on the graph to play
				);
		
		
		
		audioContext.out.addDependent(clock);
		audioContext.start();
	}
	
	/**
	 * Generate a array of WavePlayers that will be based on the data
	 * @param data
	 * @return array of WavePlayers
	 */
	public WavePlayer[] WavePlayerFromData(Data data)
	{
		float[][] parsedData = ParseData(data);
		
		//The number of columns or different lines that will be represented on the graph
		WavePlayer[] wpArray = new WavePlayer[parsedData.length];
		float ranges[][] = FindRange(parsedData);
		float num, value;
		
		Envelope intervalEnvelope = null;
		for(int i = 0; i < parsedData.length; i++)
		{
			intervalEnvelope  = new Envelope(audioContext, 0);
			
			for(int n = 0; n < parsedData[i].length; n++)
			{
				value = parsedData[i][n];
				num = (36*(value-ranges[i][0]))/ranges[i][2] + 28; //28 = starting note. 36 = range of notes. max is 28+36
				num = (float) (Math.pow(2, (num-49)/12)* 440); //Piano freq equation
				intervalEnvelope.addSegment(num,0); //Add a instant change to the new note.
				intervalEnvelope.addSegment(num,interval); //Hold the note for the length of the interval
			}
			intervalEnvelope.addSegment(-1,0); //stop playing sound when it is over
			wpArray[i] = new WavePlayer(audioContext, intervalEnvelope, Buffer.SINE); //Create the wavePlayer using the calcualted data
		}
		
		return wpArray;
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



	public static void main(String[] args)
	{
		AudioPlayer ap = new AudioPlayer(new DataConverter(new File("/Users/Student/Downloads/testingcsv.csv")).readCSVFile());
		ap.Start(0);

		//AudioTestMain test = new AudioTestMain();
		//test.Lesson1();
		//test.Lesson2();
		//test.Lesson3();
		//test.Lesson4();
		//test.Lesson5();
		//test.Lesson7();
		//test..testing();
	}
}