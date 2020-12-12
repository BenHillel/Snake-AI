package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int WIDTH = 700,HEIGHT=700,BOARDSIZE=20,SCL = WIDTH/BOARDSIZE;
	public static double AMOUNT_OF_TICKS=10;
	public static int[][] applesPos = new int[BOARDSIZE*BOARDSIZE][2];
	
	
	//Staff you can mess with ------------------------------
	//A bit of instructions:
	//if you change the lairs size, you need to do one game of new train to update the brain memory to the current size
	public static int MUTATION_SIZE=2;
	public static double MUTATION_RATE = 0.1;
	public static int FIRST_LAIR_LEN = 8,SECOND_LAIR_LEN = 5;
	public boolean newTrain = true;
	int num_of_victims = 2000;
	//------------------------------------------------------
	
	Brain bestBrain;
	public int generation;
	public int highScore = 0;
	boolean running = false;
	
	private Font normalFont = new Font("Ariel",1,30);
	private Font bigFont = new Font("David",10,80);
	
	Window window;
	Thread thread;
	Snake[] snakes;
	Random r;
	
	public Game() {
		this.bestBrain = new Brain(FIRST_LAIR_LEN,SECOND_LAIR_LEN,false);
		this.generation = 0;
		this.addKeyListener(new KeyInput(this));
		r = new Random();
		for(int i =0;i<BOARDSIZE*BOARDSIZE;i++) {
			applesPos[i][0] = r.nextInt(BOARDSIZE-1);
			applesPos[i][1] = r.nextInt(BOARDSIZE-1);
		}
		snakes = new Snake[num_of_victims];
		if(newTrain) {
			for(int i=0;i<num_of_victims;i++) {
				snakes[i] = new Snake();
			}
		}else {
			for(int i=0;i<num_of_victims;i++) {
				snakes[i] = new Snake(bestBrain);
			}
		}


		window = new Window(this);
		
	}
	
	public synchronized void Start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	
	public void run() {
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            System.out.println("In shutdown hook");
	            updateData(bestBrain);
	        }
	    }, "Shutdown-thread"));
		this.requestFocus();
		long lastTime = System.nanoTime();
		double ns = 1000000000 / AMOUNT_OF_TICKS;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames =0;
		while(running) {
			ns = 1000000000 / AMOUNT_OF_TICKS;
			long now = System.nanoTime();
			delta =min(AMOUNT_OF_TICKS*10,delta+(now-lastTime)/ns);
			lastTime = now;
			while(delta>=1) {
				tick();
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}
	public boolean everyoneIsDead(Snake[] sl) {
		for(int i=0;i<sl.length;i++) {
			if(sl[i].alive==true) {
				return false;
			}
		}
		return true;
	}
	
	public Snake bestSnake(Snake[] sl) {
		Snake snake = sl[0];
		int maxScore = 0;
		for(int i=0;i<sl.length;i++) {
			if(sl[i].score >= maxScore) {
				snake = sl[i];
				maxScore = sl[i].score;
			}
		}
		System.out.println(snake.score);
		highScore = maxScore;
		bestBrain = snake.getBrain();
		//updateData(snake.getBrain());
		return snake;
	}
	
	public void evolution() {
		Snake sorviver = bestSnake(this.snakes);
		this.snakes[0] = new Snake();
		this.snakes[0].setBrain(new Brain(sorviver.getBrain().weights1,sorviver.getBrain().weights2,sorviver.getBrain().weights3,sorviver.getBrain().biase1,sorviver.getBrain().biase2,sorviver.getBrain().biase3,false));
		for(int i=1;i<num_of_victims;i++) {
			this.snakes[i] = new Snake(sorviver.getBrain());
		}
	}
	
	public void train() {
		for(int i=0;i<num_of_victims;i++) {
			this.snakes[i].tick();
		}
		if(everyoneIsDead(snakes)) {
			this.generation++;
			evolution();
		}
	}
	
	public void tick() {
		train();
	}
	
	public void render() {
		if(!KeyInput.keyPressed[1]) {
			BufferStrategy bs = this.getBufferStrategy();
			if(bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			Graphics g = bs.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			if(!KeyInput.keyPressed[0]) {
				snakes[0].render(g);	
				if(!snakes[0].alive) {
					g.setFont(bigFont);
					g.setColor(Color.white);
					g.drawString("Waiting for all", 100, 300);
					g.drawString("victims to die...", 100, 400);
				}
			}else {
				for(int i=0;i<num_of_victims;i++) {
					snakes[i].render(g);
				}	
			}
			
			g.setFont(normalFont);
			g.setColor(Color.white);
			g.drawString("Generation:"+this.generation, 20, 30);
			g.drawString("High Score = "+highScore, 400, 30);
			
			g.dispose();
			bs.show();
		}
	}
	
	public void updateData(Brain brain) {
		double[][] weights1 = brain.weights1;
		double[][] weights2 = brain.weights2;
		double[][] weights3 = brain.weights3;
		double[] biase1 = brain.biase1;
		double[] biase2 = brain.biase2;
		double[] biase3 = brain.biase3;
		try {
			File Weights1 = new File("weights1.txt");
			if(Weights1.createNewFile()) {
				System.out.println("weights1 created");
			}else {
				System.out.println("weights1 already exist");
			}
			FileWriter weights1W = new FileWriter("weights1.txt");
			for(int i=0;i<weights1.length;i++) {
				for(int j=0;j<weights1[i].length;j++) {
					if(!(i==j && i ==0)) {
						weights1W.write("\n");
					}
					String val = String.valueOf(weights1[i][j]);
					weights1W.write(val);
				} 
			}
			weights1W.close();
			//-------------------------------------------------------
			File Weights2 = new File("weights2.txt");
			if(Weights2.createNewFile()) {
				System.out.println("weights2 created");
			}else {
				System.out.println("weights2 already exist");
			}
			FileWriter weights2W = new FileWriter("weights2.txt");
			for(int i=0;i<weights2.length;i++) {
				for(int j=0;j<weights2[i].length;j++) {
					if(!(i==j && i ==0)) {
						weights2W.write("\n");
					}
					String val = String.valueOf(weights2[i][j]);
					weights2W.write(val);
				} 
			}
			weights2W.close();
			//-------------------------------------------------------
			File Weights3 = new File("weights3.txt");
			if(Weights3.createNewFile()) {
				System.out.println("weights3 created");
			}else {
				System.out.println("weights3 already exist");
			}
			FileWriter weights3W = new FileWriter("weights3.txt");
			for(int i=0;i<weights3.length;i++) {
				for(int j=0;j<weights3[i].length;j++) {
					if(!(i==j && i ==0)) {
						weights3W.write("\n");
					}
					String val = String.valueOf(weights3[i][j]);
					weights3W.write(val);
				} 
			}
			weights3W.close();
			//-------------------------------------------------------
			File Biase1 = new File("biase1.txt");
			if(Biase1.createNewFile()) {
				System.out.println("biase1 created");
			}else {
				System.out.println("biase1 already exist");
			}
			FileWriter biase1W = new FileWriter("biase1.txt");
			for(int i=0;i<biase1.length;i++) {
				if(i != 0) {
					biase1W.write("\n");
				}
				String val = String.valueOf(biase1[i]);
				biase1W.write(val);
			}
			biase1W.close();
			//-------------------------------------------------------
			File Biase2 = new File("biase2.txt");
			if(Biase2.createNewFile()) {
				System.out.println("biase2 created");
			}else {
				System.out.println("biase2 already exist");
			}
			FileWriter biase2W = new FileWriter("biase2.txt");
			for(int i=0;i<biase2.length;i++) {
				if(i != 0) {
					biase2W.write("\n");
				}
				String val = String.valueOf(biase2[i]);
				biase2W.write(val);
			}
			biase2W.close();
			//-------------------------------------------------------
			File Biase3 = new File("biase3.txt");
			if(Biase3.createNewFile()) {
				System.out.println("biase3 created");
			}else {
				System.out.println("biase3 already exist");
			}
			FileWriter biase3W = new FileWriter("biase3.txt");
			for(int i=0;i<biase3.length;i++) {
				if(i != 0) {
					biase3W.write("\n");
				}
				String val = String.valueOf(biase3[i]);
				biase3W.write(val);
			}
			biase3W.close();
			System.out.println("==========UPDATED_DATA==========");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public double min(double n1,double n2) {
		if(n1<n2) return n1;
		return n2;
	}
	

	
	public static void main(String[] args) {
		new Game();

	}



}
