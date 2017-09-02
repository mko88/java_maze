package com.mixo.maze;

import java.awt.Color;
import java.awt.Graphics;

public class Cell {		
	public static final int BOTTOM=0, TOP=1, LEFT=2, RIGHT=3;
	private boolean[] walls;
	private int x;
	private int y;		
	private int size;
	private boolean visited;
	private Graphics g;

	public Cell(Graphics g, int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		visited = false;
		walls = new boolean[] {true, true, true, true};
		this.g = g;
		draw(Color.WHITE);

	}

	public void draw(Color color) {
		g.setColor(color);			
		g.fillRect(x*size, y*size, size + 1, size + 1);
		g.setColor(Color.BLACK);	
		if(walls[TOP]) {
			//0, 0, 0, 20
			g.drawLine(x*size, y*size, x*size, y*size + size);
		}
		if(walls[LEFT]) {
			//0, 0, 20, 0
			g.drawLine(x*size, y*size, x*size+size, y*size);
		}
		if(walls[RIGHT]) {
			//0, 20, 20, 20
			g.drawLine(x*size, y*size + size, x*size + size, y*size + size);
		}
		if(walls[BOTTOM]) {
			//20, 0, 20, 20
			g.drawLine(x*size + size, y*size, x*size + size, y*size + size);
		}
	}
	
	public void drawSmall(Color color) {
		g.setColor(color);
		if(size>=9) {
			g.fillRect(x*size + 4, y*size + 4, size + 1 - 7, size + 1 - 7);
		}else {
			g.fillRect(x*size, y*size, size + 1, size + 1);
		}
		g.setColor(Color.BLACK);	
		if(walls[TOP]) {
			//0, 0, 0, 20
			g.drawLine(x*size, y*size, x*size, y*size + size);
		}
		if(walls[LEFT]) {
			//0, 0, 20, 0
			g.drawLine(x*size, y*size, x*size+size, y*size);
		}
		if(walls[RIGHT]) {
			//0, 20, 20, 20
			g.drawLine(x*size, y*size + size, x*size + size, y*size + size);
		}
		if(walls[BOTTOM]) {
			//20, 0, 20, 20
			g.drawLine(x*size + size, y*size, x*size + size, y*size + size);
		}
	}
	
	public boolean getWall(int which) {
		return walls[which];
	}
	
	public void removeWall(int which) {
		walls[which] = false;
	}	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}	

	@Override
	public String toString() {
		return "[x=" + x 
				+" y=" + y
				+"]";
	}
	
	private static final int ARROW_PAD = 3;
	
	public void drawArrow(int direction) {
		switch (direction) {
		case Cell.BOTTOM:
			drawArrowLine(g, x*size + size/2, y*size + ARROW_PAD, x*size + size/2, y*size + size - ARROW_PAD, 3, 3);
			break;
		case Cell.TOP:
			drawArrowLine(g, x*size + size/2, y*size + size - ARROW_PAD, x*size + size/2, y*size + ARROW_PAD, 3, 3);
			break;
		case Cell.LEFT:			
			drawArrowLine(g, x*size + size - ARROW_PAD, y*size + size/2, x*size + ARROW_PAD, y*size + size/2, 3, 3);
			break;
		case Cell.RIGHT:
			drawArrowLine(g, x*size + ARROW_PAD, y*size + size/2, x*size + size - ARROW_PAD, y*size + size/2, 3, 3);
			break;
		default:
			break;
		}
	}
	
	/**
     * Draw an arrow line betwwen two point 
     * @param g the graphic component
     * @param x1 x-position of first point
     * @param y1 y-position of first point
     * @param x2 x-position of second point
     * @param y2 y-position of second point
     * @param d  the width of the arrow
     * @param h  the height of the arrow
     */
    public void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h){
       int dx = x2 - x1, dy = y2 - y1;
       double D = Math.sqrt(dx*dx + dy*dy);
       double xm = D - d, xn = xm, ym = h, yn = -h, x;
       double sin = dy/D, cos = dx/D;

       x = xm*cos - ym*sin + x1;
       ym = xm*sin + ym*cos + y1;
       xm = x;

       x = xn*cos - yn*sin + x1;
       yn = xn*sin + yn*cos + y1;
       xn = x;

       int[] xpoints = {x2, (int) xm, (int) xn};
       int[] ypoints = {y2, (int) ym, (int) yn};

       g.drawLine(x1, y1, x2, y2);
       g.fillPolygon(xpoints, ypoints, 3);
    }

}
