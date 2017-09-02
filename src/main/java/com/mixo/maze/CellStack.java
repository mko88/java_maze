package com.mixo.maze;

import java.util.Stack;

public class CellStack extends Stack<Cell> {
	private static final long serialVersionUID = 1L;

	@Override
	public Cell push(Cell item) {
		item.setInStack(true);
		return super.push(item);			
	}
	
	@Override
	public synchronized Cell pop() {
		Cell item = super.pop();
		item.setInStack(false);
		return item;
	}
}