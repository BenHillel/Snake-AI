package main;

import java.util.Random;

public class Brain {
	private double[] input;
	double[] lair1,lair2,output;
	double[][] weights1,weights2,weights3;
	double[] biase1,biase2,biase3;
	int inputSize = Snake.vesion*Snake.vesion;
	
	public Brain(int len1,int len2) {
		this.input = new double[inputSize];
		this.lair1 = new double[len1];
		this.lair2 = new double[len2];
		this.output = new double[4];
		this.weights1 = new double[len1][inputSize];
		this.biase1 = new double[len1];
		this.weights2 = new double[len2][len1];
		this.biase2 = new double[len2];
		this.weights3 = new double[4][len2];
		this.biase3 = new double[4];
		this.randomSetup();
	}
	
	public Brain(double[][] weights1,double[][] weights2,double[][] weights3,double[] biase1,double[] biase2,double[] biase3,Random r) {
		this.weights1 = this.clone(weights1);
		this.weights2 = this.clone(weights2);
		this.weights3 = this.clone(weights3);
		this.biase1 = this.clone(biase1);
		this.biase2 = this.clone(biase2);
		this.biase3 = this.clone(biase3);
		this.lair1 = new double[biase1.length];
		this.lair2 = new double[biase2.length];
		this.output = new double[4];
		this.input = new double[inputSize];
		this.mutate(r);
	}
	public Brain(double[][] weights1,double[][] weights2,double[][] weights3,double[] biase1,double[] biase2,double[] biase3) {
		this.weights1 = this.clone(weights1);
		this.weights2 = this.clone(weights2);
		this.weights3 = this.clone(weights3);
		this.biase1 = this.clone(biase1);
		this.biase2 = this.clone(biase2);
		this.biase3 = this.clone(biase3);
		this.lair1 = new double[biase1.length];
		this.lair2 = new double[biase2.length];
		this.output = new double[4];
		this.input = new double[inputSize];
	}
	
	private void randomSetup() {
		int len1 = this.lair1.length;
		int len2 = this.lair2.length;
		//biases and weights setup -----------------------
		for(int i=0;i<len1;i++) {
			this.biase1[i] = (Math.random()*10)-5;
			for(int j=0;j<inputSize;j++) {
				weights1[i][j] = (Math.random()*4)-2;
			}
		}
		for(int i=0;i<len2;i++) {
			this.biase2[i] = (Math.random()*10)-5;
			for(int j=0;j<len1;j++) {
				weights2[i][j] = (Math.random()*4)-2;
			}
		}
		for(int i=0;i<4;i++) {
			this.biase3[i] = (Math.random()*10)-5;
			for(int j=0;j<len2;j++) {
				weights3[i][j] = (Math.random()*4)-2;
			}
		}
		
	}
	private double random(Random r) {
		return Math.random()*Game.MUTATION_SIZE*2-Game.MUTATION_SIZE;
	}
	
	public void mutate(Random r) {
		int len1 = this.lair1.length;
		int len2 = this.lair2.length;
		for(int i=0;i<len1;i++) {
			if(Math.random()<=Game.MUTATION_RATE) {
				this.biase1[i] += this.random(r);
			}
			for(int j=0;j<inputSize;j++) {
				if(Math.random()<=Game.MUTATION_RATE) {
					this.weights1[i][j] += this.random(r);				
				}
			}
		}
		for(int i=0;i<len2;i++) {
			if(Math.random()<=Game.MUTATION_RATE) {
				this.biase2[i] += this.random(r);
			}
			for(int j=0;j<len1;j++) {
				if(Math.random()<=Game.MUTATION_RATE) {
					this.weights2[i][j] += this.random(r);
				}
			}
		}
		for(int i=0;i<4;i++) {
			if(Math.random()<=Game.MUTATION_RATE) {
				this.biase3[i] += this.random(r);
			}
			for(int j=0;j<len2;j++) {
				if(Math.random()<=Game.MUTATION_RATE) {
					this.weights3[i][j] += this.random(r);
				}
			}
		}
	}
	
	public double sigmoid(double num) {
		return 1/(1+Math.pow(Math.E,-num));
	}
	public double[] sigmoid(double[] vec) {
		for(int i=0;i<vec.length;i++) {
			vec[i] = sigmoid(vec[i]);
		}
		return vec;
	}
	public double[] multiply(double[][] mat,double[] vec) {
		double[] result = new double[mat.length];
		for(int i=0;i<mat.length;i++) {
			for(int j=0;j<mat[i].length;j++) {
				result[i] += mat[i][j]*vec[j];
			}
		}
		return result;
	}
	public double[] add(double[] vec1,double[] vec2) {
		double[] result = new double[vec1.length];
		for(int i=0;i<vec1.length;i++) {
			result[i] = vec1[i]+vec2[i];
		}
		return result;
	}
	public int maxIndex(double[] vec) {
		double max = 0;
		int index = 0;
		for(int i=0;i<vec.length;i++) {
			if(vec[i]>max) {
				max = vec[i];
				index = i;
			}
		}
		return index;
	}
	
	public Dir calculate() {
		this.lair1 = sigmoid(add(multiply(this.weights1,this.input),this.biase1));
		this.lair2 = sigmoid(add(multiply(this.weights2,this.lair1),this.biase2));
		this.output = sigmoid(add(multiply(this.weights3,this.lair2),this.biase3));
		switch(maxIndex(output)) {
		case 0:
			return Dir.UP;
		case 1:
			return Dir.DOWN;
		case 2:
			return Dir.RIGHT;
		default:
			return Dir.LEFT;
		}
	}
	
	private double[] clone(double[] mat) {
		double[] clone = new double[mat.length];
		for(int i=0;i<clone.length;i++) {
			clone[i] = mat[i];
		}
		return clone;
	}
	private double[][] clone(double[][] mat) {
		double[][] clone = new double[mat.length][mat[0].length];
		for(int i=0;i<clone.length;i++) {
			for(int j=0;j<clone[i].length;j++) {
				clone[i][j] = mat[i][j];
			}
		}
		return clone;
	}

	public double[] getInput() {
		return input;
	}

	public void setInput(double[] input) {
		this.input = input;
	}
	
}
