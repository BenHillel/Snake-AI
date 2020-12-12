package main;

import java.awt.Color;
import java.awt.Graphics;


public class Apple {
	int x,y,posIndex;
	public Apple() {
		this.posIndex = 0;
		this.relocate();
	}
	
	public void relocate() {
		//this.x = r.nextInt(Game.BOARDSIZE);
		//this.y = r.nextInt(Game.BOARDSIZE);
		
		this.x = Game.applesPos[this.posIndex][0];
		this.y = Game.applesPos[this.posIndex][1];
		this.posIndex++;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x*Game.SCL, y*Game.SCL, Game.SCL, Game.SCL);
	}
}
