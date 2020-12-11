package main;

import java.awt.Dimension;

import javax.swing.JFrame;



public class Window{
	JFrame frame;
	public Window(Game game) {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(Game.WIDTH,Game.HEIGHT));
		frame.setMaximumSize((new Dimension(Game.WIDTH,Game.HEIGHT)));
		frame.setMinimumSize((new Dimension(Game.WIDTH,Game.HEIGHT)));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.setVisible(true);
		game.Start();
	}
}
