package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	Game game;
	public static boolean[] keyPressed;
	public KeyInput(Game game) {
		this.game = game;
		keyPressed = new boolean[2];
		for(int i=0;i<keyPressed.length;i++) {
			keyPressed[i] = false;
		}
		
	}
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_0) keyPressed[0] = !keyPressed[0];
		if(key == KeyEvent.VK_1) Game.AMOUNT_OF_TICKS = 10.0;
		if(key == KeyEvent.VK_2) Game.AMOUNT_OF_TICKS = 20.0;
		if(key == KeyEvent.VK_3) Game.AMOUNT_OF_TICKS = 50.0;
		if(key == KeyEvent.VK_4) Game.AMOUNT_OF_TICKS = 100.0;
		if(key == KeyEvent.VK_5) Game.AMOUNT_OF_TICKS = 500.0;
		if(key == KeyEvent.VK_6) Game.AMOUNT_OF_TICKS = 1000.0;
		if(key == KeyEvent.VK_9) keyPressed[1] = !keyPressed[1];
	}
}
