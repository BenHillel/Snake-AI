package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Snake {
	int[] x,y;
	int length;
	int score;
	int life;
	boolean alive;
	int timeAlive;
	Dir dir;
	private Brain brain;
	private Apple apple;
	public static int vesion = 10;
	private static int maxLife = 100;
	
	public Snake(Random r) {
		this.x = new int[400];
		this.y = new int[400];
		this.length=3;
		this.score = 0;
		this.alive = true;
		this.timeAlive = 0;
		this.life= maxLife;
		x[0]=10;
		x[1]=11;
		x[2]=12;
		y[0]=10;
		y[1]=10;
		y[2]=10;
		this.dir = Dir.LEFT;
		this.brain = new Brain(10,10);
		this.apple = new Apple(r);
	}
	public Snake(Brain brain,Random r) {
		this.x = new int[400];
		this.y = new int[400];
		this.length=3;
		this.score = 0;
		this.alive = true;
		this.timeAlive = 0;
		this.life = maxLife;
		x[0]=10;
		x[1]=11;
		x[2]=12;
		y[0]=10;
		y[1]=10;
		y[2]=10;
		this.dir = Dir.LEFT;
		this.brain = new Brain(brain.weights1,brain.weights2,brain.weights3,brain.biase1,brain.biase2,brain.biase3,r);
		this.apple = new Apple(r);
	}

	public void move() {
		for(int i=length;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(dir) {
		case UP:
			y[0]--;
			break;
		case DOWN:
			y[0]++;
			break;
		case LEFT:
			x[0]--;
			break;
		case RIGHT:
			x[0]++;
			break;
		}
	}
	
	public double checkTile(int x,int y) {
		for(int i=1;i<this.length;i++) {
			if((this.x[i]==x && this.y[i]==y) || x>=Game.BOARDSIZE || y >=Game.BOARDSIZE || x<0 || y<0) {
				return 0;
			}
			if(x == this.apple.x && y == this.apple.y) {
				return 1;
			}
		}
		return 0.5;
	}
	
	public double[] getInput() {
		double[] input = new double[vesion*vesion];
		for(int i=0;i<vesion;i++) {
			for(int j=0;j<vesion;j++) {
				input[i*vesion + j] = this.checkTile(this.x[0]-(vesion/2)+i, this.y[0]-(vesion/2)+j);
			}
		}
		return input;
	}
	
	public void tick() {
		if(this.alive) {
			this.timeAlive++;
			this.life--;
			this.brain.setInput(this.getInput());
			this.dir = this.brain.calculate();
			this.move();
			if(this.x[0]==this.apple.x && this.y[0]==this.apple.y) {
				this.apple.relocate();
				this.life = maxLife+this.length*10;
				this.score += 100;
				this.length++;
			}
			if(this.checkTile(this.x[0], this.y[0])==0 || this.life<=0) {
				this.alive = false;
				this.score += Math.min(this.timeAlive,20+10*this.length);
			}
		}
	}
	
	public void render(Graphics g) {
		if(this.alive) {
			g.setColor(new Color((int)(255*(1-(this.life/(double)(maxLife+this.length*10)))),(int)(255*(this.life/(double)(maxLife+this.length*10))),0));
			for(int i=0;i<this.length;i++) {
				g.fillRect(x[i]*Game.SCL, y[i]*Game.SCL, Game.SCL, Game.SCL);
			}
			this.apple.render(g);
		}

	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Brain getBrain() {
		return brain;
	}
	public void setBrain(Brain brain) {
		this.brain = brain;
	}
	
}
