package com.sonification.sheet;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sheet extends JPanel{
	
	public Sheet(int rows, int columns){
		this.setLayout(new GridLayout(rows, columns, -7, -7));
		
		for (int i = 0; i < rows * columns; i++){
			JTextField jtf = new JTextField();
			this.add(jtf);
		}
	}
	
	public static void main (String[] args){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new Sheet(10, 10));
		f.pack();
		f.setVisible(true);
	}
}
