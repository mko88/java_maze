package com.mixo.maze;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RandomMazeGenerator extends Canvas {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setPreferredSize(new Dimension(700, 700));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);							
				frame.add(new RandomMazeGenerator(30));	
			}
		});

	}

	private static final int DRAW_DELAY = 5;
	private static final int SOLVE_DELAY = 5;
	private static final int CELL_SIZE = 13;
	final Color WHITE = Color.WHITE;
	final Color MAGENTA = new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), 100);
	final Color CYAN = new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), 100);
	final Color BLUE = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 100);
	
	private int size;
	private List<Cell> cells;
	private Stack<Cell> cellStack;
	

	private Cell current = null;

	public RandomMazeGenerator(int size) {
		this.cells = new ArrayList<Cell>();
		cellStack = new Stack<Cell>();
		this.size = size;

	}

	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(int row=0;row<size;row++) {
			for(int col=0;col<size;col++) {
				cells.add(new Cell(g, row, col, CELL_SIZE));
			}				
		}
		current = null;		
		generate(g);
		for(int row=0;row<size;row++) {
			for(int col=0;col<size;col++) {
				cells.get(row*size + col).setVisited(false);
			}				
		}
		Cell start = cells.get(0);
		Cell end = cells.get(cells.size()-1);
		start.draw(CYAN);
		end.draw(BLUE);
		cellStack.clear();
		//solveRecursive(g,start,end);
		solveStack(g,start,end);
	}

	private boolean solveStack(Graphics g, Cell current, Cell end) {
		cellStack.push(current);
		while(!cellStack.isEmpty() && !current.equals(end)) {
			boolean visited = false;						
			//current.drawSmall(new Color(100, 100, 155));
			current.setVisited(true);
			Cell[] neighbours = getNeighbours(current, true);
			for(Cell neighbour:neighbours) {
				if(neighbour.equals(end) && canVisit(current, neighbour)) {
					//neighbour.drawSmall(new Color(100, 100, 155, 180));
					current.drawArrow(getDirection(current, neighbour));
					current = neighbour;
					return true;
				}
			}
			for(Cell neighbour:neighbours) {
				if(canVisit(current, neighbour)) {
					visited = true;
					//neighbour.drawSmall(new Color(100, 100, 155, 180));
					current.draw(Color.WHITE);
					current.drawArrow(getDirection(current, neighbour));
					//current.drawArrow(Cell.TOP);
					neighbour.setVisited(true);
					cellStack.push(current);
					current = neighbour;
					sleep(SOLVE_DELAY);	
					break;
				}
			}
			if(!visited) {
				current.draw(MAGENTA);
				current = cellStack.pop();						
				sleep(SOLVE_DELAY);
				//current.drawSmall(MAGENTA);
				
			}
		}
		return false;
	}
	
	private int getDirection(Cell from, Cell to) {
		//TODO doesn't look natural, when drawing the maze is translated...
		int idxA = cells.indexOf(from);
		int idxB = cells.indexOf(to);
		int delta = idxA - idxB;
		if(delta == 1) {
			return Cell.TOP;			
		}
		if(delta == -1) {	
			return Cell.BOTTOM;			
		}
		if(delta == size) {			
			return Cell.LEFT;
		}
		if(delta == -size) {
			return Cell.RIGHT;			
		}
		return -1;
	}
		
	@SuppressWarnings("unused")
	private boolean solveRecursive(Graphics g, Cell start, Cell end) {
		if(start.equals(end)) {
			return true;
		}
		start.drawSmall(new Color(100, 100, 155, 100));
		Cell[] neighbours = getNeighbours(start, true);
		for(Cell neighbour:neighbours) {
			if(canVisit(start, neighbour)) {
				neighbour.drawSmall(new Color(100, 100, 155, 180));
				neighbour.setVisited(true);
				sleep(SOLVE_DELAY);
				if(solveRecursive(g, neighbour, end)) {
					return true;
				}else {
					neighbour.draw(Color.WHITE);
				}
			}
		}
		return false;
	}

	public void generate(Graphics g) {
		current = cells.get(0);		
		Cell[] neighbours;
		neighbours = getNeighbours(current, true);
		while(neighbours.length != 0 || !cellStack.isEmpty()) {
			System.out.println("Visit " + current);
			current.setVisited(true);						
			sleep(DRAW_DELAY);			
			if(neighbours.length>0) {
				current.draw(MAGENTA);
				cellStack.push(current);				
				int nextIndex = ThreadLocalRandom.current().nextInt(neighbours.length);			
				Cell next = neighbours[nextIndex];
				removeWalls(current, next);				
				next.draw(MAGENTA);
				current = next;			
				neighbours = getNeighbours(current, true);
			} else {
				current.draw(WHITE);
				current = cellStack.pop();
				current.draw(CYAN);
				neighbours = getNeighbours(current, true);
			}
		}
		current.draw(WHITE);
	}

	private void sleep(long millis) {
		if(millis==0) {
			return;
		}
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	private boolean canVisit(Cell a, Cell b) {
		int idxA = cells.indexOf(a);
		int idxB = cells.indexOf(b);
		int delta = idxA - idxB;
		if(delta == 1) {
			return !a.getWall(Cell.LEFT);
		}
		if(delta == -1) {
			return !a.getWall(Cell.RIGHT);
		}
		if(delta == size) {
			return !a.getWall(Cell.TOP);
		}
		if(delta == -size) {
			return !a.getWall(Cell.BOTTOM);
		}
		return false;
	}
	
	private void removeWalls(Cell a, Cell b) {
		int idxA = cells.indexOf(a);
		int idxB = cells.indexOf(b);
		int delta = idxA - idxB;
		if(delta == 1) {
			a.removeWall(Cell.LEFT);
			b.removeWall(Cell.RIGHT);
		}
		if(delta == -1) {
			a.removeWall(Cell.RIGHT);
			b.removeWall(Cell.LEFT);
		}
		if(delta == size) {
			a.removeWall(Cell.TOP);
			b.removeWall(Cell.BOTTOM);
		}
		if(delta == -size) {
			a.removeWall(Cell.BOTTOM);
			b.removeWall(Cell.TOP);
		}
	}

	public Cell[] getNeighbours(Cell cell, boolean ignoreVisited) {
		int indexOfCurrent = cells.indexOf(cell);
		int indexOfLeft = indexOfCurrent - 1;
		int indexOfRight = indexOfCurrent + 1;
		int indexOfAbove = indexOfCurrent - size;
		int indexOfBelow = indexOfCurrent + size;
		List<Cell> neighbours = new ArrayList<Cell>();
		//right
		if(cell.getY()<size-1) {			
			addNeighbour(neighbours, cells.get(indexOfRight), ignoreVisited);
		}
		//left
		if(cell.getY()>0) {
			addNeighbour(neighbours, cells.get(indexOfLeft), ignoreVisited);
		}
		//above
		if(cell.getX()>0) {
			addNeighbour(neighbours, cells.get(indexOfAbove), ignoreVisited);
		}
		//below
		if(cell.getX()<size-1) {
			addNeighbour(neighbours, cells.get(indexOfBelow), ignoreVisited);
		}

		return neighbours.toArray(new Cell[neighbours.size()]);
	}

	private void addNeighbour(List<Cell> array, Cell cell, boolean ignoreVisited) {		
		if(!cell.isVisited() || !ignoreVisited) {
			array.add(cell);
		}
	}	
}




