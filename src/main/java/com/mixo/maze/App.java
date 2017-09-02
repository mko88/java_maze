package com.mixo.maze;

import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				MazeView mv = new MazeView();
				mv.setVisible(true);
			}
		});
    }
}
