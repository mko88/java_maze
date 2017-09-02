package com.mixo.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class RandomMazeGeneratorNew extends JComponent {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();														
				frame.setPreferredSize(PREF_DIMENSION);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocation(10, 10);
				
				JPanel panel = new JPanel();
				panel.setPreferredSize(PREF_DIMENSION);
				panel.add(new RandomMazeGeneratorNew(MAZE_SIZE));	
				frame.add(panel);	
				frame.pack();
				frame.setVisible(true);				
				
			}
		});
	}	

	private static final int DRAW_DELAY = 0;
	private static final int SOLVE_DELAY = 5;
	private static final int CELL_SIZE = 2;
	private static final int MAZE_SIZE = 400;
	private static Dimension PREF_DIMENSION = new Dimension(CELL_SIZE*MAZE_SIZE+50, CELL_SIZE*MAZE_SIZE+50);
	private static Dimension PREF_DIMENSION_MAZE = new Dimension(CELL_SIZE*MAZE_SIZE + 1, CELL_SIZE*MAZE_SIZE + 1);
	final Color WHITE = Color.WHITE;
	final Color MAGENTA = new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), 100);
	final Color CYAN = new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), 100);
	final Color BLUE = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 100);
	
	private int size;
	private List<Cell> cells;
	private CellStack cellStack;
	private Stack<Cell> repaintStack;
	

	private Cell current = null;
	private Cell[] neighbours = null;
	private Timer timer;
	boolean initPaint = true;
	RepaintManager rm;
			
	public RandomMazeGeneratorNew(int size) {
		rm = RepaintManager.currentManager(this);
		rm.markCompletelyClean(this);
		setPreferredSize(PREF_DIMENSION_MAZE);
		
		this.cells = new ArrayList<Cell>();
		cellStack = new CellStack();
		repaintStack = new Stack<Cell>();
		this.size = size;		
		init();
		repaint();
		current = cells.get(0);
		neighbours = getNeighbours(current);
		startMazeGeneration();
	}
	
	private void init() {
		for(int row=0;row<size;row++) {
			for(int col=0;col<size;col++) {
				Cell cell = new Cell(row, col, CELL_SIZE);
				cells.add(cell);
				repaintStack.push(cell);
			}				
		}
	}
	
	public void startMazeGeneration() {		
		timer = new Timer(DRAW_DELAY, new GenerateTask());
		timer.start();
	}
	
	private class GenerateTask implements ActionListener {		
		
		public void actionPerformed(ActionEvent e) {			
			if(neighbours.length>0 || !cellStack.isEmpty()) {
				current.setVisited(true);								
				if(neighbours.length>0) {
					cellStack.push(current);	
					repaintStack.push(current);
					repaint(current.getX() * CELL_SIZE, current.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					int nextIndex = ThreadLocalRandom.current().nextInt(neighbours.length);			
					Cell next = neighbours[nextIndex];
					removeWalls(current, next);				
					current = next;			
					neighbours = getNeighbours(current);
				} else {
					repaintStack.push(current);
					repaint(current.getX() * CELL_SIZE, current.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					current = cellStack.pop();
					repaintStack.push(current);
					repaint(current.getX() * CELL_SIZE, current.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);	
					neighbours = getNeighbours(current);			
				}				
			}else {
				timer.stop();
			}
		}
		
	}	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		while(!repaintStack.isEmpty()) {
			repaintStack.pop().draw(g);
		}		
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		//super.paintComponent(g);
	}

	private boolean solveStack(Graphics g, Cell current, Cell end) {
		cellStack.push(current);
		while(!cellStack.isEmpty() && !current.equals(end)) {
			boolean visited = false;						
			//current.drawSmall(new Color(100, 100, 155));
			current.setVisited(true);
			Cell[] neighbours = getNeighbours(current);
			for(Cell neighbour:neighbours) {
				if(neighbour.equals(end) && canVisit(current, neighbour)) {
					//neighbour.drawSmall(new Color(100, 100, 155, 180));
					current = neighbour;
					return true;
				}
			}
			for(Cell neighbour:neighbours) {
				if(canVisit(current, neighbour)) {
					visited = true;
					//neighbour.drawSmall(new Color(100, 100, 155, 180));
					//current.drawArrow(Cell.TOP);
					neighbour.setVisited(true);
					cellStack.push(current);
					current = neighbour;
					sleep(SOLVE_DELAY);	
					break;
				}
			}
			if(!visited) {
				current = cellStack.pop();						
				sleep(SOLVE_DELAY);
				//current.drawSmall(MAGENTA);
				
			}
		}
		return false;
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

	public Cell[] getNeighbours(Cell cell) {
		int indexOfCurrent = cells.indexOf(cell);
		int indexOfLeft = indexOfCurrent - 1;
		int indexOfRight = indexOfCurrent + 1;
		int indexOfAbove = indexOfCurrent - size;
		int indexOfBelow = indexOfCurrent + size;
		List<Cell> neighbours = new ArrayList<Cell>();
		//right
		if(cell.getY()<size-1) {			
			addNeighbour(neighbours, cells.get(indexOfRight));
		}
		//left
		if(cell.getY()>0) {
			addNeighbour(neighbours, cells.get(indexOfLeft));
		}
		//above
		if(cell.getX()>0) {
			addNeighbour(neighbours, cells.get(indexOfAbove));
		}
		//below
		if(cell.getX()<size-1) {
			addNeighbour(neighbours, cells.get(indexOfBelow));
		}

		return neighbours.toArray(new Cell[neighbours.size()]);
	}

	private void addNeighbour(List<Cell> array, Cell cell) {		
		if(!cell.isVisited()) {
			array.add(cell);
		}
	}	
}