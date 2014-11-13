package com.sonification.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sonification.data.Data;
import com.sonification.data.DataConverter;
 
 
public class Graph extends JPanel {

	private static final int SIZE_WIDTH = 800;
	private static final int SIZE_HEIGHT = 400;
	private static final int BORDER = 25;
	private GeneralPath path = new GeneralPath();
	private GeneralPath[] curves; 
	private Double[] smallest;
	private Double[] largest;
	private Double multiplyVar = 1.0;
	private Color[] colors;
	
	public Graph(Data data){
		Random rnd = new Random((long) Math.random()); // for colors of path
		
		smallest = new Double[data.getData()[0].length];
		largest = new Double[data.getData()[0].length];
		for (int i = 0; i < data.getData()[0].length; i++){
			for (String s : data.get(i)){
				if (isDouble(s)){
					double v = Double.parseDouble(s);
					if (smallest[i] == null) { smallest[i] = v; largest[i] = v; }
					if (v < smallest[i]) { smallest[i] = v; }
					if (v > largest[i]) { largest[i] = v; }
				}
			}
		}
		curves = new GeneralPath[data.getData()[0].length];
		colors = new Color[data.getData()[0].length];
		for (int i = 0; i < curves.length; i++){
			// Avoid columns with just String data, TODO check for other points besides just (i)[0]
			while (!isDouble(data.get(i)[0])) {
				i++;
			}
			GeneralPath p = new GeneralPath();
			multiplyVar = SIZE_HEIGHT / (largest[i] - smallest[i]);
			System.out.println(largest[i] + " - " + smallest[i] + " => " + (largest[i] - smallest[i]));
			System.out.println(multiplyVar);
			// Avoid rows with just String data
			int j = 0;
			while (!isDouble(data.get(i)[j])){ j++; }
			p.moveTo(0 + BORDER, SIZE_HEIGHT - (multiplyVar * (Double.parseDouble(data.get(i)[j]) - smallest[i])) + BORDER);
			System.out.println(p.getCurrentPoint());
			while (j < data.get(i).length) {
				if (isDouble(data.get(i)[j])){
					double v = SIZE_HEIGHT - (multiplyVar * (Double.parseDouble(data.get(i)[j]) - smallest[i]));
					p.lineTo(j*(SIZE_WIDTH / data.get(i).length) + BORDER, v + BORDER);
				}
				j++;
			}
			curves[i] = p;
			colors[i] = new Color(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat());
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SIZE_WIDTH + 50, SIZE_HEIGHT + 50);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		int w = this.getWidth();
		int h = this.getHeight();

		/*
		 * Drawing grid
		 */
		path.reset();
		for (double t = 0; t < w * 2; t = t + 20) {
			path.moveTo(t, 0);
			path.lineTo(t, h);
		}
		for (double t = 0; t < w * 2; t = t + 20) {
			path.moveTo(w, t);
			path.lineTo(0, t);
		}
		g2d.setColor(Color.gray);
		g2d.draw(path);
		
		/*
		 * Drawing curves
		 */
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = 0; i < curves.length; i++){
			if (curves[i] != null){
				// TODO make sure colors aren't similar / hurt
				g2d.setColor(colors[i]);
				g2d.draw(curves[i]);
			}
		}
	}
	
	public static boolean isDouble(String str){
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new Graph(new DataConverter(new File("/Users/Student/Downloads/download.csv")).readCSVFile()));
		f.pack();
		f.setVisible(true);

	}
}