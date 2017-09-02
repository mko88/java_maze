package com.mixo.maze;

import java.util.ArrayList;
import java.util.List;

public class MazeGen {
	private int maze[][];
	private List<Cell> grid;
	private final int SIZE;
	public MazeGen(int size) {
		SIZE = size;
		maze = new int[size][size];
		grid = new ArrayList<MazeGen.Cell>();
		for(int i=0; i<size; i++) {
			for(int j=0;j<size;j++) {
				Cell cell = new Cell(i,j);
				grid.add(cell);
			}
		}
	}
	
	public class Cell {
		int i;
		int j;
		
		public Cell(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}
}
