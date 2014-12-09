package com.sonification.audio;

import java.io.File;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Noise;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Spatial;
import net.beadsproject.beads.ugens.WavePlayer;

import com.sonification.data.DataConverter;

public class AudioTestMain {
	

	public void testing()
	{
		//Was testing spatial to get sound in two different headphones. Found out that
		//the panning uGen is the correct way to do this
		AudioContext audioContext = new AudioContext();
		
		float[][] locations = new float[8][3]; //Array for the speaker locations
		locations[0][0] = 0f; //First speaker in dimension 1
		locations[1][0] = 0f; //Second speaker in dimension 1
		locations[2][0] = 0f; //First speaker in dimension 1
		locations[3][0] = 0f; //Second speaker in dimension 1
		locations[4][0] = 0f; //First speaker in dimension 1
		locations[5][0] = 0f; //Second speaker in dimension 1
		locations[6][0] = 0f; //First speaker in dimension 1
		locations[7][0] = 0f; //Second speaker in dimension 1
		locations[0][1] = 0f; //First speaker in dimension 1
		locations[1][1] = 0f; //Second speaker in dimension 1
		locations[2][1] = 0f; //First speaker in dimension 1
		locations[3][1] = 0f; //Second speaker in dimension 1
		locations[4][1] = 0f; //First speaker in dimension 1
		locations[5][1] = 0f; //Second speaker in dimension 1
		locations[6][1] = 0f; //First speaker in dimension 1
		locations[7][1] = -1f; //Second speaker in dimension 1
		locations[0][2] = -1f; //First speaker in dimension 1
		locations[1][2] = -1f; //Second speaker in dimension 1
		locations[2][2] = -1f; //First speaker in dimension 1
		locations[3][2] = -1f; //Second speaker in dimension 1
		locations[4][2] = -1f; //First speaker in dimension 1
		locations[5][2] = -1f; //Second speaker in dimension 1
		locations[6][2] = -1f; //First speaker in dimension 1
		locations[7][2] = -1f; //Second speaker in dimension 1
		
		Spatial spatial = new Spatial(
				audioContext, //Audio Context
				3, //Number of dimensions
				locations, //Array containing the locations of the speakers
				180 //Diamater of the circle where speakers exist
		);
		Gain gain = new Gain(audioContext, 1, 1f); //Gain controls volume (Audio context, dimension, starting value[Max:1])

		Envelope intervalEnvelope = new Envelope(audioContext, 0); //Encelope provides a value that changes over a given amount of time
		intervalEnvelope.addSegment(1000, 1000); //(frequency, duration in miliseconds)
		intervalEnvelope.addSegment(600, 1000);
		intervalEnvelope.addSegment(1000, 1000);
		intervalEnvelope.addSegment(400, 1000);
		intervalEnvelope.addSegment(0, 1000);
		//Wave player produces sound. 
		WavePlayer wp = new WavePlayer(audioContext, intervalEnvelope, Buffer.SINE);//(AudioContext,Frequency,Type of sound)
		
		gain.addInput(wp);
		spatial.addInput(gain);
		audioContext.out.addInput(spatial); //If you replace spatial here for gain it makes sound
		audioContext.start();
	}

	//Lesson 1
	public void Lesson1()
	{
		AudioContext audioContext = new AudioContext();
		//This is a UGen. UGens always have some form of audio inputs and outputs and audio processing/generation.
		//All UGens take in a Audio Context
		Noise noise = new Noise(audioContext);
		
		//This is a gain contol UGen. It takes in the numbe of channels and initial gain level repectivly. 
		Gain gain = new Gain(audioContext, 1, 0.1f);
		
		//UGens can be added to eachother to combine effects.
		gain.addInput(noise);
		//Add to the context to play.
		audioContext.out.addInput(gain);
		
		//Play your sounds.
		audioContext.start();
	}

	//Lesson 2
	public void Lesson2()
	{
		AudioContext audioContext = new AudioContext();
		//Envelopes are used to modify other UGen objects.
		Envelope freqEnv = new Envelope(audioContext,500);
		WavePlayer wavePlay = new WavePlayer(audioContext, freqEnv, Buffer.SINE);
		
		freqEnv.addSegment(1000, 1000);
		
		Gain gain = new Gain(audioContext, 1, 0.1f);
		gain.addInput(wavePlay);
		audioContext.out.addInput(gain);
		audioContext.start();
	}
	
	//Lesson 3
	public void Lesson3()
	{
		AudioContext audioContext = new AudioContext();
		WavePlayer freqModulator = new WavePlayer(audioContext, 50, Buffer.SINE);
		
		Function function = new Function(freqModulator)
		{
			public float calculate() 
			{
				return x[0] * 100.0f + 600.0f;
			}
		};
		
		WavePlayer wp = new WavePlayer(audioContext, function, Buffer.SINE);
		
		Gain g = new Gain(audioContext, 1, 0.1f);
		g.addInput(wp);
		audioContext.out.addInput(g);
		audioContext.start();
	}
	
	//Lesson4
	public void Lesson4()
	{
		AudioContext audioContext = new AudioContext();
		String audioFile = "audio/1234.aif";
		//SampleManager.setBufferRegime(Sample.Regime.newStreamingRegime(1000));
		SamplePlayer player = new SamplePlayer(audioContext, SampleManager.sample(audioFile));
		
		Gain g = new Gain(audioContext, 1, 0.1f);
		g.addInput(player);
		audioContext.out.addInput(g);
		audioContext.start();
	}
	
	//Lesson5
	public void Lesson5()
	{
		AudioContext audioContext = new AudioContext();
		Envelope intervalEnvelope = new Envelope(audioContext, 1000);
		intervalEnvelope.addSegment(600, 10000);
		intervalEnvelope.addSegment(1000, 10000);
		intervalEnvelope.addSegment(400, 10000);
		intervalEnvelope.addSegment(1000, 10000);
		
		Clock clock = new Clock(audioContext, intervalEnvelope);
		clock.setClick(true);
		
		audioContext.out.addDependent(clock);
		audioContext.start();
	}
	
	//Lesson7
	public void Lesson7()
	{
		final AudioContext audioContext = new AudioContext();
		/*
		 * In this example a Clock is used to trigger events. We do this by
		 * adding a listener to the Clock (which is of type Bead).
		 * 
		 * The Bead is made on-the-fly. All we have to do is to give the Bead a
		 * callback method to make notes.
		 * 
		 * This example is more sophisticated than the previous ones. It uses
		 * nested code.
		 */
		Clock clock = new Clock(audioContext, 700);
		clock.addMessageListener(
				  //this is the on-the-fly bead
				  new Bead() {
				    //this is the method that we override to make the Bead do something
				    int pitch;
				     public void messageReceived(Bead message) {
				        Clock c = (Clock)message;
				        if(c.isBeat()) {
				        	/*
				          //choose some nice frequencies
				          if(random(1) < 0.5) return;
				          pitch = Pitch.forceToScale((int)random(12), Pitch.dorian);
				          float freq = Pitch.mtof(pitch + (int)random(5) * 12 + 32);
				          WavePlayer wp = new WavePlayer(audioContext, freq, Buffer.SINE);
				          Gain g = new Gain(audioContext, 1, new Envelope(audioContext, 0));
				          g.addInput(wp);
				          audioContext.out.addInput(g);
				          ((Envelope)g.getGainUGen()).addSegment(0.1f, random(200));
				          ((Envelope)g.getGainUGen()).addSegment(0, random(7000), new KillTrigger(g));
				          */
				       }
				       if(c.getCount() % 4 == 0) {
				    	   /*
				           //choose some nice frequencies
				          int pitchAlt = pitch;
				          if(random(1) < 0.2) pitchAlt = Pitch.forceToScale((int)random(12), Pitch.dorian) + (int)random(2) * 12;
				          float freq = Pitch.mtof(pitchAlt + 32);
				          WavePlayer wp = new WavePlayer(audioContext, freq, Buffer.SQUARE);
				          Gain g = new Gain(audioContext, 1, new Envelope(audioContext, 0));
				          g.addInput(wp);
				          Panner p = new Panner(audioContext, random(1));
				          p.addInput(g);
				          audioContext.out.addInput(p);
				          ((Envelope)g.getGainUGen()).addSegment(random(0.1), random(50));
				          ((Envelope)g.getGainUGen()).addSegment(0, random(400), new KillTrigger(p));
				          */
				       }
				       if(c.getCount() % 4 == 0) {
				          Noise n = new Noise(audioContext);
				          WavePlayer wp = new WavePlayer(audioContext, 500, Buffer.SINE);
				          Gain g = new Gain(audioContext, 1, new Envelope(audioContext, 0.05f));
				          g.addInput(wp);
				          Panner p = new Panner(audioContext, 0);
				          p.addInput(g);
				          audioContext.out.addInput(p);
				          ((Envelope)g.getGainUGen()).addSegment(0, 100, new KillTrigger(p));
				       }
				     }
				   }
				 );
		audioContext.out.addDependent(clock);
		audioContext.start();
	}
	
	public static float random(double x) {
		return (float)(Math.random() * x);
	}
}
