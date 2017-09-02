package com.mixo.maze;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class MazeView extends JFrame {

	private final int BOX_SIZE = 30;

	private int[][] maze = { 
			{1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,1,0,1,0,1,0,0,0,0,0,1},
			{1,0,1,0,0,0,1,0,1,1,1,0,1},
			{1,0,0,0,1,1,1,0,0,0,0,0,1},
			{1,0,1,0,0,0,0,0,1,1,1,0,1},
			{1,0,1,0,1,1,1,0,1,0,0,0,1},
			{1,0,1,0,1,0,0,0,1,1,1,0,1},
			{1,0,1,0,1,1,1,0,1,0,1,0,1},
			{1,0,0,0,0,0,0,0,0,0,1,9,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1}

	};

	public MazeView() {
		setTitle("Maze view");
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	@Override
	public void paint(Graphics g) {	
		super.paint(g);
		g.translate(50, 50);
		for(int row=0; row<maze.length; row++) {
			for (int col=0; col<maze[0].length; col++) {
				Color color;
				switch (maze[row][col]) {
				case 1: color = Color.BLACK; 
				break;
				case 9: color = Color.MAGENTA; 
				break;
				default: color = Color.GRAY;
				}
				g.setColor(color);
				g.fillRect(BOX_SIZE*col, BOX_SIZE*row, BOX_SIZE, BOX_SIZE);
				g.setColor(Color.BLACK);
				g.drawRect(BOX_SIZE*col, BOX_SIZE*row, BOX_SIZE, BOX_SIZE);
			}
		}
	}
}
