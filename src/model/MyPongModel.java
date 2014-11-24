package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;



public class MyPongModel implements PongModel {
	private class MyPoint extends Point{
		double x;
		double y;
	}
	
	private Map<BarKey, Integer> barPos = new HashMap<BarKey, Integer>();
	private Map<BarKey, Integer> barHeight = new HashMap<BarKey, Integer>();
	private String leftPlayer;
	private String rightPlayer;
	private String message = "hej!!!";
	private Map<BarKey, Integer> score = new HashMap<BarKey, Integer>();
	private Dimension fieldSize;
	private Point ballPos = new Point();
	private int ballSpeed;
	private double ballDir;
	
	public MyPongModel(String leftPlayer, String rightPlayer){
		fieldSize = new Dimension();
		fieldSize.setSize(1500, 1000);
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		barPos.put(BarKey.LEFT, 100);
		barPos.put(BarKey.RIGHT, 100);
		barHeight.put(BarKey.LEFT, 200);
		barHeight.put(BarKey.RIGHT, 200);
		score.put(BarKey.LEFT, 0);
		score.put(BarKey.RIGHT, 0);
		ballPos.setLocation(750, 500);
		resetBall();
		
		
	}
	
	public void compute(Set<Input> input, long delta_t){
		Iterator<Input> iterator = input.iterator();
		while(iterator.hasNext()){
			moveBar(iterator.next(), delta_t);
		}
		message = ("X=: " + ballPos.x + ", Y=: " + ballPos.y);
		moveBall(delta_t);
		
	}
	
	private void moveBar(Input input, long delta_t){
		int pos = getBarPos(input.key);
		if(input.dir == Input.Dir.UP && pos > 100){
			pos -= 0.55 * delta_t;
		}
		if(input.dir == Input.Dir.DOWN && pos < 900){
			pos += 0.75 * delta_t;
		}
		setBarPos(input.key, pos);
	}
	
	private void moveBall(long delta_t){
		ballPos.x += ballSpeed * Math.cos(ballDir) * 0.03 * delta_t;
		ballPos.y -= ballSpeed * Math.sin(ballDir) * 0.03 * delta_t;
		
		//Bollen träffar högerkanten
		if(ballPos.x >= 1500){
			if(Math.abs(ballPos.y - getBarPos(BarKey.RIGHT)) > 100){
				score.put(BarKey.LEFT, score.get(BarKey.LEFT) + 1);
				resetBall();
			}
			else{
				ballSpeed++;
				if(Math.abs(ballPos.y - getBarPos(BarKey.RIGHT)) <= 70){
					ballDir = Math.PI - ballDir;
				}else{
						ballDir += Math.PI;
				}
				
			}
		}
		
		//Bollen träffar vänsterkanten
		if(ballPos.x <= 0){ 
			if(Math.abs(ballPos.y - getBarPos(BarKey.LEFT)) > 100){
				score.put(BarKey.RIGHT, score.get(BarKey.RIGHT) + 1);
				resetBall();
			}
			else{
				ballSpeed++;
				if(Math.abs(ballPos.y - getBarPos(BarKey.LEFT)) <= 70){
					ballDir = Math.PI - ballDir;
				}else{
						ballDir += Math.PI;
				}
			}
		}
		
		//Bollen träffar golvet/taket
		if(ballPos.y >= 1000 || ballPos.y <= 0){
			ballDir *= -1;
			
		}
	}
		
	 private void resetBall(){
		 ballPos.setLocation(750, 500);
		 ballDir = Math.random()*2.0*Math.PI;
		 while(ballDir > Math.PI / 4 && ballDir < 3 * Math.PI / 4 || ballDir > 5 * Math.PI / 4 && ballDir < 7 * Math.PI / 4) ballDir = Math.random()*2.0*Math.PI;
		 ballSpeed = 20;
		 //ballDir = Math.PI / 3;
	 }
	 
	 public int getBarPos(BarKey k){
		 return barPos.get(k);
	 }
	 public void setBarPos(BarKey k, int pos){
		 barPos.put(k, pos);
	 }
	 
	 public int getBarHeight(BarKey k){
		 return barHeight.get(k);
	 }
	 
	 public Point getBallPos(){
		 return ballPos;
	 }
	 
	 public String getMessage(){
		 return message;
	 }
	 
	 public String getScore(BarKey k){
		 return "" + score.get(k);
	 }
	 
	 public Dimension getFieldSize(){
		 return fieldSize;
	 }
	 
	
}
