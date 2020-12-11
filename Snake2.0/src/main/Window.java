package main;

import java.awt.Dimension;

import javax.swing.JFrame;



public class Window{
	JFrame frame;
	public Window(Game game) {
		frame = new JFrame("HEAVEN");
		frame.setPreferredSize(new Dimension(Game.WIDTH,Game.HEIGHT));
		frame.setMaximumSize((new Dimension(Game.WIDTH,Game.HEIGHT)));
		frame.setMinimumSize((new Dimension(Game.WIDTH,Game.HEIGHT)));
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.setVisible(true);
		game.Start();
	}
}
