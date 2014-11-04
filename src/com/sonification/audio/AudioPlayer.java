package com.sonification.audio;

//Lesson1
//import net.beadsproject.beads.core.AudioContext;
//import net.beadsproject.beads.ugens.Gain;
//import net.beadsproject.beads.ugens.Noise;

//Lesson 2
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class AudioPlayer {
	public static void main(String args)
	{
		//Lesson 1
		/* 
		//Audio Context is needed to produce sounds 
		AudioContext ac;
		ac = new AudioContext();
		
		//This is a UGen. UGens always have some form of audio inputs and outputs and audio processing/generation.
		//All UGens take in a Audio Context
		Noise noise = new Noise(ac);
		
		//This is a gain contol UGen. It takes in the numbe of channels and initial gain level repectivly. 
		Gain gain = new Gain(ac, 1, 0.1f);
		
		//UGens can be added to eachother to combine effects.
		gain.addInput(noise);
		//Add to the context to play.
		ac.out.addInput(gain);
		
		//Play your sounds.
		ac.start();
		 */
	
		//Lesson 2
		AudioContext ac = new AudioContext();
		//Envelopes are used to modify other UGen objects.
		Envelope freqEnv = new Envelope(ac,500);
		WavePlayer wavePlay = new WavePlayer(ac, freqEnv, Buffer.SINE);
		
		freqEnv.addSegment(1000, 1000);
		
		Gain gain = new Gain(ac, 1, 0.1f);
		gain.addInput(wavePlay);
		ac.out.addInput(gain);
		ac.start();
	}
}
